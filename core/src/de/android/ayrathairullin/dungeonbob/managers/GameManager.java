package de.android.ayrathairullin.dungeonbob.managers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.android.ayrathairullin.dungeonbob.gameobjects.Bob;

public class GameManager {
    public static final float BOB_RESIZE_FACTOR = 400;
    public static final float PADDLE_RESIZE_FACTOR = 700;
    public static final float PADDLE_ALPHA = .25f;
    public static final float PADDLE_HORIZ_POSITION_FACTOR = .02f;
    public static final float PADDLE_VERT_POSITION_FACTOR = .01f;

    static Bob bob;
    static Texture bobTexture;
    public static Sprite backgroundSprite;
    public static Texture backgroundTexture;

    static Texture leftPaddleTexture;
    static Texture rightPaddleTexture;
    static Sprite leftPaddleSprite;
    static Sprite rightPaddleSprite;

    public static void initialize(float width, float height) {
        bob = new Bob();
        bobTexture = new Texture(Gdx.files.internal("data/bob.png"));
        bob.bobSprite = new Sprite(bobTexture);
        bob.bobSprite.setSize(bob.bobSprite.getWidth() * (width / BOB_RESIZE_FACTOR),
                bob.bobSprite.getHeight() * (width / BOB_RESIZE_FACTOR));
        bob.setPosition(width / 2f, 0);

        backgroundTexture = new Texture(Gdx.files.internal("data/background.jpg"));
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize(width, height);

        initializeLeftPaddle(width, height);
        initializeRightPaddle(width, height);
    }

    public static void initializeLeftPaddle(float width, float height) {
        leftPaddleTexture = new Texture(Gdx.files.internal("data/paddleLeft.png"));
        leftPaddleSprite = new Sprite(leftPaddleTexture);
        leftPaddleSprite.setSize(leftPaddleSprite.getWidth() * (width / PADDLE_RESIZE_FACTOR),
                leftPaddleSprite.getHeight() * (width / PADDLE_RESIZE_FACTOR));
        leftPaddleSprite.setPosition(width * PADDLE_HORIZ_POSITION_FACTOR, height * PADDLE_VERT_POSITION_FACTOR);
        leftPaddleSprite.setAlpha(PADDLE_ALPHA);
    }

    public static void initializeRightPaddle(float width, float height) {
        rightPaddleTexture = new Texture(Gdx.files.internal("data/paddleRight.png"));
        rightPaddleSprite = new Sprite(rightPaddleTexture);
        rightPaddleSprite.setSize(rightPaddleSprite.getWidth() * (width / PADDLE_RESIZE_FACTOR),
                rightPaddleSprite.getHeight() * (width / PADDLE_RESIZE_FACTOR));
        rightPaddleSprite.setPosition(leftPaddleSprite.getX() + leftPaddleSprite.getWidth()
        + width * PADDLE_HORIZ_POSITION_FACTOR, height * PADDLE_VERT_POSITION_FACTOR);
        rightPaddleSprite.setAlpha(PADDLE_ALPHA);
    }

    public static void renderGame(SpriteBatch batch) {
        backgroundSprite.draw(batch);
        bob.update();
        bob.render(batch);
        leftPaddleSprite.draw(batch);
        rightPaddleSprite.draw(batch);
    }

    public static void dispose() {
        backgroundTexture.dispose();
        bobTexture.dispose();
        leftPaddleTexture.dispose();
        rightPaddleTexture.dispose();
    }
}
