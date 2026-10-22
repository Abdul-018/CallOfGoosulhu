package io.github.goldengoose.goose;

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

    // Distance from where the Goose will stop following player and start honking
    private static final float HONK_RANGE = 50f;
    // Range from which goose will notice player
    private static final float NOTICE_RANGE = 150f;
    // Range from which it will still follow the player, ie simply running far enough won't make the goose forget
    private static final float FOLLOW_RANGE = 200f;


    // Min and max time goose will wait after idle walk
    private static final float MIN_IDLE_WAIT = 5f;
    private static final float MAX_IDLE_WAIT = 10f;
    //------------
    // END CONSTANTS
    //------------


    private GooseState state = GooseState.IDLE;
    private Vector2 idleTarget;
    private final Goose goose;
    private final Vector2 velocity = new Vector2();

    private boolean idleWaiting = false;
    private float waitTimer;

    public GooseAI(Goose goose) {
        this.goose = goose;
    }

    public void update(float deltaTime) {
        float playerDist = goose.getPlayer().getPosition().cpy().sub(goose.getPosition()).len();
        if (playerDist < NOTICE_RANGE || (state == GooseState.CHASING && playerDist < FOLLOW_RANGE)) {
            idleTarget = null;
            state = GooseState.CHASING;
        }
        else {
            state = GooseState.IDLE;
        }

        if (state.equals(GooseState.CHASING) && playerDist > HONK_RANGE) {
            moveTo(goose.getPlayer().getPosition());
            goose.getPosition().mulAdd(velocity, CHASING_SPEED * deltaTime);
        }
        else if(state.equals(GooseState.IDLE)) {
            wander(deltaTime);
            goose.getPosition().mulAdd(velocity, IDLE_SPEED * deltaTime);
        }
    }

    private void wander(float deltaTime) {
        if (idleTarget == null) {
            chooseNewTarget();
            return;
        }

        if (idleWaiting) {
            waitTimer -= deltaTime;
            velocity.setZero();

            if (waitTimer <= 0f) {
                idleTarget = null;
                idleWaiting = false;
            }

            return;
        }

        if (moveTo(idleTarget)) {
            velocity.setZero();
            idleWaiting = true;

            waitTimer = MIN_IDLE_WAIT
                + (float) Math.random() * (MAX_IDLE_WAIT - MIN_IDLE_WAIT);
        }
    }


    private void chooseNewTarget() {
        float angle = (float) (Math.random() * 2 * Math.PI);
        float distance = MIN_WANDER_RANGE
            + (float) Math.random() * (MAX_WANDER_RANGE - MIN_WANDER_RANGE);

        idleTarget = new Vector2(
            goose.getPosition().x + (float) Math.cos(angle) * distance,
            goose.getPosition().y + (float) Math.sin(angle) * distance
        );
        idleWaiting = false;
    }

    private boolean moveTo(Vector2 target) {
        Vector2 direction = target.cpy().sub(goose.getPosition());

        if (direction.len() < 3f) {
            velocity.setZero();
            return true;
        }

        velocity.set(direction).nor();
        return false;
    }
}
