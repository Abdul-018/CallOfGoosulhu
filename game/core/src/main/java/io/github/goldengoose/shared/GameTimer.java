package io.github.goldengoose.shared;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/** Game timer, press p to pause and unpause. (UR_GAME_TIMER, UR_PAUSE_GAME) */
public class GameTimer {

    /** Time left, starts at 5 mins. (UR_GAME_TIMER) */
    private float secondsRemaining = 5 * 60;

    /** Game starts paused, also check. (UR_PAUSE_GAME) */
    private boolean isPaused = true;

    /** Every frame, check pause key (P) and run timer. (UR_GAME_TIMER, UR_PAUSE_GAME) */
    public void update(float deltaTime) {

        // Pressing P switches between paused and unpaused. (FR_PAUSE)
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            isPaused = !isPaused;
        }

        // Take time off the timer, but only when not paused.
        if (!isPaused) {
            secondsRemaining = secondsRemaining - deltaTime;
        }

        // Duh.
        if (secondsRemaining < 0) {
            secondsRemaining = 0;
        }
    }

    /** True while game is paused. (UR_PAUSE_GAME) */
    public boolean isPaused() { return isPaused; }

    /** True once clock reaches zero. (UR_GAME_TIMER, UR_WIN, FR_TIMER_COUNTDOWN) */
    public boolean isTimeUp() { return secondsRemaining <= 0; }

    /** How many seconds left. (UR_SCORE) */
    public float getSecondsRemaining() { return secondsRemaining; }
}
