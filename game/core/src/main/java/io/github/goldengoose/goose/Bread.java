package io.github.goldengoose.goose;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.github.goldengoose.world.Entity;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * A piece of bread dropped by the bread dispenser (FR_BREAD_DISPENSER).
 *
 * Acts as a lure for geese. Outweighs the player's importance such that chasing geese will disregard the player
 * and chase the bread to eat instead.
 *
 * To keep one piece from pulling every goose on the map, each piece only attracts
 * MAX_EATERS geese. A goose that cannot claim a slot just ignores this piece.
 *
 * Bread works like any other entity: GameWorld calls update() and interact() on it,
 * and removes it once isExpired() is true.
 */

public class Bread extends Entity {
    private static final float LIFETIME_SECONDS = 8f;
    private static final float LURE_RADIUS = 120f;
    private static final int MAX_EATERS = 2;
    private static final float SIZE = 12f;

    // future art functionality
    private static TextureRegion sprite;

    // Identity set
    private final Set<Goose> eaters = Collections.newSetFromMap(new IdentityHashMap<>());
    private final Vector2 otherCentre = new Vector2();
    private final Vector2 centre = new Vector2();
    private float age = 0f;

    // Where the bread lies on the map
    public Bread(Vector2 centre) {
        position.set(centre.x - SIZE / 2f, centre.y - SIZE / 2f); // Entity positions are bottom-left corners
    }

    public static void setSprite(TextureRegion region) {
        sprite = region;
    }

    @Override
    public void update(float deltaTime) {
        age += deltaTime;
    }


    @Override
    public void interact(Entity other) {
        // TODO: ignore this bread if it has expired
        // TODO: if the goose's centre is within LURE_RADIUS of this bread's centre then distract.
    }


    public boolean tryClaim(Goose goose) {
        return false;
    }

    public int getEaterCount() {
        return eaters.size();
    }

    @Override
    public boolean isExpired() {
        return age >= LIFETIME_SECONDS;
    }

    @Override
    public void render(SpriteBatch batch) {
        // No art yet ):
        if (sprite == null) {
            return;
        }
        batch.draw(sprite, position.x, position.y, SIZE, SIZE);
    }

    public void renderDebug(ShapeRenderer shapes) {
        // TODO: draw a small circle for the bread and a larger ring showing the lure radius
    }

    @Override
    public float getWidth() {
        return SIZE;
    }

    @Override
    public float getHeight() {
        return SIZE;
    }
}
