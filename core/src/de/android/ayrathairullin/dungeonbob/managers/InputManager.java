package de.android.ayrathairullin.dungeonbob.managers;


import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class InputManager extends InputAdapter{
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.LEFT) {
            GameManager.bob.setLeftPressed(true);
        }else if (keycode == Keys.RIGHT) {
            GameManager.bob.setRightPressed(true);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Keys.LEFT) {
            GameManager.bob.setLeftPressed(false);
        }else if (keycode == Keys.RIGHT) {
            GameManager.bob.setRightPressed(false);
        }
        return false;
    }
}
