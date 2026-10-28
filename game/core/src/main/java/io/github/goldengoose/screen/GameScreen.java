package io.github.goldengoose.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.goldengoose.Cog;
import io.github.goldengoose.player.Player;
import io.github.goldengoose.shared.GameTimer;
import io.github.goldengoose.shared.GameWorld;
import io.github.goldengoose.spells.PentagramTrapSpell;
import io.github.goldengoose.spells.SpellTome;

public class GameScreen implements Screen {
    final Cog game;

    private final Viewport viewport = new FitViewport(
        400,
        300,
        new OrthographicCamera()
    );
    OrthographicCamera hudCamera;

    GameWorld world = new GameWorld();
    GameTimer timer;
    SpellTome spellTome;
    Player player;

    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;

    private final ShapeRenderer shapeRenderer;

    public GameScreen(final Cog game) {
        this.game = game;

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight());
        hudCamera.update();

        spellTome = new SpellTome(hudCamera, this.game);
        PentagramTrapSpell pentagramTrapSpell = new PentagramTrapSpell(this.player);
        spellTome.addSpell(pentagramTrapSpell);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer = new ShapeRenderer();
        player = new Player();

        world.addEntity(player);
        world.init();

        timer = new GameTimer(hudCamera, game);

        tiledMap = new TmxMapLoader().load("maps/base_map.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        viewport.apply();
        var camera = (OrthographicCamera) viewport.getCamera();
        camera.position.set(player.getPosition().x, player.getPosition().y, 0);
        camera.update();

        game.batch.setProjectionMatrix(viewport.getCamera().combined);
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();


        if (!timer.isPaused()) {
            world.update(delta);
        }
        timer.update(delta);
        spellTome.update(delta);

        game.batch.begin();
        world.render(game.batch);
        // Comment/uncomment to see debugging lines
        //renderDebug(camera);
        game.batch.end();

        timer.drawPauseOverlay(shapeRenderer);

        game.batch.begin();
        timer.draw(game.batch);
        spellTome.draw(game.batch);
        game.batch.end();

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
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        timer.resize(width, height);
        spellTome.resize(width, height);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        tiledMap.dispose();
        timer.dispose();
        spellTome.dispose();
    }

    // Unused overrides
    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
