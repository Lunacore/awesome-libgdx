package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.states.State;
import com.mygdx.game.states.StateManager;

import io.anuke.gif.GifRecorder;

public class AwesomeLibGDX {
	
	static SpriteBatch batch;
	static StateManager manager;
	static GifRecorder gifRecorder;
	static boolean drawFPS = false;
	
	static BitmapFont font;
	
	public static void init() {
		batch = new SpriteBatch();
		batch.enableBlending();
		manager = new StateManager();
		gifRecorder = new GifRecorder(batch);
		
		font = new BitmapFont();
	}
	
	public static void drawFPS(boolean b) {
		drawFPS = b;
	}
	
	public static void create() {
		manager.create();
	}
	
	public static int addState(State state) {
		return manager.addState(state);
	}
	
	public static void render() {
		Gdx.gl.glClearColor(17/255f, 26/255f, 36/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		manager.update(Gdx.graphics.getDeltaTime());
		manager.render(batch);
		
		batch.setProjectionMatrix(Helper.getDefaultProjection());
		batch.begin();
		font.draw(batch, "FPS: " + (int)(1/Gdx.graphics.getDeltaTime()), 20, Gdx.graphics.getHeight() - 20);
		batch.end();
		
		gifRecorder.update();
		Helper.Game.globalTimer += Gdx.graphics.getDeltaTime();
	}
	
	public static void dispose () {
		batch.dispose();
		manager.dispose();
	}

}
