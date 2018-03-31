package de.android.ayrathairullin.dungeonbob.managers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.android.ayrathairullin.dungeonbob.gameobjects.Bob;

public class GameManager {
    public static final float BOB_RESIZE_FACTOR = 400;

    static Bob bob;
    static Texture bobTexture;
    public static Sprite backgroundSprite;
    public static Texture backgroundTexture;

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
    }

    public static void renderGame(SpriteBatch batch) {
        backgroundSprite.draw(batch);
        bob.render(batch);
    }

    public static void dispose() {
        backgroundTexture.dispose();
        bobTexture.dispose();
    }
}
