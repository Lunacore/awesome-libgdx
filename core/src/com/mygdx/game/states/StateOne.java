package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.helper.Helper;

public class StateOne extends State{

	float timer = -2;
	
	Interpolation test;
	
	public StateOne() {
		enablePhysics(new DefaultStateListener(this));
		setGravity(new Vector2(0, -20));
		enableDebugDraw();
		
		
		test = Helper.makeCustomInterpolation("maluco.json");
	}
	
	public void create() {
		setTmxMap("tiled/maps/test.tmx", 1);
	}

	Vector2 temp = new Vector2(0, 0);
	
	public void render(SpriteBatch sb) {
		
		sr.begin(ShapeType.Filled);
		
		float val = test.apply(0, 1, timer / 10f);
		
		//System.out.println(val);
		
		sr.circle(
				(timer * 100 + 200) / State.PHYS_SCALE,
				
				( + 300) /State.PHYS_SCALE + val * 3,
				
				4 / State.PHYS_SCALE, 20);
		
		sr.end();
		
	}

	public void update(float delta) {		

		timer += delta;
		
	}

	public void dispose() {
		
	}

	@Override
	public void enter() {
		// TODO Auto-generated method stub
		
	}

}
