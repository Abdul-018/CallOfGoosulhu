package io.github.goldengoose.shared;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import io.github.goldengoose.shared.enums.CogFonts;

public class Utilities {
    public static BitmapFont createFont(CogFonts fontPath, int size) {
        FreeTypeFontGenerator generator =
            new FreeTypeFontGenerator(Gdx.files.internal(fontPath.toString()));

        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
            new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = size;

        BitmapFont font = generator.generateFont(parameter);

        generator.dispose();

        return font;
    }
}
