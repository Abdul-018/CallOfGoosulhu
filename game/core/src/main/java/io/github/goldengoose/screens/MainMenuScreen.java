package io.github.goldengoose.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.goldengoose.Cog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


import java.awt.*;

public class MainMenuScreen implements Screen {

    final Cog game;

    private Stage stage;
    private BitmapFont menuFont;

    public MainMenuScreen(final Cog game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport(), game.batch);
        Gdx.input.setInputProcessor(stage);

        createMenu();
    }

    private void createMenu() {
        menuFont = createFont();

        com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle titleStyle = new com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle();
        titleStyle.font = menuFont;
        titleStyle.fontColor = Color.WHITE;

        Label title = new Label("Call of Goosulhu", titleStyle);

        TextButton.TextButtonStyle buttonStyle =
            new TextButton.TextButtonStyle();

        buttonStyle.font = menuFont;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.overFontColor = Color.YELLOW;

        buttonStyle.up = new BaseDrawable();
        buttonStyle.down = new BaseDrawable();
        buttonStyle.over = new BaseDrawable();

        TextButton playButton =
            new TextButton("PLAY", buttonStyle);

        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });

        Table table = new Table();
        table.setFillParent(true);

        table.add(title).padBottom(50);
        table.row();
        table.add(playButton).width(250).height(80);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        stage.act(delta);
        stage.draw();
    }


    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
        stage.getViewport().update(width, height, true);
    }


    @Override
    public void dispose() {
        stage.dispose();
        menuFont.dispose();
    }

    private BitmapFont createFont() {
        FreeTypeFontGenerator generator =
            new FreeTypeFontGenerator(Gdx.files.internal("fonts/Cristoforo.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
            new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 64;

        BitmapFont font = generator.generateFont(parameter);

        generator.dispose();

        return font;
    }

    // Unused overrides
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
