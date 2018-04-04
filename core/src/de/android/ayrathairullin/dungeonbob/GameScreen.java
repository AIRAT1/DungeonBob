package de.android.ayrathairullin.dungeonbob;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.android.ayrathairullin.dungeonbob.managers.GameManager;
import de.android.ayrathairullin.dungeonbob.managers.InputManager;

public class GameScreen implements Screen {
    MainGame game ;
    SpriteBatch batch; // spritebatch for drawing
    public static OrthographicCamera camera,hudCamera;

    public GameScreen (MainGame game){
        this.game=game;
        // get window dimensions and set our viewport dimensions
        float height= Gdx.graphics.getHeight();
        float width = Gdx.graphics.getWidth();
        // set our camera viewport to window dimensions
        camera = new OrthographicCamera(width,height);
        // center the camera at w/2,h/2
        camera.setToOrtho(false);

        batch = new SpriteBatch();
        //initialize the game
        GameManager.initialize(width, height);

        // set our hud camera's viewport to window dimensions
        hudCamera = new OrthographicCamera(width,height);
        // center the camera at w/2,h/2
        hudCamera.setToOrtho(false);

        Gdx.input.setInputProcessor(new InputManager(hudCamera)); // enable ImputManager to receive input events
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        // set the spritebatch's drawing view to the hud camera's view
        batch.setProjectionMatrix(hudCamera.combined);

        batch.begin();
        GameManager.renderBackground(batch);
        batch.end();

        // set the renderer's view to the game's main camera
        GameManager.renderer.render();

        // render the game objects
        batch.begin();
        GameManager.renderGame(batch);
        batch.end();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        //dispose the batch and the textures
        batch.dispose();
        GameManager.dispose();
    }
}
