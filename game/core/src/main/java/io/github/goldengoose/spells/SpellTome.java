package io.github.goldengoose.spells;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.goldengoose.Cog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpellTome {
    private final float xPos;
    private final float yPos;

    OrthographicCamera camera;
    private final Stage stage;
    private final Cog game;
    private Texture texture;

    private final List<Spell> spells = new ArrayList<>();
    private int selectedSpell = 0;

    public SpellTome(OrthographicCamera camera, Cog game) {
        this.camera = camera;
        this.game = game;
        texture = new Texture("spell_tome.png");

        xPos = 20;
        yPos = Gdx.graphics.getHeight() - 20;

        stage = new Stage(new ScreenViewport(), game.batch);
        Gdx.input.setInputProcessor(stage);
    }

    public void draw(SpriteBatch batch) {
        batch.setProjectionMatrix(stage.getCamera().combined);

        float screenWidth = stage.getViewport().getWorldWidth();

        float scale = screenWidth / 800f;

        float width = texture.getWidth() * scale * 2f;
        float height = texture.getHeight() * scale * 1.5f;

        float padding = 20f * scale;

        float x = screenWidth - width - padding;
        float y = padding;

        batch.draw(texture, x, y, width, height);

        Spell currentSpell = getSelectedSpell();

        if (currentSpell != null) {
            Texture spellTexture = currentSpell.getTexture();

            float spellX = x + 11f * scale * 2f;
            float spellY = y + 19f * scale * 1.5f;

            float spellWidth = spellTexture.getWidth() * scale * 2f;
            float spellHeight = spellTexture.getHeight() * scale * 1.5f;

            batch.draw(
                spellTexture,
                spellX,
                spellY,
                spellWidth,
                spellHeight
            );
        }
    }

    /**
     * Returns the currently selected spell
     * @return the currently selected spell
     */
    public Spell getSelectedSpell() {
        return spells.get(selectedSpell);
    }

    /**
     * Changes the currently selected spell
     * @param i the "ID" of the spell to choose. ID is just the order
     * @return true if spell change happened, false if there was no spell at that id;
     */
    public boolean selectSpell(int i) {
        if (i >= spells.size() || spells.get(i) == null) return false;
        selectedSpell = i;
        return true;
    }

    public boolean addSpell(Spell spell) {
        if (spells.contains(spell)) return false;
        spells.add(spell);
        return true;
    }

    public void update(float deltaTime) {
        stage.act(deltaTime);
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
    }
}
