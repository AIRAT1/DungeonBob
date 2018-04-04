package de.android.ayrathairullin.dungeonbob.managers;


import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class InputManager extends InputAdapter{
    OrthographicCamera camera;
    static Vector3 temp = new Vector3(); // temporary vector

    public InputManager(OrthographicCamera camera) {

        this.camera = camera;
    }


    @Override
    public boolean keyDown(int keycode) {
        // set the left key status to pressed
        if(keycode==Keys.LEFT){
            GameManager.bob.setLeftPressed(true);
        }
        // set the right key status to pressed
        else if(keycode==Keys.RIGHT){
            GameManager.bob.setRightPressed(true);
        }

        // make bob jump
        else if(keycode==Keys.SPACE){
            GameManager.bob.jump();
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // set the left key status to pressed
        if(keycode==Keys.LEFT){
            GameManager.bob.setLeftPressed(false);
        }
        // set the right key status to pressed
        else if(keycode==Keys.RIGHT){
            GameManager.bob.setRightPressed(false);
        }

        return false;
    }

    boolean isLeftPaddleTouched(float touchX, float touchY){
        // handle touch input on the left paddle
        if((touchX>=GameManager.leftPaddleSprite.getX()) && touchX<=(GameManager.leftPaddleSprite.getX()+GameManager.leftPaddleSprite.getWidth()) && (touchY>=GameManager.leftPaddleSprite.getY()) && touchY<=(GameManager.leftPaddleSprite.getY()+GameManager.leftPaddleSprite.getHeight()) ){
            return true;
        }
        return false;
    }

    boolean isRightPaddleTouched(float touchX, float touchY){
        // handle touch input on the right paddle
        if((touchX>=GameManager.rightPaddleSprite.getX()) && touchX<=(GameManager.rightPaddleSprite.getX()+GameManager.rightPaddleSprite.getWidth()) && (touchY>=GameManager.rightPaddleSprite.getY()) && touchY<=(GameManager.rightPaddleSprite.getY()+GameManager.rightPaddleSprite.getHeight()) ){
            return true;
        }
        return false;
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        temp.set(screenX,screenY, 0);
        //get the touch co-ordinates with respect to the camera's viewport
        camera.unproject(temp);

        float touchX = temp.x;
        float touchY = temp.y;

        if(isLeftPaddleTouched(touchX,touchY)){
            GameManager.bob.setLeftPaddleTouched(true);
        }
        else if(isRightPaddleTouched(touchX,touchY)){
            GameManager.bob.setRightPaddleTouched(true);
        }
        else if(isjumpButtonTouched(touchX, touchY)){
            GameManager.bob.jump();
        }

        return false;

    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        temp.set(screenX,screenY, 0);
        //get the touch co-ordinates with respect to the camera's viewport
        camera.unproject(temp);

        float touchX = temp.x;
        float touchY = temp.y;

        if(isLeftPaddleTouched(touchX,touchY)){
            GameManager.bob.setLeftPaddleTouched(false);
        }
        else if(isRightPaddleTouched(touchX,touchY)){
            GameManager.bob.setRightPaddleTouched(false);
        }
        return false;
    }

    boolean isjumpButtonTouched(float touchX, float touchY){
        // handle touch input on the jump button
        if((touchX>=GameManager.jumpButtonSprite.getX()) && touchX<=(GameManager.jumpButtonSprite.getX()+GameManager.jumpButtonSprite.getWidth()) && (touchY>=GameManager.jumpButtonSprite.getY()) && touchY<=(GameManager.jumpButtonSprite.getY()+GameManager.jumpButtonSprite.getHeight()) ){
            return true;
        }
        return false;
    }
}
