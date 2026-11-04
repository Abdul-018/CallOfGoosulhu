package io.github.goldengoose.goose;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Goose AI plan:
 * IDLE: Wander around the campus, has cone-like visual detection, circular audial detection
 * ALERT: possibly not needed
 * CHASING: Chasing the player
 */
public class GooseAI {
    enum GooseState {
        IDLE,
        CHASING
    }

    //------------
    // CONSTANTS
    //------------
    private static final float IDLE_SPEED = 20f;
    private static final float CHASING_SPEED = 40f;

    // Min and max x/y dist goose will wander from current pos when idle
    private static final float MIN_WANDER_RANGE = 40f;
    private static final float MAX_WANDER_RANGE = 80f;


    // Min and max time goose will wait after idle walk
    private static final float MIN_IDLE_WAIT = 5f;
    private static final float MAX_IDLE_WAIT = 10f;

    // Detection
    private static final float VISION_RANGE = 150f;
    private static final float VISION_HALF_ANGLE_DEGREES = 45f;
    private static final float HEARING_RANGE = 100f;

    // Distance from where the Goose will stop following player and start honking
    private static final float HONK_RANGE = 50f;
    // Range from which it will still follow the player, ie simply running far enough won't make the goose forget
    private static final float FOLLOW_RANGE = 200f;

    // Instead of chasing the player's exact position, each goose heads for it's own spot on a ring around the player
    // This prevents them from stacking ontop of each other like a blob when the player runs in a circle around them
    private static final float CHASE_RING_RADIUS = HONK_RANGE * .8f;

    // Geese's chasing target is picke7d within this many degrees either side of the direction the goose is running from
    // (So they don't pick a target on the otherside of the player - thus running through them)
    private static final float CHASE_SPREAD_ANGLE = 90f;

    //------------
    // END CONSTANTS
    //------------


    private GooseState state = GooseState.IDLE;

    private final Goose goose;
    private final Vector2 velocity = new Vector2();
    private final Vector2 direction = new Vector2();

    private final Vector2 idleTarget = new Vector2();
    private boolean hasIdleTarget = false;

    private boolean idleWaiting = false;
    private float waitTimer;


    private final Vector2 facingDirection = new Vector2(1f, 0f);
    private final Vector2 toPlayer = new Vector2();
    private final Vector2 chaseOffset = new Vector2();
    private final Vector2 chaseTarget = new Vector2();
    private static final float VISION_DOT_THRESHOLD =
        (float) Math.cos(Math.toRadians(VISION_HALF_ANGLE_DEGREES));

    private final Vector2 gooseCentre = new Vector2();
    private final Vector2 playerCentre = new Vector2();

    public GooseAI(Goose goose) {
        this.goose = goose;
    }


    public void update(float deltaTime) {
        goose.getCentre(gooseCentre);
        goose.getPlayer().getCentre(playerCentre);
        float playerDist2 = gooseCentre.dst2(playerCentre);

        // TODO: Once we determine how player makes noise, replace true with variable:
        boolean detected = canSeePlayer()
            || canHearPlayer(true);

        if (state == GooseState.CHASING) {
            if (playerDist2 > FOLLOW_RANGE * FOLLOW_RANGE) {
                state = detected ? GooseState.CHASING : GooseState.IDLE;
            }
        } else if (detected) {
            state = GooseState.CHASING;
            pickOffset(playerCentre, gooseCentre);
        }

        velocity.setZero();

        if (state == GooseState.CHASING) {
            hasIdleTarget = false;
            idleWaiting = false;

            chaseTarget.set(playerCentre).add(chaseOffset);
            moveTo(chaseTarget);

            // if (playerDist2 > HONK_RANGE * HONK_RANGE) {
                // moveTo(playerPosition);
            // } else {

            if (playerDist2 <= HONK_RANGE * HONK_RANGE){
                // TODO: Honk at the player.
            }
        } else {
            wander(deltaTime);
        }

        float speed = state == GooseState.CHASING
            ? CHASING_SPEED
            : IDLE_SPEED;

        goose.getPosition().mulAdd(velocity, speed * deltaTime);
    }

