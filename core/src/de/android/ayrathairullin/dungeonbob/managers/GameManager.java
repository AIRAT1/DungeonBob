package de.android.ayrathairullin.dungeonbob.managers;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;

import java.util.Iterator;

import de.android.ayrathairullin.dungeonbob.GameConstants;
import de.android.ayrathairullin.dungeonbob.GameScreen;
import de.android.ayrathairullin.dungeonbob.gameobjects.Bob;

public class GameManager {
    public static final float BOB_RESIZE_FACTOR = 400;
    public static final float PADDLE_RESIZE_FACTOR = 700;
    public static final float PADDLE_ALPHA = .25f;
    public static final float PADDLE_HORIZ_POSITION_FACTOR = .02f;
    public static final float PADDLE_VERT_POSITION_FACTOR = .01f;

    static Bob bob;
    static TextureRegion bobSpriteSheet;
    static TextureAtlas texturePack;
    public static Sprite backgroundSprite;
    public static Texture backgroundTexture;

    static TextureRegion leftPaddleTexture;
    static TextureRegion rightPaddleTexture;
    static Sprite leftPaddleSprite;
    static Sprite rightPaddleSprite;

    static AssetManager assetManager;
    static TiledMap map;
    public static OrthogonalTiledMapRenderer renderer;

    static MapObjects mapObjects;
    static ShapeRenderer shapeRenderer;

    public static int mapWidth, mapHeight;

    public static void initialize(float width, float height) {
        assetManager = new AssetManager();
        loadAssets();

        map = assetManager.get(GameConstants.LEVEL1);
        setMapDimensions();

//        Iterator<String> iterator = map.getProperties().getKeys();
//        while (iterator.hasNext()) {
//            String key = iterator.next();
//            System.out.println("Name: " + key + ", Value: " + map.getProperties().get(key));
//        }

//        TiledMapTileLayer tiledLayer = (TiledMapTileLayer)map.getLayers().get("Wall");
//        tiledLayer.setOpacity(.25f);
//        tiledLayer.setVisible(true);

//        tiledLayer.getCell(8, 2).setRotation(Cell.ROTATE_90);
//        tiledLayer.setCell(0, 0, null);

//        MapLayer objectLayer = map.getLayers().get("Objects");
//        mapObjects = objectLayer.getObjects();
//        shapeRenderer = new ShapeRenderer();

        renderer = new OrthogonalTiledMapRenderer(map, GameConstants.UNIT_SCALE);
//        GameScreen.camera.setToOrtho(false, 35, 20); // TODO 15, 13
        GameScreen.camera.setToOrtho(false, 15, 13); //
        GameScreen.camera.update();
        renderer.setView(GameScreen.camera);

        texturePack = assetManager.get(GameConstants.TEXTURE_PACK);
        bob = new Bob();
        bobSpriteSheet = texturePack.findRegion(GameConstants.BOB_SPRITE_SHEET);
        bob.initialize(width, height, bobSpriteSheet);

        backgroundTexture = assetManager.get(GameConstants.BACKGROUND_IMAGE);
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize(width, height);

        initializeLeftPaddle(width, height);
        initializeRightPaddle(width, height);
    }

    public static void initializeLeftPaddle(float width, float height) {
        leftPaddleTexture = texturePack.findRegion(GameConstants.LEFT_PADDLE_IMAGE);
        leftPaddleSprite = new Sprite(leftPaddleTexture);
        leftPaddleSprite.setSize(leftPaddleSprite.getWidth() * (width / PADDLE_RESIZE_FACTOR),
                leftPaddleSprite.getHeight() * (width / PADDLE_RESIZE_FACTOR));
        leftPaddleSprite.setPosition(width * PADDLE_HORIZ_POSITION_FACTOR, height * PADDLE_VERT_POSITION_FACTOR);
        leftPaddleSprite.setAlpha(PADDLE_ALPHA);
    }

