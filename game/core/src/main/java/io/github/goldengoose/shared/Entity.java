package io.github.goldengoose.shared;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Abstract class for entities, basically anything with an in-game position and a texture
 */
public abstract class Entity {
    protected final Vector2 position = new Vector2();
    protected Texture texture;

    //----------------
    // Game loop
    //----------------

    /**
     * Called on game start
     */
    public abstract void gameStart();

    /**
     * Called every frame, use for updating logic
     * @param deltaTime The time in seconds since the last frame. Used so framerate doesn't impact movement speed for example
     */
    public abstract void update(float deltaTime);

    /**
     * Called every frame, use for drawing textures
     * @param batch Used for drawing things on the screen
     */
    public abstract void render(SpriteBatch batch);

    //----------------
    // Position
    //----------------

    /**
     * Returns the world position of the entity
     * @return The position of the entity as a Vector2
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Sets the world position of the entity
     */
    public void setPosition(float x, float y) {
        position.set(x, y);
    }

    public void dispose() {
        if (texture != null) {
            texture.dispose();
            texture = null;
        }
    }
}
