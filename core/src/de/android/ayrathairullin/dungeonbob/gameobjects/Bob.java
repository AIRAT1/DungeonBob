package de.android.ayrathairullin.dungeonbob.gameobjects;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import de.android.ayrathairullin.dungeonbob.GameConstants;
import de.android.ayrathairullin.dungeonbob.GameData;
import de.android.ayrathairullin.dungeonbob.managers.GameManager;
import de.android.ayrathairullin.dungeonbob.utils.MapUtils;

public class Bob {
    public Sprite bobSprite; //sprite to display Bob
    boolean isLeftPressed; // indicates if left key is pressed
    boolean isRightPressed; // indicates if right key is pressed
    boolean isLeftPaddleTouched; // indicates if left paddle is touched
    boolean isRightPaddleTouched; // indicates if right paddle is touched

    Animation     walkAnimation;          // animation instance
    TextureRegion        walkSheet;                     // sprite sheet
    TextureRegion currentFrame;           // current animation frame
    float  stateTime;                                 // elapsed time
    boolean updateAnimationStateTime =false; // keep track of when to update Bob state time
    Rectangle bobRectangle; // represents collision box around Bob
    boolean isGrounded = false; // denotes whether the player is on the ground

    private static int ANIMATION_FRAME_SIZE=8; // this specifies the number of frames(images) that we are using for animation
    private static float ANIMATION_TIME_PERIOD=0.08f;// this specifies the time between two consecutive frames of animation
    public static final float BOB_RESIZE_FACTOR = 700f;

    enum Direction{LEFT,RIGHT};
    Direction direction = Direction.RIGHT; //denotes player's direction. defaulted to right

    Vector2 velocity; // Bob's velocity
    private static final float damping= 0.03f;
    private static final float maxVelocity = 0.1f; // units bob will move in x direction
    private static final Vector2 gravity = new Vector2(0, -0.02f);
    private static final float jumpVelocity = 0.35f;

    public void initialize(float width,float height,TextureRegion walkSheet){

        velocity = new Vector2(0, 0);

        this.walkSheet=walkSheet; // save the sprite-sheet
        //split the sprite-sheet into different textures
        TextureRegion[][] tmp = walkSheet.split( walkSheet.getRegionWidth()/ANIMATION_FRAME_SIZE, walkSheet.getRegionHeight());
        // convert 2D array to 1D
        TextureRegion[] walkFrames = tmp[0];
        // create a new animation sequence with the walk frames and time period of specified seconds
        walkAnimation = new Animation(ANIMATION_TIME_PERIOD, walkFrames);

        // instantiate bob sprite
        bobSprite = new Sprite();
        //set the size of the bob
        bobSprite.setSize((walkSheet.getRegionWidth()/ANIMATION_FRAME_SIZE)*(width/BOB_RESIZE_FACTOR),walkSheet.getRegionHeight()*(width/BOB_RESIZE_FACTOR));

        bobSprite.setSize(bobSprite.getWidth()*GameConstants.UNIT_SCALE,bobSprite.getHeight()*GameConstants.UNIT_SCALE);
        // set the position of the bob to bottom - left
        setPosition(GameConstants.SPAWN_POINT.x, GameConstants.SPAWN_POINT.y);

        bobRectangle = new Rectangle();

        // set the animation to loop
        walkAnimation.setPlayMode(PlayMode.LOOP);
        // get initial frame
        currentFrame = (TextureRegion) walkAnimation.getKeyFrame(stateTime, true);

    }



    public void render(SpriteBatch batch){
        bobSprite.setRegion(currentFrame); // set the bob sprite's texture to the current frame
        if(direction==Direction.LEFT){
            bobSprite.setFlip(true, false);
        }
        else{
            bobSprite.setFlip(false, false);
        }
        bobSprite.draw(batch);
    }


    public void setPosition(float x,float y){
        bobSprite.setPosition(x, y);
    }

