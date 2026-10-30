package io.github.goldengoose.goose;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.github.goldengoose.player.Player;
import io.github.goldengoose.shared.Entity;

public class Goose extends Entity {
    private final GooseAI gooseAI = new GooseAI(this);
    private final Player player;

    private boolean honk;
    private float trappedTimer = 0f;

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
        if (trappedTimer > 0f) {
            trappedTimer -= deltaTime;
            return;
        }

        gooseAI.update(deltaTime);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public void renderDebug(ShapeRenderer shapes) {
        gooseAI.renderDebug(shapes);
    }

    public Player getPlayer() {
        return player;
    }

    public void trap(float duration) {
        trappedTimer = Math.max(trappedTimer, duration);
    }

    public boolean isTrapped() {
        return trappedTimer > 0f;
    }
}
