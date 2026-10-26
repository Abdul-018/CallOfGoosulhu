package io.github.goldengoose.shared.enums;

public enum CogFonts {
    CRISTOFORO("fonts/Cristoforo.ttf");

    private final String path;
    CogFonts(final String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return path;
    }
}
