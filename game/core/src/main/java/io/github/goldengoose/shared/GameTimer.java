package io.github.goldengoose.shared;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/** Game timer, press p to pause and unpause. (UR_GAME_TIMER, UR_PAUSE_GAME) */
public class GameTimer {

    /** Time left, starts at 5 mins. (UR_GAME_TIMER) */
    private float secondsRemaining = 5 * 60;

    /** Game starts paused, also check. (UR_PAUSE_GAME) */
    private boolean isPaused = true;

    private final float xPos;
    private final float yPos;

    BitmapFont font;
    OrthographicCamera camera;

    public GameTimer(OrthographicCamera camera) {
        font = new BitmapFont();
        this.camera = camera;

        xPos = 20;
        yPos = Gdx.graphics.getHeight() - 20;
    }

    /** Every frame, check pause key (P) and run timer. (UR_GAME_TIMER, UR_PAUSE_GAME) */
    public void update(float deltaTime) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            isPaused = !isPaused;
        }

        if (!isPaused) {
            secondsRemaining -= deltaTime;
        }

        if (secondsRemaining < 0) {
            secondsRemaining = 0;
        }
    }

    public void draw(SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);

        font.draw(batch, String.format("%.1f", secondsRemaining), xPos, yPos);
        font.draw(batch, "Press P to pause", xPos, yPos - 20);
    }

    public void drawPauseOverlay(ShapeRenderer shapeRenderer) {
        if (isPaused) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(
                GL20.GL_SRC_ALPHA,
                GL20.GL_ONE_MINUS_SRC_ALPHA
            );

            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            shapeRenderer.setColor(0f, 0f, 0f, 0.5f);
            shapeRenderer.rect(
                0,
                0,
                camera.viewportWidth,
                camera.viewportHeight
            );

            shapeRenderer.end();
        }
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    /** True while game is paused. (UR_PAUSE_GAME) */
    public boolean isPaused() { return isPaused; }

    /** True once clock reaches zero. (UR_GAME_TIMER, UR_WIN, FR_TIMER_COUNTDOWN) */
    public boolean isTimeUp() { return secondsRemaining <= 0; }

    /** How many seconds left. (UR_SCORE) */
    public float getSecondsRemaining() { return secondsRemaining; }
}
