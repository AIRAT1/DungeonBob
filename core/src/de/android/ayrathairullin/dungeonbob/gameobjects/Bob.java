package de.android.ayrathairullin.dungeonbob.gameobjects;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bob {
    public Sprite bobSprite;

    public void render(SpriteBatch batch) {
        bobSprite.draw(batch);
    }

    public void setPosition(float x, float y) {
        bobSprite.setPosition(x, y);
    }
}
