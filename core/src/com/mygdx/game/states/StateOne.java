package com.mygdx.game.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;

public class StateOne extends State{

	public StateOne(StateManager manager) {
		super(manager);
		enablePhysics(new StateOneListener(this));
		enableDebugDraw();
		
		setTmxMap("tiled/maps/test.tmx", 1);
		
	}
	
	public void create() {
		
	}

	public void render(SpriteBatch sb) {
		
	}

	public void update(float delta) {		

	}

	public void dispose() {
		
	}

}
