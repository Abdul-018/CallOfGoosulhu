package io.github.goldengoose.shared;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.github.goldengoose.goose.Goose;
import io.github.goldengoose.player.Player;

import java.util.ArrayList;
import java.util.List;

public class GameWorld {
    private final List<Entity> entities = new ArrayList<>();

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void init() {
        entities.add(new Goose((Player) entities.get(0), new Vector2(200, 200)));
        for (Entity entity : entities) {
            entity.gameStart();
        }
    }

    public void update(float deltaTime) {
        for (Entity entity : entities) {
            entity.update(deltaTime);
        }
    }

    public void render(SpriteBatch batch) {
        for (Entity entity : entities) {
            entity.render(batch);
        }
    }

    public void renderDebug(ShapeRenderer shapes) {
        for (Entity entity : entities) {
            if (entity instanceof Goose goose) {
                goose.renderDebug(shapes);
            }
        }
    }
}
