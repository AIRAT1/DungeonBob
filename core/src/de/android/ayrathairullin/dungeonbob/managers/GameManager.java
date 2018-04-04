package de.android.ayrathairullin.dungeonbob.managers;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

import de.android.ayrathairullin.dungeonbob.GameConstants;
import de.android.ayrathairullin.dungeonbob.GameScreen;
import de.android.ayrathairullin.dungeonbob.gameobjects.Bob;
import de.android.ayrathairullin.dungeonbob.gameobjects.Enemy;
import de.android.ayrathairullin.dungeonbob.utils.MapUtils;

public class GameManager {
    static Bob bob  ; // bob instance
    static TextureRegion  bobSpriteSheet; // texture spriteSheet for the bob
    public static Sprite backgroundSprite  ; // background sprite
    public static Texture backgroundTexture; // texture image for the background

    public static final float PADDLE_RESIZE_FACTOR = 700f;
    public static final float PADDLE_ALPHA = 0.25f;
    public static final float PADDLE_HORIZ_POSITION_FACTOR = 0.02f;
    public static final float PADDLE_VERT_POSITION_FACTOR = 0.01f;

    static TextureRegion leftPaddleTexture;
    static TextureRegion rightPaddleTexture;
    static Sprite leftPaddleSprite;
    static Sprite rightPaddleSprite;
    static TextureRegion jumpButtonTexture;
    static Sprite jumpButtonSprite;


    static AssetManager assetManager;
    static TextureAtlas texturePack ; // packed texture.

    public static TiledMap map;
    public static OrthogonalTiledMapRenderer renderer; // map renderer

    static ShapeRenderer shapeRenderer; // for drawing shapes

    static MapObjects mapObjects;

    public static int mapWidth;
    public static int mapHeight;

    static BitmapFont font;
    public static Array<Enemy> enemies; // enemies list

    public static void initialize(float width,float height){

        assetManager = new AssetManager();
        loadAssets();
        font = assetManager.get(GameConstants.FONT_PATH);

        // get the map instance loaded
        map = assetManager.get(GameConstants.LEVEL1);
        setMapDimensions();

        TiledMapTileLayer tiledLayer = (TiledMapTileLayer)map.getLayers().get(0);

		/*MapLayer objectLayer = map.getLayers().get("Object Layer");
		mapObjects = objectLayer.getObjects();*/

        renderer = new OrthogonalTiledMapRenderer(map, GameConstants.UNIT_SCALE);

        GameScreen.camera.setToOrtho(false, mapWidth,mapHeight);
        GameScreen.camera.update();
        // set the renderer's view to the game's main camera
        renderer.setView(GameScreen.camera);

        texturePack = assetManager.get(GameConstants.TEXTURE_PACK); // get the packed texture from asset manager

        // instantiate the bob
        bob = new Bob();
        // load the bob sprite sheet from the packed image
        bobSpriteSheet = texturePack.findRegion(GameConstants.BOB_SPRITE_SHEET);
        // initialize Bob
        bob.initialize(width,height,bobSpriteSheet);


        //load background texture
        backgroundTexture = assetManager.get(GameConstants.BACKGROUND_IMAGE);
        //set background sprite with the texture
        backgroundSprite= new Sprite(backgroundTexture);
        // set the background to completely fill the screen
        backgroundSprite.setSize(width, height);

        initializeLeftPaddle(width,height);
        initializeRightPaddle(width,height);
        initializeJumpButton(width, height);

        MapUtils.initialize(map);
        TextManager.initialize(width, height, font);

        // instantaite and initialize zombies
        enemies = new Array<Enemy>();
        MapUtils.spawnEnemies(enemies,width,height,texturePack);
		/*shapeRenderer = new ShapeRenderer();
		drawShapes();*/
    }

    public static void renderBackground(SpriteBatch batch){
        // draw the background

        backgroundSprite.draw(batch);
    }


    public static void renderGame(SpriteBatch batch){
        //draw the Bob with respect to main cam
        batch.setProjectionMatrix(GameScreen.camera.combined);

        bob.update();
        // Render(draw) the bob
        bob.render(batch);
        for(Enemy enemy :enemies){
            enemy.update();
            enemy.render(batch);
        }

        //update the camera's x position to Bob's x position
        GameScreen.camera.position.x= bob.bobSprite.getX();
        //if the viewport goes outside the map's dimensions update the camera's position correctly
        if(!((GameScreen.camera.position.x-(GameScreen.camera.viewportWidth/2))>0)){
            GameScreen.camera.position.x = GameScreen.camera.viewportWidth/2;
        }
        else if(((GameScreen.camera.position.x+(GameScreen.camera.viewportWidth/2))>=mapWidth)){
            GameScreen.camera.position.x = mapWidth - GameScreen.camera.viewportWidth/2;
        }

        renderer.setView(GameScreen.camera);
        GameScreen.camera.update();

        //draw the paddles with respect to hud cam
        batch.setProjectionMatrix(GameScreen.hudCamera.combined);

        leftPaddleSprite.draw(batch);
        rightPaddleSprite.draw(batch);
        jumpButtonSprite.draw(batch);
        TextManager.displayMessage(batch);

    }
    public  static void dispose() {
        assetManager.clear();
    }

