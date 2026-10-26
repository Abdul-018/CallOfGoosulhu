package io.github.goldengoose.shared;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import io.github.goldengoose.shared.enums.CogFonts;

public class Utilities {
    public static BitmapFont createFont(CogFonts fontPath) {
        FreeTypeFontGenerator generator =
            new FreeTypeFontGenerator(Gdx.files.internal(fontPath.toString()));

        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
            new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 64;

        BitmapFont font = generator.generateFont(parameter);

        generator.dispose();

        return font;
    }
}
