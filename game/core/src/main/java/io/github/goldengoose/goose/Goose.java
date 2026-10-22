package io.github.goldengoose.goose;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.goldengoose.player.Player;
import io.github.goldengoose.shared.Entity;

public class Goose extends Entity {
    private final GooseAI gooseAI = new GooseAI(this);
    private final Player player;

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
        gooseAI.update(deltaTime);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public Player getPlayer() {
        return player;
    }
}
