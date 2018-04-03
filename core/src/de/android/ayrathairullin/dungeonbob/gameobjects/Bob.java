package de.android.ayrathairullin.dungeonbob.gameobjects;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import de.android.ayrathairullin.dungeonbob.GameConstants;
import de.android.ayrathairullin.dungeonbob.utils.MapUtils;

public class Bob {
    private static final float MAX_VELOCITY = .1f;
    private static final int ANIMATION_FRAME_SIZE = 8;
    private static final float ANIMATION_TIME_PERIOD = .08f;
    public static final float BOB_RESIZE_FACTOR = 700;
    private static final float DAMPING = .03f;
    private static final Vector2 gravity = new Vector2(0, - .02f);
    private static final float JUMP_VELOCITY = .35f;

    enum Direction {LEFT, RIGHT}
    Direction direction = Direction.RIGHT;

    public Sprite bobSprite;
    boolean isLeftPressed;
    boolean isRightPressed;
    boolean isLeftPaddleTouched;
    boolean isRightPaddleTouched;

    Animation walkAnimation;
    TextureRegion walkSheet;
    TextureRegion currentFrame;
    float stateTime;
    boolean updateAnimationStateTime = false;

    Vector2 velocity;
    Rectangle bobRectangle;

    public void initialize(float width, float height, TextureRegion walkSheet) {
        this.walkSheet = walkSheet;
        TextureRegion[][] temp = walkSheet.split(walkSheet.getRegionWidth() / ANIMATION_FRAME_SIZE,
                walkSheet.getRegionHeight());
        TextureRegion[] walkFrames = temp[0];
        walkAnimation = new Animation(ANIMATION_TIME_PERIOD, walkFrames);

        bobSprite = new Sprite();
        bobSprite.setSize((walkSheet.getRegionWidth() /ANIMATION_FRAME_SIZE) * (width/BOB_RESIZE_FACTOR),
                walkSheet.getRegionHeight() * (width/BOB_RESIZE_FACTOR));
        bobSprite.setSize(bobSprite.getWidth() * GameConstants.UNIT_SCALE, bobSprite.getHeight() * GameConstants.UNIT_SCALE);
        setPosition(0, 2);

        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);
        currentFrame = (TextureRegion) walkAnimation.getKeyFrame(stateTime, true);

        velocity = new Vector2(0, 0);

        bobRectangle = new Rectangle();
    }

    public void render(SpriteBatch batch) {
        bobSprite.setRegion(currentFrame);
        if (direction == Direction.LEFT) {
            bobSprite.flip(true, false);
        }else {
            bobSprite.flip(false, false);
        }
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
            direction = Direction.LEFT;
            velocity.x = - MAX_VELOCITY;
        }else if (isRightPressed) {
            direction = Direction.RIGHT;
            velocity.x = MAX_VELOCITY;
        }

        if (isLeftPaddleTouched) {
            direction = Direction.LEFT;
            velocity.x = - MAX_VELOCITY;
        }else if (isRightPaddleTouched) {
            direction = Direction.RIGHT;
            velocity.x = MAX_VELOCITY;
        }

        if (velocity.x < 0) {
            velocity.x += DAMPING;
        }else if (velocity.x > 0) {
            velocity.x -= DAMPING;
        }

        if (direction == Direction.RIGHT && velocity.x <= .02f) {
            velocity.x = 0;
        }else if (direction == Direction.LEFT && velocity.x >= -.02f) {
            velocity.x = 0;
        }

        if (velocity.x != 0) {
            updateAnimationStateTime = true;
        }
        velocity.add(gravity);
        checkWallHit();
        move(velocity.x, velocity.y);

        if(updateAnimationStateTime){
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

    public void checkWallHit() {
        bobRectangle.set(bobSprite.getX(), bobSprite.getY(), bobSprite.getWidth(), bobSprite.getHeight());
        int startX, startY, endX, endY;
        if (velocity.x > 0) {
            startX = endX = (int)(bobSprite.getX() + + bobSprite.getWidth() + velocity.x);
        }else {
            startX = endX = (int)(bobSprite.getX() + velocity.x);
        }
        startY = (int)bobSprite.getY();
        endY = (int)(bobSprite.getY() + bobSprite.getHeight());
        Array<Rectangle> tiles = MapUtils.getTiles(startX, startY, endX, endY, "Wall");
        for (Rectangle tile : tiles) {
            if (bobRectangle.overlaps(tile)) {
                velocity.x = 0;
                break;
            }
        }

        bobRectangle.x = bobSprite.getX();
        if (velocity.y > 0) {
            startY = endY = (int)(bobSprite.getY() + bobSprite.getHeight());
        }else {
            startY = endY = (int)(bobSprite.getY() + velocity.y);
        }
        startX = (int)bobSprite.getX();
        endX = (int)(bobSprite.getX() + bobSprite.getWidth());
        tiles = MapUtils.getTiles(startX, startY, endX, endY, "Wall");
        bobRectangle.y += velocity.y;
        for (Rectangle tile : tiles) {
            if (bobRectangle.overlaps(tile)) {
                if (velocity.y > 0) {
                    bobSprite.setY(tile.y - bobSprite.getHeight());
                }else if (velocity.y < 0) {
                    bobSprite.setY(tile.y + tile.height);
                }
                velocity.y = 0;
                break;
            }
        }
    }

    public void jump() {
        velocity.y = JUMP_VELOCITY;
    }
}