    private void pickOffset(Vector2 playerPosition, Vector2 goosePosition) {
        Vector2 playerToGoose = goosePosition.cpy().sub(playerPosition);
        float approachAngle = playerToGoose.isZero() ? 0f : playerToGoose.angleDeg();
        float spread = ((float) Math.random() * 2f - 1f) * CHASE_SPREAD_ANGLE;

        chaseOffset.set(CHASE_RING_RADIUS, 0f).setAngleDeg(approachAngle + spread);
    }
    private void wander(float deltaTime) {
        if (!hasIdleTarget) {
            chooseNewTarget();
            return;
        }

        if (idleWaiting) {
            waitTimer -= deltaTime;
            velocity.setZero();

            if (waitTimer <= 0f) {
                hasIdleTarget = false;
                idleWaiting = false;
            }

            return;
        }

        if (moveTo(idleTarget)) {
            velocity.setZero();
            idleWaiting = true;

            waitTimer = MathUtils.random(MIN_IDLE_WAIT, MAX_IDLE_WAIT);
        }
    }


    private void chooseNewTarget() {
        float angle = MathUtils.random(MathUtils.PI2);
        float distance = MathUtils.random(MIN_WANDER_RANGE, MAX_WANDER_RANGE);

        idleTarget.set(gooseCentre).add(MathUtils.cos(angle) * distance, MathUtils.sin(angle) * distance);

        hasIdleTarget = true;
        idleWaiting = false;
    }

    private boolean canHearPlayer(boolean playerMakingNoise) {
        if (!playerMakingNoise) {
            return false;
        }

        return gooseCentre.dst2(playerCentre) <= HEARING_RANGE * HEARING_RANGE;
    }

    private boolean canSeePlayer() {
        toPlayer.set(playerCentre).sub(gooseCentre);

        if (toPlayer.len2() > VISION_RANGE * VISION_RANGE) {
            return false;
        }

        toPlayer.nor();

        return facingDirection.dot(toPlayer) >= VISION_DOT_THRESHOLD;
    }

    private boolean moveTo(Vector2 target) {
        direction.set(target).sub(gooseCentre);

        if (direction.len2() < 3f * 3f) {
            velocity.setZero();
            return true;
        }
        velocity.set(direction).nor();

        facingDirection.set(velocity);
        return false;
    }

    public boolean isMoving() {
        return !velocity.isZero();
    }

    public boolean isChasing() {
        return state == GooseState.CHASING;
    }

    public Vector2 getFacingDirection() {
        return facingDirection;
    }

    public void renderDebug(ShapeRenderer shapes) {
        goose.getCentre(gooseCentre);

        float x = gooseCentre.x;
        float y = gooseCentre.y;

        // Hearing radius: circular range.
        shapes.setColor(0f, 0.7f, 1f, 1f);
        shapes.circle(x, y, HEARING_RANGE, 48);

        // Vision cone: two boundary rays and a connecting arc.
        float facingAngle = facingDirection.angleDeg();
        float halfAngle = VISION_HALF_ANGLE_DEGREES;

        float leftAngle = facingAngle + halfAngle;
        float rightAngle = facingAngle - halfAngle;

        float leftX = x + VISION_RANGE
            * (float) Math.cos(Math.toRadians(leftAngle));
        float leftY = y + VISION_RANGE
            * (float) Math.sin(Math.toRadians(leftAngle));

        float rightX = x + VISION_RANGE
            * (float) Math.cos(Math.toRadians(rightAngle));
        float rightY = y + VISION_RANGE
            * (float) Math.sin(Math.toRadians(rightAngle));

        shapes.setColor(1f, 0.8f, 0f, 1f);

        shapes.line(x, y, leftX, leftY);
        shapes.line(x, y, rightX, rightY);

        shapes.arc(
            x, y,
            VISION_RANGE,
            rightAngle,
            halfAngle * 2f,
            32
        );

        // Facing direction: short white line.
        shapes.setColor(1f, 1f, 1f, 1f);

        shapes.line(
            x, y,
            x + facingDirection.x * 25f,
            y + facingDirection.y * 25f
        );
    }
}
