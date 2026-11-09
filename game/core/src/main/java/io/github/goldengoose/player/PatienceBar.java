package io.github.goldengoose.shared;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Patience bar drawn in the game world, floating above the player.
 * Shape-based: no assets needed. Colour goes green -> yellow -> red as patience drops.
 * Use the GAME camera (not hudCamera) when rendering.
 */
public class PatienceBar {
    // Size in world units (the game viewport is 400x300)
    private static final float WIDTH = 30f;
    private static final float HEIGHT = 4f;
    private static final float BORDER = 1f;

    // How far above the player's position the bar floats (tune to the sprite size)
    private static final float Y_OFFSET = 20f;

    // How fast the bar catches up with the real value (higher = snappier)
    private static final float SMOOTHING = 10f;

    private float displayedPercent = 1f;

    /** Smoothly move the displayed value towards the real patience. percent in [0..1]. */
    public void update(float delta, float targetPercent) {
        float t = Math.min(1f, SMOOTHING * delta);
        displayedPercent = MathUtils.lerp(displayedPercent, MathUtils.clamp(targetPercent, 0f, 1f), t);
    }

    /**
     * Must be called OUTSIDE of batch.begin()/end().
     * @param worldCamera the game camera (same one used to render the world)
     * @param playerPosition the player's position; the bar is centred above it
     */
    public void render(ShapeRenderer shapes, Camera worldCamera, Vector2 playerPosition) {
        float x = playerPosition.x - WIDTH / 2f;
        float y = playerPosition.y + Y_OFFSET;

        shapes.setProjectionMatrix(worldCamera.combined);
        shapes.begin(ShapeRenderer.ShapeType.Filled);

        // Border
        shapes.setColor(0f, 0f, 0f, 0.9f);
        shapes.rect(x - BORDER, y - BORDER, WIDTH + 2 * BORDER, HEIGHT + 2 * BORDER);

        // Background (empty part)
        shapes.setColor(0.25f, 0.25f, 0.25f, 0.9f);
        shapes.rect(x, y, WIDTH, HEIGHT);

        // Fill
        setFillColor(shapes, displayedPercent);
        shapes.rect(x, y, WIDTH * displayedPercent, HEIGHT);

        shapes.end();
    }

    private void setFillColor(ShapeRenderer shapes, float percent) {
        if (percent > 0.5f) {
            shapes.setColor(0.2f, 0.8f, 0.2f, 1f);   // green
        } else if (percent > 0.25f) {
            shapes.setColor(0.95f, 0.8f, 0.1f, 1f);  // yellow
        } else {
            shapes.setColor(0.9f, 0.2f, 0.2f, 1f);   // red
        }
    }
}
