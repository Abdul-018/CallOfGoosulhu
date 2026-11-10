package io.github.goldengoose.hidden_events;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import io.github.goldengoose.world.Entity;
import io.github.goldengoose.world.GameWorld;

/**
 * FR_BREAD_DISPENSER: "Dispense bread that distracts nearby geese."
 *
 * A one-time hidden event. The dispenser sits in one spot, mostly hidden until
 * the player walks into it's trigger zone. It will then pop up out of the ground (animation)
 * and then drop a few pieces of bread around itself. It cannot be triggered again.
 *
 * What the bread does to the geese is NOT decided here. Bread is it's own entity and gooseAi decides
 * how strongly to be pulled towards it. But each bread entity will outweigh the player.
 */

public class BreadDispenser extends HiddenEvent {

    private enum State { HIDDEN, POPPING_UP, DISPENSED }

    private static final int BREAD_COUNT = 3;
    private static final float BREAD_SCATTER_RADIUS = 28f;
    private static final float FALLBACK_POP_UP_SECONDS = 1f;

    private final GameWorld world;
    private final Animation<TextureRegion> popUpAnimation;
    private final float popUpDuration;

    private State state = State.HIDDEN;
    private float stateTime = 0f;
    private final Vector2 centre = new Vector2();

    public BreadDispenser(GameWorld world, Animation<TextureRegion> popUpAnimation,
                               float x, float y, float width, float height) {
        super("Bread dispenser", "Bread dispenser found", x, y, width, height, 0f);

        this.world = world;
        this.popUpAnimation = popUpAnimation;
        this.popUpDuration = popUpAnimation != null
            ? popUpAnimation.getAnimationDuration()
            : FALLBACK_POP_UP_SECONDS;
    }

    // Player steps into zone
    @Override
    protected void onEnter(Entity entity) {
        // TODO: if the dispenser has already been used, then do nothing.
        // TODO: otherwise call trigger
    }

    @Override
    protected void onUpdate(float deltaTime) {
        // TODO: only act while has popped up.
    }

    private void dispenseBread() {
        // TODO: for each of BREAD_COUNT pieces, work out a point on a circle around the bread dispenser's center.
        //  Bread should be evenly spaced, and add a new Bread to world Entity
    }

    @Override
    public void render(SpriteBatch batch) {
        // TODO: draw nothing while HIDDEN or if popUpAnimation is null.
        // TODO: otherwise draw popUpAnimation.getKeyFrame(stateTime)
    }

    public boolean isDispensed() {
        return state == State.DISPENSED;
    }

    public boolean isPoppingUp() {
        return state == State.POPPING_UP;
    }
}
