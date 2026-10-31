package io.github.goldengoose.spells;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.goldengoose.goose.Goose;
import io.github.goldengoose.shared.Entity;

public class PentagramTrap extends Entity {

    private static final float RADIUS = 15f;
    private static final float LIFETIME = 8f;

    private static final Texture TEXTURE =
        new Texture("pentagram.png");

    private float lifetime = LIFETIME;
    private boolean triggered = false;

    public PentagramTrap(Vector2 position) {
        this.position.set(position);
        texture = TEXTURE;
    }

    @Override
    public void gameStart() {
    }

    @Override
    public void update(float deltaTime) {
        lifetime -= deltaTime;
    }

    @Override
    public void render(SpriteBatch batch) {
        float size = RADIUS * 2f;

        batch.draw(
            texture,
            position.x - size / 2f,
            position.y - size / 2f,
            size,
            size
        );
    }

    public void checkGoose(Goose goose) {
        if (triggered || lifetime <= 0f) return;

        if (position.dst2(goose.getPosition()) <= RADIUS * RADIUS) {
            goose.getPosition().set(
                position.x - goose.getWidth() / 2f,
                position.y - goose.getHeight() / 2f
            );
            goose.trap(lifetime);
            triggered = true;
        }

    }

    public boolean isExpired() {
        return lifetime <= 0f;
    }

    public boolean isTriggered() {
        return triggered;
    }

    public float getLifetime() {
        return lifetime;
    }

    public static void disposeTexture() {
        TEXTURE.dispose();
    }

    @Override
    public void dispose() {
        // Do nothing: TEXTURE is shared between all pentagrams.
    }
}
