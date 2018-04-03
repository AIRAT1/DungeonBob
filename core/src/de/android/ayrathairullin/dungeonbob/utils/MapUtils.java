package de.android.ayrathairullin.dungeonbob.utils;


import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class MapUtils {
    public static TiledMap map;
    private static Pool<Rectangle> rectPool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject() {
            return new Rectangle();
        }
    };
    private static Array<Rectangle> tiles = new Array<Rectangle>();

    public static void initialize(TiledMap map) {
        MapUtils.map = map;
    }

    public static Array<Rectangle> getTiles(int startX, int startY, int endX, int endY, String layerName) {
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(layerName);
        rectPool.freeAll(tiles);
        tiles.clear();
        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                Cell cell = layer.getCell(x, y);
                if (cell != null) {
                    Rectangle rect = rectPool.obtain();
                    rect.set(x, y, 1, 1);
                    tiles.add(rect);
                }
            }
        }
        return tiles;
    }
}
