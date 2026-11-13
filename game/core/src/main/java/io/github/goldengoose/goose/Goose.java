package io.github.goldengoose.goose;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.github.goldengoose.player.Player;
import io.github.goldengoose.assets.Assets;
import io.github.goldengoose.world.Entity;
import io.github.goldengoose.util.Utilities;

public class Goose extends Entity {
    //------------
    // CONSTANTS
    //------------

    // For the "?" that appears above a confused goose
    private static TextureRegion confusedIcon;
    private static float ICON_GAP = 2f;
    private static final float ICON_BOB = 1.5f;
    private static final float ICON_POP_SECONDS = 0.15f;
    private static final float ICON_FADE_SECONDS = 0.3f;
    private static final float ICON_SCALE = 1.5f;

    public static void setConfusedIcon(TextureRegion region){
        confusedIcon = region;
    }

    private static final int FRAME_SIZE = 32;
    private static final float WALK_FRAME_TIME = 0.12f;
    private static final float RUN_FRAME_TIME = 0.08f;

    private static final int ROW_FRONT = 0;
    private static final int ROW_BACK = 1;
    private static final int ROW_SIDE = 2;
    //------------
    // END CONSTANTS
    //------------

    private final GooseAI gooseAI = new GooseAI(this);
    private final Player player;

    private boolean honk;
    private float trappedTimer = 0f;

    private final Animation<TextureRegion>[] walkAnimations;
    private final Animation<TextureRegion>[] runAnimations;

    private float stateTime = 0f;

    public Goose(Player player, Vector2 position, Assets assets) {
        walkAnimations = Utilities.createAnimations(assets.getTexture(Assets.GOOSE_WALK), WALK_FRAME_TIME, FRAME_SIZE);
        runAnimations = Utilities.createAnimations(assets.getTexture(Assets.GOOSE_RUN), RUN_FRAME_TIME, FRAME_SIZE);

        this.player = player;
        this.position.set(position);
    }

    @Override
    public void update(float deltaTime) {
        if (trappedTimer > 0f) {
            trappedTimer -= deltaTime;
            // A goose that has been trapped between other geese isn't updating, so don t let the pushes pile up
            gooseAI.clearSeparation();
            return;
        }

        gooseAI.update(deltaTime);


        if (gooseAI.isMoving()) {
            stateTime += deltaTime;
        } else {
            stateTime = 0f;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Vector2 facing = gooseAI.getFacingDirection();
        boolean flip = false;
        int row;

        if (Math.abs(facing.x) > Math.abs(facing.y)) {
            row = ROW_SIDE;
            flip = facing.x < 0f;
        } else if (facing.y > 0f) {
            row = ROW_BACK;
        } else {
            row = ROW_FRONT;
        }

        Animation<TextureRegion>[] animations = gooseAI.isChasing() ? runAnimations : walkAnimations;
        TextureRegion frame = animations[row].getKeyFrame(stateTime);

        if (flip) {
            batch.draw(frame, position.x + FRAME_SIZE, position.y, -FRAME_SIZE, FRAME_SIZE);
        } else {
            batch.draw(frame, position.x, position.y, FRAME_SIZE, FRAME_SIZE);
        }
        renderConfusedIcon(batch);
    }

    // The "?" fades in and out above the goose's head
    private void renderConfusedIcon(SpriteBatch batch){
        if (confusedIcon == null || !(gooseAI.isConfused())){
            return;
        }

        float elapsed = gooseAI.getConfusedElapsed();
        float pop = Math.min(1f, elapsed / ICON_POP_SECONDS);
        float alpha = Math.min(1f, gooseAI.getConfusedRemaining() / ICON_FADE_SECONDS);
        float w = confusedIcon.getRegionWidth() * pop * ICON_SCALE;
        float h = confusedIcon.getRegionHeight() * pop * ICON_SCALE;
        float bob = (float) Math.sin(elapsed * 6f) * ICON_BOB;
        batch.setColor(1f, 1f, 1f, alpha);
        batch.draw(confusedIcon, position.x - 5f + FRAME_SIZE / 2f - w / 2f, position.y + FRAME_SIZE + ICON_GAP + bob, w, h);
        batch.setColor(1f, 1f, 1f, 1f);
    }

    @Override
    //GameWorld calls this for every pair of entities each frame
    public void interact(Entity other) {
        if (other instanceof Goose otherGoose) {
            gooseAI.addSeparationFrom(otherGoose);
        }
    }

    public void distractBy(Bread bread) {
        gooseAI.distractBy(bread);
    }

    // True if standing on honk ring on piece of bread
    public boolean isSettled() {
        return gooseAI.isSettled();
    }

    // True whilst this goose is standing on the honk ring
    public boolean isSwarming(){
        return gooseAI.isSwarming();
    }

    public void renderDebug(ShapeRenderer shapes) {
        gooseAI.renderDebug(shapes);
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public float getWidth() {
        return FRAME_SIZE;
    }

    @Override
    public float getHeight() {
        return FRAME_SIZE;
    }

    public void trap(float duration) {
        trappedTimer = Math.max(trappedTimer, duration);
    }

    public boolean isTrapped() {
        return trappedTimer > 0f;
    }
}
