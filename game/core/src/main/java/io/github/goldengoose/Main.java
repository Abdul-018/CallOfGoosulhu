//package io.github.goldengoose;
//
//import com.badlogic.gdx.ApplicationAdapter;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input;
//import com.badlogic.gdx.graphics.Camera;
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
//import com.badlogic.gdx.maps.tiled.TiledMap;
//import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
//import com.badlogic.gdx.maps.tiled.TmxMapLoader;
//import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
//import com.badlogic.gdx.utils.ScreenUtils;
//import com.badlogic.gdx.utils.viewport.FitViewport;
//import com.badlogic.gdx.utils.viewport.Viewport;
//import io.github.goldengoose.player.Player;
//import io.github.goldengoose.shared.GameWorld;
//import io.github.goldengoose.shared.GameTimer;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//
///** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
//public class Main extends ApplicationAdapter {
//    private SpriteBatch batch;
//    private Texture image;
//    private final Viewport viewport = new FitViewport(
//        400,
//        300,
//        new OrthographicCamera()
//    );
//    OrthographicCamera hudCamera;
//
//    GameWorld world = new GameWorld();
//    GameTimer timer;
//    Player player;
//
//    TiledMap tiledMap;
//    TiledMapRenderer tiledMapRenderer;
//
//    private ShapeRenderer shapeRenderer;
//
//    @Override
//    public void create() {
//        shapeRenderer = new ShapeRenderer();
//        batch = new SpriteBatch();
//        player = new Player();
//
//        hudCamera = new OrthographicCamera();
//        hudCamera.setToOrtho(false, Gdx.graphics.getWidth(),
//            Gdx.graphics.getHeight());
//        hudCamera.update();
//
//        world.addEntity(player);
//        world.init();
//
//        timer = new GameTimer(hudCamera);
//
//        tiledMap = new TmxMapLoader().load("maps/base_map.tmx");
//        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
//    }
//
//    @Override
//    public void render() {
//        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
//
//        viewport.apply();
//        var camera = (OrthographicCamera) viewport.getCamera();
//        camera.position.set(player.getPosition().x, player.getPosition().y, 0);
//        camera.update();
//
//        batch.setProjectionMatrix(viewport.getCamera().combined);
//        tiledMapRenderer.setView(camera);
//        tiledMapRenderer.render();
//
//        float deltaTime = Gdx.graphics.getDeltaTime();
//
//        if (!timer.isPaused()) {
//            world.update(deltaTime);
//        }
//        timer.update(deltaTime);
//
//        batch.begin();
//        world.render(batch);
//        timer.draw(batch);
//
//        // Comment/uncomment to see debugging lines
//        //renderDebug(camera);
//
//        batch.end();
//
//        //TODO: Remove once game has proper exit feature, this is just for testing
//        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
//            Gdx.app.exit();
//        }
//    }
//
//    private void renderDebug(Camera camera) {
//        shapeRenderer.setProjectionMatrix(camera.combined);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        world.renderDebug(shapeRenderer);
//        shapeRenderer.end();
//    }
//
//    @Override
//    public void dispose() {
//        batch.dispose();
//        image.dispose();
//        shapeRenderer.dispose();
//        tiledMap.dispose();
//    }
//
//    @Override
//    public void resize(int width, int height) {
//        viewport.update(width, height, true);
//    }
//}
