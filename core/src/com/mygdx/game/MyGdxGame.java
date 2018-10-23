package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.objects.Triangulator;
import com.mygdx.game.objects.Triangulator.TriangulatedNode;
import com.mygdx.game.states.StateManager;

import io.anuke.gif.GifRecorder;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	
	StateManager manager;
	GifRecorder gifRecorder;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new StateManager();
		manager.create();
		gifRecorder = new GifRecorder(batch);

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(17/255f, 26/255f, 36/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		manager.update(Gdx.graphics.getDeltaTime());
		manager.render(batch);
		
		gifRecorder.update();
		Helper.Game.globalTimer += Gdx.graphics.getDeltaTime();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		manager.dispose();
	}
}