    // move bob's with the specified amount
    public void move (float x, float y){
        setPosition(bobSprite.getX()+x,bobSprite.getY()+y);
    }

    public void setLeftPressed(boolean isPressed)
    {
        // to have motion in only one direction if both are pressed
        if(isRightPressed && isPressed){
            isRightPressed = false;
        }

        isLeftPressed = isPressed;
    }
    public void setRightPressed(boolean isPressed)
    {
        // to have motion in only one direction if both are pressed
        if(isLeftPressed && isPressed){
            isLeftPressed = false;
        }
        isRightPressed = isPressed;
    }

    public void setLeftPaddleTouched(boolean isTouched)
    {
        // to restrict motion in only one direction if both are touched
        if(isRightPaddleTouched && isTouched){
            isRightPaddleTouched = false;
        }

        isLeftPaddleTouched = isTouched;
    }
    public void setRightPaddleTouched(boolean isTouched)
    {
        // to restrict motion if both are touched
        if(isLeftPaddleTouched && isTouched){
            isLeftPaddleTouched = false;
        }
        isRightPaddleTouched = isTouched;
    }


    public void update(){

        updateAnimationStateTime=false;

        // move specified units to left if left key is pressed
        if (isLeftPressed){
            direction=Direction.LEFT;
            velocity.x=-maxVelocity;
        }

        // move specified units to right if right key is pressed
        else if (isRightPressed){
            direction=Direction.RIGHT;
            velocity.x=maxVelocity;
        }

        // move specified units to left if left paddle is touched
        if (isLeftPaddleTouched){
            direction=Direction.LEFT;
            velocity.x=-maxVelocity;
        }

        // move specified units to right if right paddle is touched
        else if (isRightPaddleTouched){
            direction=Direction.RIGHT;
            velocity.x=maxVelocity;
        }

        //reduce bob's velocity if he is not at rest by damping factor
        if(velocity.x<0){
            velocity.x+=damping;
        }
        else if(velocity.x>0) {
            velocity.x-=damping;
        }

        //if bob's velocity becomes too low make it 0 so that he stays at rest
        if(direction==Direction.RIGHT && velocity.x<=0.02f){
            velocity.x=0.0f;
        }

        else if(direction==Direction.LEFT && velocity.x>=-0.02f){
            velocity.x=0.0f;
        }

        // if bob is not at rest, animate him
        if(velocity.x!=0){
            updateAnimationStateTime=true;
        }

        velocity.add(gravity); // factor gravity into Bob's velocity
        checkWallHit();
        checkCollectibleHit();
        checkHazards();
        checkEnemies();
        move(velocity.x, velocity.y); // update Bob's position according to velocity

        if(updateAnimationStateTime){
            stateTime += Gdx.graphics.getDeltaTime();
            currentFrame = (TextureRegion) walkAnimation.getKeyFrame(stateTime, true);
        }

    }

    public void checkWallHit(){

        // set the bob's bounding rectangle to its position and dimensions
        bobRectangle.set(bobSprite.getX(), bobSprite.getY(), bobSprite.getWidth(), bobSprite.getHeight());

        // get the tiles from map utilities
        Array<Rectangle> tiles = MapUtils.getHorizNeighbourTiles(velocity, bobSprite, "Wall");

        //if bob collides with any tile while walking right, stop his horizontal motion
        for (Rectangle tile : tiles) {
            if (bobRectangle.overlaps(tile)) {
                velocity.x = 0;
                break;
            }
        }

        bobRectangle.x = bobSprite.getX();
        tiles=    MapUtils.getVertNeighbourTiles(velocity, bobSprite, "Wall");

        bobRectangle.y += velocity.y;
        for (Rectangle tile : tiles) {
            if (bobRectangle.overlaps(tile)) {
                // we reset the Bob's y-position here
                // so it is just below/above the tile we collided with

                if (velocity.y > 0) {
                    bobSprite.setY( tile.y - bobSprite.getHeight() );

                }
                else if(velocity.y < 0) {
                    bobSprite.setY( tile.y + tile.height);
                    isGrounded=true;
                }
                velocity.y = 0;
                break;
            }
        }

    }

