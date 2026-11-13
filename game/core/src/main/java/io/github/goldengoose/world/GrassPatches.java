package io.github.goldengoose.world;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.Deque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Works out which tall grass cells belong to a specific patch. Done so that transparency is specific to each patch
 * Using the floodfill algorithm
 * WORK IN PROGRESS.
 *
 *
public class GrassPatches {
    public static final int NONE = -1;
    private final String GRASS_LAYER_PROPERTY = "tall_grass";

    private final int height;
    private final int width;

    private final int[][] patchOf;
    private final int patchCount = 0;

    public GrassPatches(TiledMap map){
        width = map.getProperties().get("width", Integer.class);
        height = map.getProperties().get("height", Integer.class);

        boolean[][] grass = new boolean[width][height];
        List<TiledMapTileLayer> layers = new ArrayList<>();
        for (MapLayer layer : map.getLayers()){
            boolean marked = layer.getProperties().get(GRASS_LAYER_PROPERTY, false, Boolean.class)};
            if (marked && layers instanceof TiledMapTileLayer tileLayer){
                layers.add(tileLayer);
            }
        for (TiledMapTileLayer layer : layers){
            for ()
        }

    }

    public int GetPatchCount(){
        return patchCount;
    }

    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }
}

*/

