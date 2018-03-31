package de.android.ayrathairullin.dungeonbob.gameobjects;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bob {
    private static final float X_MOVE_UNITS = 3f;

    public Sprite bobSprite;
    boolean isLeftPressed;
    boolean isRightPressed;
    boolean isLeftPaddleTouched;
    boolean isRightPaddleTouched;

    public void render(SpriteBatch batch) {
        bobSprite.draw(batch);
    }

    public void setPosition(float x, float y) {
        bobSprite.setPosition(x, y);
    }

    public void move(float x, float y) {
        setPosition(bobSprite.getX() + x, bobSprite.getY() + y);
    }

    public void setLeftPressed(boolean isPressed) {
        if (isRightPressed && isPressed) {
            isRightPressed = false;
        }
        isLeftPressed = isPressed;
    }

    public void setRightPressed(boolean isPressed) {
        if (isLeftPressed && isPressed) {
            isLeftPressed = false;
        }
        isRightPressed = isPressed;
    }

    public void update() {
        if (isLeftPressed) {
            move(- X_MOVE_UNITS, 0);
        }else if (isRightPressed) {
            move(X_MOVE_UNITS, 0);
        }
    }

    public void setLeftPaddleTouched(boolean isTouched) {
        if (isRightPaddleTouched && isTouched) {
            isRightPaddleTouched = false;
        }
        isLeftPaddleTouched = isTouched;
    }

    public void setRightPaddleTouched(boolean isTouched) {
        if (isLeftPaddleTouched && isTouched) {
            isLeftPaddleTouched = false;
        }
        isRightPaddleTouched = isTouched;
    }
}
