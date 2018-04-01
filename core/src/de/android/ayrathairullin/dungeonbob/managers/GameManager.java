package de.android.ayrathairullin.dungeonbob.managers;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

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

    public static void initialize(float width, float height) {
        assetManager = new AssetManager();
        loadAssets();

        map = assetManager.get(GameConstants.LEVEL1);
        renderer = new OrthogonalTiledMapRenderer(map, GameConstants.UNIT_SCALE);
        GameScreen.camera.setToOrtho(false, 35, 20);
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
//        backgroundSprite.draw(batch); // TODO uncomment for drawing
//        bob.update();
//        bob.render(batch);
//        leftPaddleSprite.draw(batch);
//        rightPaddleSprite.draw(batch);
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
}
