package io.github.goldengoose.hidden_events;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.goldengoose.world.Entity;
import io.github.goldengoose.world.GameWorld;
import io.github.goldengoose.goose.Bread;

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

    private enum State { HIDDEN, RISING, DISPENSED }

    private static final int BREAD_COUNT = 3;
    private static final float BREAD_SCATTER_RADIUS = 15;
    private static final float RISE_FRAME_SECONDS = 0.1f;
    private static final float IDLE_FRAME_SECONDS = 0.4f;

    private final GameWorld world;

    private final Animation<TextureRegion> rise;
    private final Animation<TextureRegion> idle;
    private final float zoneX, zoneY, zoneWidth;

    private State state = State.HIDDEN;
    private float stateTime = 0f;
    private final Vector2 centre = new Vector2();


    public BreadDispenser(GameWorld world, TextureAtlas atlas, float x, float y, float width, float height) {
        super("Bread dispenser", "Bread dispenser found", x, y, width, height, 0f);

        this.world = world;
        this.zoneX = x;
        this.zoneY = y;
        this.zoneWidth = width;

        rise = new Animation<TextureRegion>(RISE_FRAME_SECONDS, atlas.findRegions("dispenser_rise"), Animation.PlayMode.NORMAL);
        idle = new Animation<TextureRegion>(IDLE_FRAME_SECONDS, atlas.findRegions("dispenser_idle"), Animation.PlayMode.LOOP);
    }

    // Player steps into zone
    @Override
    protected void onEnter(Entity entity) {
        if (state != State.HIDDEN) {
            return;
        }

        if (trigger()) {
            state = State.RISING;
            stateTime = 0f;
        }
    }

    @Override
    protected void onUpdate(float deltaTime) {
        if (state == State.HIDDEN) {
            return;
        }

        stateTime += deltaTime;
        if (state == State.RISING && stateTime >= rise.getAnimationDuration()) {
            state = State.DISPENSED;
            stateTime = 0f;
            dispenseBread();
        }
    }

    private void dispenseBread() {
        float baseX = zoneX + zoneWidth / 2f;
        float baseY = zoneY;
        for (int i = 0; i < BREAD_COUNT; i++) {
            // Bread dispersion on a semicircle
            double angle = Math.PI + Math.PI * (i + 1) / (BREAD_COUNT + 1);
            float breadX = baseX + (float) Math.cos(angle) * BREAD_SCATTER_RADIUS;
            float breadY = baseY + (float) Math.sin(angle) * BREAD_SCATTER_RADIUS;
            world.addEntity(new Bread(new Vector2(breadX, breadY)));
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (state == State.HIDDEN) {
            return;
        }

        TextureRegion frame = (state == State.RISING)
            ? rise.getKeyFrame(stateTime, false)
            : idle.getKeyFrame(stateTime, true);

        float w = frame.getRegionWidth();
        float h = frame.getRegionHeight();
        batch.draw(frame, zoneX + zoneWidth / 2f - w / 2f, zoneY, w, h);
    }

    public boolean isDispensed() {
        return state == State.DISPENSED;
    }

    public boolean isPoppingUp() {
        return state == State.RISING;
    }
}
