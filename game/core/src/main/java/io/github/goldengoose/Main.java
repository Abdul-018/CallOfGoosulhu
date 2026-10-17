package io.github.goldengoose;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
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
        400,
        300,
        new OrthographicCamera()
    );

    GameWorld world = new GameWorld();
    Player player;

    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;

    @Override
    public void create() {
        batch = new SpriteBatch();
        player = new Player();
        world.addEntity(player);
        world.init();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        tiledMap = new TmxMapLoader().load("maps/base_map.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        viewport.apply();
        var camera = (OrthographicCamera) viewport.getCamera();
        camera.position.set(player.getPosition().x, player.getPosition().y, 0);
        camera.update();

        batch.setProjectionMatrix(viewport.getCamera().combined);
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        float deltaTime = Gdx.graphics.getDeltaTime();
        world.update(deltaTime);

        batch.begin();
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
