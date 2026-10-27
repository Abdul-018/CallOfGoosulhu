package io.github.goldengoose.spells;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.goldengoose.Cog;

public class SpellTome {

    private final float xPos;
    private final float yPos;

    OrthographicCamera camera;
    private final Stage stage;
    private final Cog game;


    public SpellTome(OrthographicCamera camera, Cog game) {
        this.camera = camera;
        this.game = game;

        xPos = 20;
        yPos = Gdx.graphics.getHeight() - 20;

        stage = new Stage(new ScreenViewport(), game.batch);
        Gdx.input.setInputProcessor(stage);
        //createTomeMenu();
    }

    public void update(float deltaTime) {

        //stage.act(deltaTime);
    }

//    public void resize(int width, int height) {
//        stage.getViewport().update(width, height, true);
//    }
    public void dispose() {

    }
}