    public static void initializeLeftPaddle(float width,float height){
        //load background texture
        leftPaddleTexture = texturePack.findRegion(GameConstants.LEFT_PADDLE_IMAGE);
        //set left paddle sprite with the texture
        leftPaddleSprite= new Sprite(leftPaddleTexture);
        // resize the sprite
        leftPaddleSprite.setSize(leftPaddleSprite.getWidth()*width/ PADDLE_RESIZE_FACTOR, leftPaddleSprite.getHeight()*width/ PADDLE_RESIZE_FACTOR);
        // set the position to bottom left corner with offset
        leftPaddleSprite.setPosition(width* PADDLE_HORIZ_POSITION_FACTOR, height* PADDLE_VERT_POSITION_FACTOR);
        // make the paddle semi transparent
        leftPaddleSprite.setAlpha(PADDLE_ALPHA);
    }


    public static void initializeRightPaddle(float width,float height){
        //load background texture
        rightPaddleTexture = texturePack.findRegion(GameConstants.RIGHT_PADDLE_IMAGE);
        //set right paddle sprite with the texture
        rightPaddleSprite= new Sprite(rightPaddleTexture);
        // resize the sprite
        rightPaddleSprite.setSize(rightPaddleSprite.getWidth()*width/ PADDLE_RESIZE_FACTOR, rightPaddleSprite.getHeight()*width/ PADDLE_RESIZE_FACTOR);
        // set the position to bottom left corner with offset
        rightPaddleSprite.setPosition(leftPaddleSprite.getX()+ leftPaddleSprite.getWidth()+ width*PADDLE_HORIZ_POSITION_FACTOR, height*PADDLE_VERT_POSITION_FACTOR);
        // make the paddle semi transparent
        rightPaddleSprite.setAlpha(PADDLE_ALPHA);
    }

    public static void initializeJumpButton(float width,float height){
        //load jump button texture region
        jumpButtonTexture =  texturePack.findRegion(GameConstants.JUMP_IMAGE);
        //set jump button sprite with the texture
        jumpButtonSprite= new Sprite(jumpButtonTexture);
        // resize the sprite
        jumpButtonSprite.setSize(jumpButtonSprite.getWidth()*width/PADDLE_RESIZE_FACTOR, jumpButtonSprite.getHeight()*width/PADDLE_RESIZE_FACTOR);
        // set the position to bottom right corner with offset
        jumpButtonSprite.setPosition(width*0.9f, height*0.01f);
        // make the button semi transparent
        jumpButtonSprite.setAlpha(0.25f);
    }

    public static void loadAssets(){
        // queue the assets for loading
        assetManager.load(GameConstants.BACKGROUND_IMAGE, Texture.class);
        assetManager.load(GameConstants.TEXTURE_PACK,  TextureAtlas.class);
        assetManager.load(GameConstants.FONT_PATH,BitmapFont.class);
        // set the tiled map loader for the assetmanager
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        //load the tiled map
        assetManager.load(GameConstants.LEVEL1, TiledMap.class);
        //blocking method to load all assets
        assetManager.finishLoading();
    }

    static void drawShapes(){
        GameScreen.camera.update();

        shapeRenderer.setProjectionMatrix(GameScreen.camera.combined.scl(GameConstants.UNIT_SCALE));

        // set the shape as an outline
        shapeRenderer.begin(ShapeType.Line);
        //set the shape's color as blue
        shapeRenderer.setColor(0, 1, 1, 1);

        Iterator<MapObject> mapObjectIterator = mapObjects.iterator();

        while(mapObjectIterator.hasNext()){
            // get the map object from iterator
            MapObject mapObject = mapObjectIterator.next();

            // check if it is a rectangle
            if(mapObject instanceof RectangleMapObject){
                Rectangle rectangle = ((RectangleMapObject)mapObject).getRectangle();
                //draw rectangle shape on the screen
                shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            }

            // check if it is a polygon
            else if(mapObject instanceof PolygonMapObject){
                Polygon polygon = ((PolygonMapObject)mapObject).getPolygon();

                shapeRenderer.polygon(polygon.getTransformedVertices());
            }

            //check if it an ellipse
            else if(mapObject instanceof EllipseMapObject){
                Ellipse ellipse = ((EllipseMapObject)mapObject).getEllipse();

                shapeRenderer.ellipse(ellipse.x, ellipse.y, ellipse.width,ellipse.height);
            }

            // check if it is a polyline
            else if(mapObject instanceof PolylineMapObject){
                Polyline polyline = ((PolylineMapObject)mapObject).getPolyline();

                shapeRenderer.polyline(polyline.getTransformedVertices());
            }
        }


        shapeRenderer.end();
    }

    static void setMapDimensions(){
        MapProperties properties = map.getProperties();
        mapHeight= Integer.parseInt(properties.get("height").toString());
        mapWidth= Integer.parseInt(properties.get("width").toString());
    }
}
