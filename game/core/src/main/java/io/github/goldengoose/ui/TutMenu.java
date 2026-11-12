package io.github.goldengoose.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.goldengoose.Cog;
import io.github.goldengoose.assets.CogFonts;
import io.github.goldengoose.spells.SpellTome;
import io.github.goldengoose.util.Utilities;

public class TutMenu {
    private static final float REFERENCE_WIDTH = 800f;
    private static final float SCALE_X = 6f;
    private static final float SCALE_Y = 4.5f;
    private static final float ICON_OFFSET_X = 11f;
    private static final float ICON_OFFSET_Y = 19f;

    private static final float PAGE_X = 51f;
    private static final float PAGE_Y = 18f;
    private static final float PAGE_WIDTH = 28f;
    private static final float PAGE_HEIGHT = 46f;
    private static final float PAGE_PADDING = 2f;
    private static final Color INK = new Color(0.23f, 0.15f, 0.2f, 1f);

    private final BitmapFont titleFont = Utilities.createFont(CogFonts.CRISTOFORO, 28);
    private final BitmapFont bodyFont = Utilities.createFont(CogFonts.CRISTOFORO, 16);
    private final GlyphLayout layout = new GlyphLayout();

    private final Texture texture;

    public TutMenu(Texture texture) {
        this.texture = texture;
    }

    public void draw(SpriteBatch batch, Viewport hud) {
        float screenWidth = hud.getWorldWidth();
        float screenHeight = hud.getWorldHeight();
        float scale = screenWidth / REFERENCE_WIDTH;

        float sx = scale * SCALE_X;
        float sy = scale * SCALE_Y;

        float width = texture.getWidth() * sx;
        float height = texture.getHeight() * sy;

        float x = (screenWidth - width) / 2f;
        float y = (screenHeight - height) / 2f;

        batch.draw(texture, x, y, width, height);

        float pageX = x + (PAGE_X + PAGE_PADDING) * sx;
        float pageTop = y + (PAGE_Y + PAGE_HEIGHT - PAGE_PADDING) * sy;
        float pageWidth = (PAGE_WIDTH - PAGE_PADDING * 2) * sx;

        titleFont.getData().setScale(scale);
        bodyFont.getData().setScale(scale);
        // LEFT PAGE - Tutorial controls
        float leftPageX = x + 10f * sx;

        layout.setText(titleFont, "Controls", INK, pageWidth, Align.center, true);
        titleFont.draw(batch, layout, leftPageX, pageTop);

        float leftBodyTop = pageTop - layout.height - 10f * scale;

        String controls = "WASD - Movement\n\n" +
            "T- Open Tome Menu\n\n" +
            "Arrow Keys - Turn the tome book pages";

        layout.setText(bodyFont, controls,
            INK, pageWidth, Align.left, true);
        bodyFont.draw(batch, layout, leftPageX, leftBodyTop);

        // RIGHT PAGE - Goal
        layout.setText(titleFont, "Goal", INK, pageWidth, Align.center, true);

        titleFont.draw(batch, layout, pageX, pageTop);

        float rightBodyTop = pageTop - layout.height - 10f * scale;

        String goal = "Disperse required amount of geese " +
            "within the time limit " +
            "in order to leave.";

        layout.setText(bodyFont, goal, INK, pageWidth, Align.left, true);

        bodyFont.draw(batch, layout, pageX, rightBodyTop);
    }
    public void dispose() {

        titleFont.dispose();
        bodyFont.dispose();
    }
}
