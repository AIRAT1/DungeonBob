package de.android.ayrathairullin.dungeonbob.gameobjects;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import de.android.ayrathairullin.dungeonbob.GameConstants;
import de.android.ayrathairullin.dungeonbob.utils.MapUtils;

public class Zombie extends Enemy{
    private static final float RESIZE_FACTOR = 900f;
    private static final float ZOMBIE_VELOCITY = 0.04f;
    private static int ANIMATION_FRAME_SIZE=3;
    enum Direction{LEFT,RIGHT};
    Direction direction = Direction.LEFT; //denotes zombie's direction
    Animation     walkAnimation;          // animation instance
    TextureRegion walkSheet;        // sprite sheet
    TextureRegion currentFrame;           // current animation frame
    float stateTime;


    @Override
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    @Override
    public void update() {
        // set the rectangle with zombie's dimensions for collisions
        rectangle.set(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        checkWallHit();

        // change the direction based on velocity
        if (velocity.x < 0) {
            direction = Direction.LEFT;
        } else {
            direction = Direction.RIGHT;
        }

        sprite.setX(sprite.getX()+velocity.x);
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = (TextureRegion) walkAnimation.getKeyFrame(stateTime, true);

        sprite.setRegion(currentFrame); // set the zombie sprite's texture to the current frame

        if(direction==Direction.RIGHT){
            sprite.setFlip(true, false);
        }
        else {
            sprite.setFlip(false, false);
        }

    }

    public Zombie(float width,float height,TextureRegion zombieSheet,float x,float y){

        sprite = new Sprite();
        sprite.setPosition(x,y);
        velocity = new Vector2(ZOMBIE_VELOCITY, 0);
        rectangle = new Rectangle();
        this.walkSheet=zombieSheet; // save the sprite-sheet
        //split the sprite-sheet into different textures
        TextureRegion[][] tmp = walkSheet.split( walkSheet.getRegionWidth()/ANIMATION_FRAME_SIZE, walkSheet.getRegionHeight());
        // convert 2D array to 1D
        TextureRegion[] walkFrames = tmp[0];

        // create a new animation sequence with the walk frames and time period of 0.04 seconds
        walkAnimation = new Animation(0.25f, walkFrames);

        // set the animation to loop
        walkAnimation.setPlayMode(PlayMode.LOOP_PINGPONG);
        // get initial frame
        currentFrame = (TextureRegion) walkAnimation.getKeyFrame(stateTime, true);

        sprite.setSize(((walkSheet.getRegionWidth()/ANIMATION_FRAME_SIZE)*(width/RESIZE_FACTOR)),(walkSheet.getRegionHeight()*(width/RESIZE_FACTOR)));
        sprite.setSize(sprite.getWidth()* GameConstants.UNIT_SCALE,sprite.getHeight()*GameConstants.UNIT_SCALE);

    }

    public void checkWallHit(){
        // get the tiles from map utilities
        Array<Rectangle> tiles = MapUtils.getHorizNeighbourTiles(velocity, sprite, "Wall");
        //if zombie collides with any wall tile while walking right/left,reverse his horizontal motion
        for (Rectangle tile : tiles) {
            if (rectangle.overlaps(tile)) {
                velocity.x *=-1;
                break;
            }
        }
    }
}
