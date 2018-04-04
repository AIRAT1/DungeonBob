package de.android.ayrathairullin.dungeonbob.utils;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import java.util.Iterator;

import de.android.ayrathairullin.dungeonbob.GameConstants;
import de.android.ayrathairullin.dungeonbob.gameobjects.Enemy;
import de.android.ayrathairullin.dungeonbob.gameobjects.Zombie;

public class MapUtils {
    public static TiledMap map;

    public static void initialize(TiledMap map) {
        MapUtils.map = map;
    }

    // create a pool of rectangle objects for collision detection
    private static Pool<Rectangle> rectPool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject() {
            return new Rectangle();
        }
    };
    // denotes a list of tiles which are likely to collide with the player
    private static Array<Rectangle> tiles = new Array<Rectangle>();


    public static Array<Rectangle> getTiles(int startX, int startY, int endX, int endY, String layerName) {

        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerName);
        // return the rectangle objects to the pool from previous frame
        rectPool.freeAll(tiles);

        tiles.clear();

        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {

                Cell cell = layer.getCell(x, y);
                //if cell is present at a particular location in the map,
                if (cell != null) {
                    //add a rectangle object representing its position and dimensions to the tiles list
                    Rectangle rect = rectPool.obtain();
                    rect.set(x, y, 1, 1);
                    tiles.add(rect);
                }
            }
        }
        return tiles;
    }

    /**
     * this method returns the tiles which are near to a sprite horizontally
     */
    public static Array<Rectangle> getHorizNeighbourTiles(Vector2 velocity, Sprite sprite, String layerName) {
        int startX, startY, endX, endY;
        //if the sprite is moving right, get the tiles to its right side
        if (velocity.x > 0) {
            startX = endX = (int) (sprite.getX() + sprite.getWidth() + velocity.x);
        }
        //if the sprite is moving left, get the tiles to its left side
        else {
            startX = endX = (int) (sprite.getX() + velocity.x);
        }
        startY = (int) (sprite.getY());
        endY = (int) (sprite.getY() + sprite.getHeight());

        // get the tiles
        return getTiles(startX, startY, endX, endY, layerName);
    }

    /**
     * this method returns the tiles which are near to a sprite vertically
     */
    public static Array<Rectangle> getVertNeighbourTiles(Vector2 velocity, Sprite sprite, String layerName) {
        int startX, startY, endX, endY;
        //if sprite is moving up, get the tiles above it
        if (velocity.y > 0) {
            startY = endY = (int) (sprite.getY() + sprite.getHeight());

        }
        // if sprite is moving down, get the tiles below it
        else {
            startY = endY = (int) (sprite.getY() + velocity.y);
        }
        startX = (int) (sprite.getX());
        endX = (int) (sprite.getX() + sprite.getWidth());
        // get the tiles
        return getTiles(startX, startY, endX, endY, layerName);
    }

    public static void spawnEnemies(Array<Enemy> enemies, float width, float height, TextureAtlas texturePack) {

        Iterator<MapObject> mapObjectIterator = map.getLayers().get("Enemies").getObjects().iterator();

        float unitScale = GameConstants.UNIT_SCALE;
        while (mapObjectIterator.hasNext()) {
            // get the map object from iterator
            MapObject mapObject = mapObjectIterator.next();

            // if the name of the object is "zombie" as we have given
            if (mapObject.getName().equals("zombie")) {
                if (mapObject instanceof RectangleMapObject) {
                    Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
                    //create a zombie object and place it in that location
                    Zombie zombie = new Zombie(width, height, texturePack.findRegion(GameConstants.ZOMBIE_SPRITE_SHEET),
                            rectangle.x * unitScale, rectangle.y * unitScale);
                    enemies.add(zombie);
                }else {
                    Gdx.app.log("LOG", "No Rectangle"); // TODO ERROR HERE - NO RECTANGLE OBJECTS DETECTED
                }

//                Zombie zombie = new Zombie(width, height, texturePack.findRegion(GameConstants.ZOMBIE_SPRITE_SHEET),
//                       330 * unitScale, 50 * unitScale);
//                enemies.add(zombie);
            }
        }
    }
}
