package io.github.goldengoose.goose;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.goldengoose.player.Player;
import io.github.goldengoose.shared.Entity;

/**
 * Goose AI plan:
 * IDLE: Wander around the campus, has cone-like visual detection, circular audial detection
 * ALERT: possibly not needed
 * CHASING: Chasing the player
 */
public class Goose extends Entity {
    enum GooseState {
        IDLE,
        ALERT,
        CHASING
    }

    private static final float IDLE_SPEED = 20f;
    private static final float CHASING_SPEED = 40f;
    // Maximum and minimum distance the goose will wander from its current position when idle, range is a square
    private static final float MIN_WANDER_RANGE = 40f;
    private static final float MAX_WANDER_RANGE = 80f;
    // Distance from where the Goose will stop following player and start honking
    private static final float HONK_RANGE = 50f;

    private final Vector2 velocity = new Vector2();
    private GooseState state = GooseState.CHASING;
    private final Player player;
    private Vector2 idleTarget;

    private boolean honk;

    public Goose(Player player, Vector2 position) {
        texture = new Texture("goose.png");
        this.player = player;
        this.position.set(position);
    }

    @Override
    public void gameStart() {
    }

    @Override
    public void update(float deltaTime) {
        if (player.getPosition().cpy().sub(position).len() > HONK_RANGE) {
            state = GooseState.CHASING;
        }
        else {
            state = GooseState.IDLE;
        }

        if (state.equals(GooseState.CHASING)) {
            moveTo(player.getPosition());
            position.add(velocity.scl(CHASING_SPEED*deltaTime));
        }
        else {
            wander();
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    private void wander() {
        if (idleTarget != null) {
            moveTo(idleTarget);
            return;
        }

        idleTarget = new Vector2(
            (float) Math.random() * (MAX_WANDER_RANGE - MIN_WANDER_RANGE) + MIN_WANDER_RANGE,
            (float) Math.random() * (MAX_WANDER_RANGE - MIN_WANDER_RANGE) + MIN_WANDER_RANGE
        );
    }

    private void moveTo(Vector2 pos) {
        if (pos == null) return;
        var direction = pos.cpy().sub(position);
        if (direction.len() < 3f) {
            pos = null;
            return;
        }

        velocity.set(Float.compare(direction.x, 0.0f), Float.compare(direction.y, 0.0f));
    }
}
