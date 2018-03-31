package de.android.ayrathairullin.dungeonbob;

import com.badlogic.gdx.Game;

public class MainGame extends Game {

	@Override
	public void create () {
		setScreen(new GameScreen(this));
	}
}
