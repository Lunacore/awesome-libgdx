package com.mygdx.game.objects;

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
import com.mygdx.game.states.State;

public abstract class TopDownPlayer extends GameObject{
	
	protected Body body;
	protected float speed = 10;
	protected Vector2 input;
	
	
	int keyUp = Keys.UP;
	int keyDown = Keys.DOWN;
	int keyLeft = Keys.LEFT;
	int keyRight = Keys.RIGHT;
	
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
		body = Helper.PhysHelp.creatCircleBody(getState().getWorld(), get("width", Float.class)/2f, def);
		body.setUserData(this);
		input = new Vector2();
	}

	public abstract void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera);


	@Override
	public boolean update(float delta) {
		body.setLinearVelocity(input.cpy().nor().scl(speed));
		return false;
	}
	
	public boolean keyDown(int keycode) {
		if(keycode == keyLeft) {
			input.x = -1;
		}
		if(keycode == keyRight) {
			input.x = 1;
		}
		if(keycode == keyUp) {
			input.y = 1;
		}
		if(keycode == keyDown) {
			input.y = -1;
		}

		return false;
	}

	public boolean keyUp(int keycode) {
		if(keycode == keyLeft) {
			if(input.x == -1)
			input.x = 0;
		}
		if(keycode == keyRight) {
			if(input.x == 1)
			input.x = 0;
		}
		if(keycode == keyUp) {
			if(input.y == 1)
			input.y = 0;
		}
		if(keycode == keyDown) {
			if(input.y == -1)
			input.y = 0;
		}
		return false;
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

	public int getKeyUp() {
		return keyUp;
	}

	public void setKeyUp(int keyUp) {
		this.keyUp = keyUp;
	}

	public int getKeyDown() {
		return keyDown;
	}

	public void setKeyDown(int keyDown) {
		this.keyDown = keyDown;
	}

	public int getKeyLeft() {
		return keyLeft;
	}

	public void setKeyLeft(int keyLeft) {
		this.keyLeft = keyLeft;
	}

	public int getKeyRight() {
		return keyRight;
	}

	public void setKeyRight(int keyRight) {
		this.keyRight = keyRight;
	}
}
