package com.mygdx.game;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.states.StateManager;
import com.mygdx.game.states.StateOne;

import io.anuke.gif.GifRecorder;

public class MyGdxGame extends ApplicationAdapter {
		
	public void create () {
		AwesomeLibGDX.init();
		AwesomeLibGDX.addState(new StateOne());
		AwesomeLibGDX.create();
	}

	public void render () {
		AwesomeLibGDX.render();
	}
	
	public void dispose () {
		AwesomeLibGDX.dispose();
	}
}
