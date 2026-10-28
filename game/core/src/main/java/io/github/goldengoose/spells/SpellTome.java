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

    // 10 because max will be 5 starting and 5 findable items
    private final Spell[] spells = new Spell[10];
    private int selectedSpell = 0;

    public SpellTome(OrthographicCamera camera, Cog game) {
        this.camera = camera;
        this.game = game;

        xPos = 20;
        yPos = Gdx.graphics.getHeight() - 20;

        stage = new Stage(new ScreenViewport(), game.batch);
        Gdx.input.setInputProcessor(stage);
        //createTomeMenu();
    }

    /**
     * Returns the currently selected spell
     * @return the currently selected spell
     */
    public Spell getSelectedSpell() {
        return spells[selectedSpell];
    }

    /**
     * Changes the currently selected spell
     * @param i the "ID" of the spell to choose. ID is just the order
     * @return true if spell change happened, false if there was no spell at that id;
     */
    public boolean selectSpell(int i) {
        if (i >= spells.length || spells[i] == null) return false;
        selectedSpell = i;
        return true;
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