    public void checkCollectibleHit(){
        // set the bob's bounding rectangle to its position and dimensions
        bobRectangle.set(bobSprite.getX(), bobSprite.getY(), bobSprite.getWidth(), bobSprite.getHeight());

        // get the tiles from map utilities
        Array<Rectangle> tiles = MapUtils.getHorizNeighbourTiles(velocity, bobSprite, "Collectibles");

        // get the collectibles layer
        TiledMapTileLayer layer = (TiledMapTileLayer)GameManager.map.getLayers().get("Collectibles");

        //if bob collides with any tile while walking right, check the points and update score
        for (Rectangle tile : tiles) {
            if (bobRectangle.overlaps(tile)) {
                MapProperties tilePoperties=layer.getCell((int)tile.x, (int)tile.y).getTile().getProperties();
                int itemPoints=Integer.parseInt(tilePoperties.get("points").toString());
                GameData.score+=itemPoints;
                layer.setCell((int)tile.x, (int)tile.y, null);
                break;
            }
        }

        bobRectangle.x = bobSprite.getX();
        tiles= MapUtils.getVertNeighbourTiles(velocity, bobSprite, "Collectibles");

        bobRectangle.y += velocity.y;
        for (Rectangle tile : tiles) {
            if (bobRectangle.overlaps(tile)) {
                MapProperties tilePoperties=layer.getCell((int)tile.x, (int)tile.y).getTile().getProperties();
                int itemPoints=Integer.parseInt(tilePoperties.get("points").toString());
                GameData.score+=itemPoints;
                layer.setCell((int)tile.x, (int)tile.y, null);
            }
        }
    }

    public void checkHazards(){
        // set the bob's bounding rectangle to its position and dimensions
        bobRectangle.set(bobSprite.getX(), bobSprite.getY(), bobSprite.getWidth(), bobSprite.getHeight());
        // get the tiles from map utilities
        Array<Rectangle> tiles = MapUtils.getHorizNeighbourTiles(velocity, bobSprite, "Hazards");
        //if bob collides with any tile while walking right, check the points and update score
        for (Rectangle tile : tiles) {
            if (bobRectangle.overlaps(tile)) {
                if(GameData.lives>0){
                    setPosition(GameConstants.SPAWN_POINT.x, GameConstants.SPAWN_POINT.y);
                    GameData.lives--;
                    break;
                }
                else{
                    Gdx.app.exit();
                }
            }
        }

        bobRectangle.x = bobSprite.getX();
        tiles= MapUtils.getVertNeighbourTiles(velocity, bobSprite, "Hazards");

        bobRectangle.y += velocity.y;
        for (Rectangle tile : tiles) {
            if (bobRectangle.overlaps(tile)) {
                if(GameData.lives>0){
                    setPosition(GameConstants.SPAWN_POINT.x, GameConstants.SPAWN_POINT.y);
                    GameData.lives--;
                    break;
                }
                else{
                    Gdx.app.exit();
                }

            }
        }
    }

    public void checkEnemies(){
        // set the bob's bounding rectangle to its position and dimensions
        bobRectangle.set(bobSprite.getX(), bobSprite.getY(), bobSprite.getWidth(), bobSprite.getHeight());

        // check whether bob collides with the enemies
        for(Enemy enemy: GameManager.enemies){
            if(enemy.rectangle.overlaps(bobRectangle)){
                if(GameData.lives>0){
                    setPosition(GameConstants.SPAWN_POINT.x, GameConstants.SPAWN_POINT.y);
                    GameData.lives--;
                    break;
                }
                else{
                    Gdx.app.exit();
                }
            }
        }
    }


    public void jump(){
        if(isGrounded){
            velocity.y=jumpVelocity;
            isGrounded=false;
        }

    }
}
