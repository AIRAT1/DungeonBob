package de.android.ayrathairullin.dungeonbob.gameobjects;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Bob {
    private static final float X_MOVE_UNITS = 3f;
    private static final int ANIMATION_FRAME_SIZE = 8;
    private static final float ANIMATION_TIME_PERIOD = .08f;
    public static final float BOB_RESIZE_FACTOR = 400;

    public Sprite bobSprite;
    boolean isLeftPressed;
    boolean isRightPressed;
    boolean isLeftPaddleTouched;
    boolean isRightPaddleTouched;

    Animation walkAnimation;
    Texture walkSheet;
    TextureRegion currentFrame;
    float stateTime;
    boolean updateAnimationStateTime = false;

    public void initialize(float width, float height, Texture walkSheet) {
        this.walkSheet = walkSheet;
        TextureRegion[][] temp = TextureRegion.split(walkSheet, walkSheet.getWidth() / ANIMATION_FRAME_SIZE,
                walkSheet.getHeight());
        TextureRegion[] walkFrames = temp[0];
        walkAnimation = new Animation(ANIMATION_TIME_PERIOD, walkFrames);

        bobSprite = new Sprite();
        bobSprite.setSize((walkSheet.getWidth()/ANIMATION_FRAME_SIZE) * (width/BOB_RESIZE_FACTOR),
                walkSheet.getHeight() * (width/BOB_RESIZE_FACTOR));
        setPosition(width / 2f, 0);

        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);
        currentFrame = (TextureRegion) walkAnimation.getKeyFrame(stateTime, true);
    }

    public void render(SpriteBatch batch) {
        bobSprite.setRegion(currentFrame);
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
        updateAnimationStateTime = false;

        if (isLeftPressed) {
            updateAnimationStateTime = true;
            move(- X_MOVE_UNITS, 0);
        }else if (isRightPressed) {
            updateAnimationStateTime = true;
            move(X_MOVE_UNITS, 0);
        }

        if (isLeftPaddleTouched) {
            updateAnimationStateTime = true;
            move(- X_MOVE_UNITS, 0);
        }else if (isRightPaddleTouched) {
            updateAnimationStateTime = true;
            move(X_MOVE_UNITS, 0);
        }

        if (updateAnimationStateTime) {
            stateTime += Gdx.graphics.getDeltaTime();
            currentFrame = (TextureRegion) walkAnimation.getKeyFrame(stateTime, true);
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
