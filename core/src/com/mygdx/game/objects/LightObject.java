package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.states.State;

public class LightObject extends GameObject{

	public LightObject(ObjectInfo info, MapProperties properties) {
		super(info, properties);
		
		if(properties.get("type", String.class).equals("pointLight")) {
			Color color = get("color", Color.class);
			color.a = get("intensity", Float.class);
			
			info.getState().addPointLight(color, new Vector2(get("x", Float.class) /  State.PHYS_SCALE, get("y", Float.class) / State.PHYS_SCALE), get("distance", Float.class));
		}
		else if(properties.get("type", String.class).equals("coneLight")) {
			Color color = get("color", Color.class);
			color.a = get("intensity", Float.class);
			
			info.getState().addConeLight(color, new Vector2(get("x", Float.class) /  State.PHYS_SCALE, get("y", Float.class) / State.PHYS_SCALE), get("distance", Float.class), get("rotation", Float.class, 0f), get("coneAngle", Float.class));
		}
	}

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		
	}

	public boolean update(float delta) {
		return false;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}

}
