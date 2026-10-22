package io.github.goldengoose;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.goldengoose.player.Player;
import io.github.goldengoose.shared.GameWorld;
import io.github.goldengoose.shared.GameTimer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    BitmapFont font;
    private final Viewport viewport = new FitViewport(
        1200,
        900,
        new OrthographicCamera()
    );

    GameWorld world = new GameWorld();
    GameTimer timer = new GameTimer();
    Player player;

    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;

    private ShapeRenderer shapeRenderer;

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        player = new Player();
        world.addEntity(player);
        world.init();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        tiledMap = new TmxMapLoader().load("maps/base_map.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        font = new BitmapFont();

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
        timer.update(deltaTime);

        if (!timer.isPaused()) {
            world.update(deltaTime);
        }

        batch.begin();
        world.render(batch);
        // Comment/uncomment to see debugging lines
        //renderDebug(camera);

        float x = camera.position.x - 190;
        float y = camera.position.y + 140;

        String timeLeft = String.format("%.1f", timer.getSecondsRemaining());

        font.draw(batch, timeLeft, x, y);
        font.draw(batch, "Press P to pause", x, y -20);

        batch.end();

        //TODO: Remove once game has proper exit feature, this is just for testing
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    private void renderDebug(Camera camera) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        world.renderDebug(shapeRenderer);
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
        shapeRenderer.dispose();
        tiledMap.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}