    public static void initializeRightPaddle(float width, float height) {
        rightPaddleTexture = texturePack.findRegion(GameConstants.RIGHT_PADDLE_IMAGE);
        rightPaddleSprite = new Sprite(rightPaddleTexture);
        rightPaddleSprite.setSize(rightPaddleSprite.getWidth() * (width / PADDLE_RESIZE_FACTOR),
                rightPaddleSprite.getHeight() * (width / PADDLE_RESIZE_FACTOR));
        rightPaddleSprite.setPosition(leftPaddleSprite.getX() + leftPaddleSprite.getWidth()
        + width * PADDLE_HORIZ_POSITION_FACTOR, height * PADDLE_VERT_POSITION_FACTOR);
        rightPaddleSprite.setAlpha(PADDLE_ALPHA);
    }

    public static void renderGame(SpriteBatch batch) {
        batch.setProjectionMatrix(GameScreen.camera.combined);
        bob.update();
        bob.render(batch);
        GameScreen.camera.position.x = bob.bobSprite.getX();
        if (!((GameScreen.camera.position.x - GameScreen.camera.viewportWidth / 2) > 0)) {
            GameScreen.camera.position.x = GameScreen.camera.viewportWidth / 2;
        }else if (((GameScreen.camera.position.x + GameScreen.camera.viewportWidth / 2) >= mapWidth)) {
            GameScreen.camera.position.x = mapWidth - GameScreen.camera.viewportWidth / 2;
        }
        renderer.setView(GameScreen.camera);
        GameScreen.camera.update();

        batch.setProjectionMatrix(GameScreen.hudCamera.combined);
        leftPaddleSprite.draw(batch);
        rightPaddleSprite.draw(batch);
    }

    public static void dispose() {
//        assetManager.unload(GameConstants.BACKGROUND_IMAGE);
//        assetManager.unload(GameConstants.BOB_SPRITE_SHEET);
//        assetManager.unload(GameConstants.LEFT_PADDLE_IMAGE);
//        assetManager.unload(GameConstants.RIGHT_PADDLE_IMAGE);
        assetManager.clear();
    }

    public static void loadAssets() {
        assetManager.load(GameConstants.BACKGROUND_IMAGE, Texture.class);
        assetManager.load(GameConstants.TEXTURE_PACK, TextureAtlas.class);
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        assetManager.load(GameConstants.LEVEL1, TiledMap.class);
        assetManager.finishLoading();
    }

    public static void drawShapes() {
        GameScreen.camera.update();
        shapeRenderer.setProjectionMatrix(GameScreen.camera.combined.scl(GameConstants.UNIT_SCALE));
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 1, 1, 1);
        Iterator<MapObject> mapObjectIterator = mapObjects.iterator();
        while (mapObjectIterator.hasNext()) {
            MapObject mapObject = mapObjectIterator.next();
            if (mapObject instanceof RectangleMapObject) {
                Rectangle rectangle = ((RectangleMapObject)mapObject).getRectangle();
                shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            }else if (mapObject instanceof PolygonMapObject) {
                Polygon polygon = ((PolygonMapObject)mapObject).getPolygon();
                shapeRenderer.polygon(polygon.getTransformedVertices());
            }else if (mapObject instanceof EllipseMapObject) {
                Ellipse ellipse = ((EllipseMapObject)mapObject).getEllipse();
                shapeRenderer.ellipse(ellipse.x, ellipse.y, ellipse.width, ellipse.height);
            }else if (mapObject instanceof PolylineMapObject) {
                Polyline polyline = ((PolylineMapObject)mapObject).getPolyline();
                shapeRenderer.polyline(polyline.getTransformedVertices());
            }
        }
        shapeRenderer.end();
    }

    static void setMapDimensions() {
        MapProperties properties = map.getProperties();
        mapHeight = Integer.parseInt(properties.get("height").toString());
        mapWidth = Integer.parseInt(properties.get("height").toString());
    }

    public static void renderBackground(SpriteBatch batch) {
        backgroundSprite.draw(batch);
    }
}
