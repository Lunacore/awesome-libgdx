package com.mygdx.game.states;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.input.XBoxController;
import com.mygdx.game.input.KeyMapper.Device;

public class StateOne extends State{

	public StateOne() {
		enablePhysics(new StateOneListener(this));
		setGravity(new Vector2(0, -20));
		enableDebugDraw();
		
	}
	
	public void create() {
		setTmxMap("tiled/maps/test.tmx", 1);
	}

	public void render(SpriteBatch sb) {
		
	}

	public void update(float delta) {		

	}

	public void dispose() {
		
	}

}
