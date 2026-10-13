package io.github.goldengoose;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.goldengoose.player.Player;
import io.github.goldengoose.shared.GameWorld;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    private final Viewport viewport = new FitViewport(
        800,
        600,
        new OrthographicCamera()
    );

    GameWorld world = new GameWorld();
    Player player;

    @Override
    public void create() {
        batch = new SpriteBatch();
        //image = new Texture("libgdx.png");
        player = new Player();
        world.addEntity(player);
        world.init();

    }

    @Override
    public void render() {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        float deltaTime = Gdx.graphics.getDeltaTime();
        world.update(deltaTime);

        batch.begin();
        //batch.draw(image, 140, 210);
        world.render(batch);
        batch.end();

        //TODO: Remove once game has proper exit feature, this is just for testing
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}
