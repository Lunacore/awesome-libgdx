package com.mygdx.game.objects;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.states.State;

public abstract class PlatformPlayer extends GameObject{
		
	protected Body body;
	
	protected int direction = 1;
	
	protected boolean left;
	protected boolean right;
	protected boolean onFloor;
	protected float jumpStrength;
	protected int jumps;
	protected int totalJumps;
	protected float speed = 10;
	
	public PlatformPlayer(ObjectInfo info, MapProperties properties) {
		super(info, properties);
		BodyDef def = new BodyDef();
		onFloor = true;
		def.position.set(get("position", Vector2.class).cpy().scl(1/State.PHYS_SCALE));
		def.type = BodyType.DynamicBody;
		def.fixedRotation = true;
		body = Helper.PhysHelp.createBoxBody(getState().getWorld(), get("size", Vector2.class), def);
		Fixture foot = Helper.PhysHelp.createCircleFixture(body, new Vector2(0, -get("size", Vector2.class).y/2f), get("size", Vector2.class).x/2f);
		foot.setUserData("PLAYER_FOOT");
		body.setUserData(this);
		setJumpStrength(20);
		setTotalJumps(2);
	}

	
	public boolean update(float delta) {
		
		if(left) {
			body.setLinearVelocity(-speed, body.getLinearVelocity().y);
		}
		if(right) {
			body.setLinearVelocity(speed, body.getLinearVelocity().y);
		}
		if(!left && !right) {
			body.setLinearVelocity(body.getLinearVelocity().x * 0.9f, body.getLinearVelocity().y);
		}
		
		if(onFloor) {
			jumps = totalJumps;
		}
		
		return false;
	}

	public boolean keyDown(int keycode) {
		if(keycode == Keys.LEFT) {
			left = true;
			direction = -1;
		}
		if(keycode == Keys.RIGHT) {
			right = true;
			direction = 1;
		}
		if(keycode == Keys.UP) {
			if(jumps > 0) {
				body.setLinearVelocity(body.getLinearVelocity().x, 0);
				body.applyLinearImpulse(new Vector2(0, body.getMass() * jumpStrength), body.getWorldCenter(), true);
				jumps --;
			}
		}

		return false;
	}

	public boolean keyUp(int keycode) {
		if(keycode == Keys.LEFT) {
			left = false;
		}
		if(keycode == Keys.RIGHT) {
			right = false;
		}
		return false;
	}

	public void setOnFloor(boolean b) {
		onFloor = b;
	}

	public float getJumpStrength() {
		return jumpStrength;
	}

	public void setJumpStrength(float jumpStrength) {
		this.jumpStrength = jumpStrength;
	}

	public int getTotalJumps() {
		return totalJumps-1;
	}

	public void setTotalJumps(int totalJumps) {
		this.totalJumps = totalJumps-1;
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public static void beginContact(Contact contact, EmptyContact listener) {
		if(
				(contact.getFixtureA().getUserData() != null && contact.getFixtureA().getUserData().equals("PLAYER_FOOT"))
				||
				(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals("PLAYER_FOOT"))
				) {
			PlatformPlayer player = (PlatformPlayer) listener.getInstanceFromContact(contact, PlatformPlayer.class);
			player.setOnFloor(true);
		}
	}

	public static void endContact(Contact contact, EmptyContact listener) {
		if(
				(contact.getFixtureA().getUserData() != null && contact.getFixtureA().getUserData().equals("PLAYER_FOOT"))
				||
				(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals("PLAYER_FOOT"))
				) {
			PlatformPlayer player = (PlatformPlayer) listener.getInstanceFromContact(contact, PlatformPlayer.class);
			player.setOnFloor(false);
		}
	}

	public Vector2 getBodyPosition() {
		return body.getWorldCenter();
	}

	public void setBodyPosition(Vector2 position) {
		body.setTransform(position.x, position.y, 0);
	}

	
}
