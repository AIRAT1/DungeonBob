package de.android.ayrathairullin.dungeonbob;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.android.ayrathairullin.dungeonbob.managers.GameManager;
import de.android.ayrathairullin.dungeonbob.managers.InputManager;

public class GameScreen implements Screen {
    MainGame game;
    SpriteBatch batch;
    public static OrthographicCamera camera, hudCamera;

    public GameScreen(MainGame game) {
        this.game = game;
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(width, height);
        hudCamera = new OrthographicCamera(width, height);
        camera.setToOrtho(false);
        hudCamera.setToOrtho(false);
        batch = new SpriteBatch();
        GameManager.initialize(width, height);
        Gdx.input.setInputProcessor(new InputManager(hudCamera));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();
        GameManager.renderBackground(batch);
        batch.end();

        GameManager.renderer.render();

        batch.begin();
        GameManager.renderGame(batch);
        batch.end();
//        GameManager.drawShapes();
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
        dispose();
    }

    @Override
    public void dispose() {
        batch.dispose();
        GameManager.dispose();
    }
}
