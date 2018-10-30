package com.mygdx.game.test;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.objects.GameObject;
import com.mygdx.game.objects.ObjectInfo;

public class Butaaaum extends GameObject{

	Body body;
	
	public Butaaaum(ObjectInfo info, MapProperties properties) {
		super(info, properties);
		
		body = get("body", Body.class);
		
		System.out.println(body);
	}

	public void create() {
		
	}

	public void dispose() {
		
	}

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		
	}

	public boolean update(float delta) {
		return false;
	}

}
