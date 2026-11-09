package io.github.goldengoose.hidden_events.tall_grass;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import io.github.goldengoose.hidden_events.HiddenEvent;
import io.github.goldengoose.world.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Makes the player hide in tall grass (For how long?) - 3 seconds like req1 says?
 *
 * There is ONE tall grass event for the whole map, not one per patch. Instead of a
 * rectangular zone, "inside the event" means "the tile under the player's centre has
 * a tile in one of the map's tall grass layers". The map has three of them
 * ("Tall Grass", "Tall Grass (extra layer)", "Tall Grass (extra layer 1)"); the plan is
 * to mark each with a layer property tall_grass=true in Tiled and collect every layer
 * that has it, the same way the project already uses the ySort property.
 *
 * While the player is hidden:
 *   - the goose AI cannot see or hear the player (needs a hidden flag on Player + a check in GooseAI)
 *   - the grass is drawn semi-transparent (needs support in DepthRenderer, which draws the
 *     ySort layers; this class only says HOW transparent via getFadeAlpha())
 *
 * Decision still open for the team: does hiding last "while inside the grass" or for a
 * fixed window (FR_HIDE says 3 seconds)? The fields below are set up for the 3-second window.
 */

public class TallGrass extends HiddenEvent {

    private static final String GRASS_LAYER_PROPERTY = "tall_grass"; // layer property that marks a tall grass layer in Tiled
    private static final float TILE_SIZE = 32f;                      // map tiles are 32x32 world units
    private static final float HIDE_DURATION_SECONDS = 3f;           // FR_HIDE: how long the player stays undetectable
    private static final float COOLDOWN_SECONDS = 5f;                // time before hiding can start again (placeholder, team decision)
    private static final float FADED_ALPHA = 0.5f;                   // grass opacity while the player is hidden (1 = solid)

    private final List<TiledMapTileLayer> grassLayers = new ArrayList<>(); // every layer with tall_grass=true

    private boolean hiding = false;     // true while the player is undetectable
    private float hideTimer = 0f;       // seconds of hiding left


    public TallGrass(TiledMap map) {
        super("Tall grass",
            "You are hidden in the tall grass!",
            0, 0,
            mapWidthInWorldUnits(map), mapHeightInWorldUnits(map),
            COOLDOWN_SECONDS);
        collectGrassLayers(map);
    }


    private static float mapWidthInWorldUnits(TiledMap map) {
        return map.getProperties().get("width", Integer.class) * TILE_SIZE;
    }

    private static float mapHeightInWorldUnits(TiledMap map) {
        return map.getProperties().get("height", Integer.class) * TILE_SIZE;
    }

    private void collectGrassLayers(TiledMap map) {
        // TODO: loop over map.getLayers(); for each layer that is a TiledMapTileLayer and has
        //       the property GRASS_LAYER_PROPERTY set to true, add it to grassLayers.
    }


    // Replace hiddenevents trigger zone with one that copies the grass sprite
    @Override
    public boolean contains(Entity other) {
        // TODO: get the entity's centre (other.getCentre(...)) and ask isGrassAt(x, y).
        return false;
    }

    private boolean isGrassAt(float worldX, float worldY) {
        // TODO: convert world position to tile coordinates: tileX = floor(worldX / TILE_SIZE),
        //       tileY = floor(worldY / TILE_SIZE). libGDX tile layers already have y = 0 at the BOTTOM,
        //       so no flip is needed (Tiled's own editor numbers rows from the top).
        // TODO: return true if getCell(tileX, tileY) is non-null in ANY layer in grassLayers.
        return false;
    }


    @Override
    protected void onEnter(Entity entity) {
        // TODO: call trigger() (counts the event, shows the message, respects the cooldown).
        // TODO: if it returned true, start hiding: hiding = true; hideTimer = HIDE_DURATION_SECONDS;
        //       and mark the player as hidden (new method on Player, e.g. setHidden(true)).
    }

    @Override
    protected void onExit(Entity entity) {
        // TODO: only needed for the "hidden while inside" version. For the fixed 3-second window
        //       the timer ends hiding, so this can stay empty.
    }


    @Override
    protected void onUpdate(float deltaTime) {
        // TODO: while hiding, count hideTimer down by deltaTime.
        // TODO: when it reaches 0: hiding = false and tell the player it is no longer hidden.
    }

    public boolean isHiding() {
        return hiding;
    }

    // How transparent DepthRenderer should draw the grass layer right now
    public float getFadeAlpha() {
        return hiding ? FADED_ALPHA : 1f;
    }

    @Override
    public void renderDebug(ShapeRenderer shapes) {
        // TODO: Draw trigger box for each grass tile
    }
}
