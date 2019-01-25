package com.mygdx.game.entities;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.input.KeyMapper.Device;
import com.mygdx.game.states.State;

public abstract class TopDownPlayer extends GameObject{
	
	protected Body body;
	protected float speed = 10;
	protected Vector2 input;
	
	public TopDownPlayer(ObjectInfo info, MapProperties properties) {
		super(info, properties);
		transform.setPosition(Vector2.Zero.cpy());
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.fixedRotation = true;
		Vector2 position = new Vector2(
				get("x", Float.class) + get("width", Float.class) / 2f,
				get("y", Float.class) + get("height", Float.class) / 2f);
		
		def.position.set(position.scl(1/State.PHYS_SCALE));
		body = Helper.PhysHelp.createCircleBody(getState().getWorld(), get("width", Float.class)/2f, def);
		body.setUserData(this);
		input = new Vector2();
		
		getState().manager.registerKey("Up", Device.KEYBOARD, Keys.W);
		getState().manager.registerKey("Left", Device.KEYBOARD, Keys.A);
		getState().manager.registerKey("Down", Device.KEYBOARD, Keys.S);
		getState().manager.registerKey("Right", Device.KEYBOARD, Keys.D);

	}
	
	
	public Vector2 getWorldPosition() {
		return body.getWorldCenter();
	}

	
	public TopDownPlayer(ObjectInfo info, Vector2 position, float radius) {
		super(info);
		
		transform.setPosition(Vector2.Zero.cpy());
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.fixedRotation = true;
		def.position.set(position.scl(1/State.PHYS_SCALE));
		body = Helper.PhysHelp.createCircleBody(getState().getWorld(), radius, def);
		body.setUserData(this);
		input = new Vector2();
		
		getState().manager.registerKey("Up", Device.KEYBOARD, Keys.W);
		getState().manager.registerKey("Left", Device.KEYBOARD, Keys.A);
		getState().manager.registerKey("Down", Device.KEYBOARD, Keys.S);
		getState().manager.registerKey("Right", Device.KEYBOARD, Keys.D);
	}

	public abstract void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera);


	@Override
	public boolean update(float delta) {
		body.setLinearVelocity(input.cpy().nor().scl(speed));
		return false;
	}
	
	@Override
	public void inputIn(Device device, String mapName) {
		if(mapName.equals("Left")) {
			input.x = -1;
		}
		if(mapName.equals("Right")) {
			input.x = 1;
		}
		if(mapName.equals("Up")) {
			input.y = 1;
		}
		if(mapName.equals("Down")) {
			input.y = -1;
		}
		super.inputIn(device, mapName);
	}
	
	@Override
	public void inputOut(Device device, String mapName) {
		if(mapName.equals("Left")) {
			if(input.x == -1)
			input.x = 0;
		}
		if(mapName.equals("Right")) {
			if(input.x == 1)
			input.x = 0;
		}
		if(mapName.equals("Up")) {
			if(input.y == 1)
			input.y = 0;
		}
		if(mapName.equals("Down")) {
			if(input.y == -1)
			input.y = 0;
		}
		super.inputOut(device, mapName);
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public Vector2 getBodyPosition() {
		return body.getWorldCenter();
	}

	public void setBodyPosition(Vector2 position) {
		body.setTransform(position.x, position.y, 0);
	}
}
