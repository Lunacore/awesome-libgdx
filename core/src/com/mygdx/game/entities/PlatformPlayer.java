package com.mygdx.game.entities;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.input.KeyMapper;
import com.mygdx.game.input.XBoxController;
import com.mygdx.game.input.KeyMapper.Device;
import com.mygdx.game.states.State;

public abstract class PlatformPlayer extends GameObject{
		
	protected Body body;
	
	protected int direction = 1;
	
	protected boolean left;
	protected boolean right;
	private boolean onFloor;
	protected float jumpStrength;
	private int jumps;
	protected int totalJumps;
	protected float speed = 7;
	
	public PlatformPlayer(ObjectInfo info, MapProperties properties) {
		super(info, properties);
		BodyDef def = new BodyDef();
		Vector2 position = new Vector2(get("x", Float.class) + get("width", Float.class)/2f, get("y", Float.class) + get("height", Float.class) / 2f);
		def.position.set(position.cpy().scl(1/State.PHYS_SCALE));
		def.type = BodyType.DynamicBody;
		def.fixedRotation = true;
		Vector2 size = new Vector2(get("width", Float.class)/2f, get("height", Float.class)/2f);
		body = Helper.PhysHelp.createBoxBody(getState().getWorld(), size, def);
		body.getFixtureList().get(0).setFriction(0);
		Fixture foot = Helper.PhysHelp.createCircleFixture(body, new Vector2(0, -size.y/2f), size.x/2f * 0.9f);
		foot.setUserData("PLAYER_FOOT");
		body.setUserData(this);
		setJumpStrength(7);
		setTotalJumps(1);
		
		getState().manager.registerKey("Left", Device.KEYBOARD, Keys.A);
		getState().manager.registerKey("Right", Device.KEYBOARD, Keys.D);
		getState().manager.registerKey("Jump", Device.KEYBOARD, Keys.W);
		
		getState().manager.registerKey("Left", Device.CONTROLLER, XBoxController.POV_LEFT);
		getState().manager.registerKey("Right", Device.CONTROLLER, XBoxController.POV_RIGHT);
		getState().manager.registerKey("Jump", Device.CONTROLLER, XBoxController.POV_UP);
	}
	
	public PlatformPlayer(ObjectInfo info, Vector2 position, Vector2 size) {
		super(info, new MapProperties());
		BodyDef def = new BodyDef();
		def.position.set(position.cpy().scl(1/State.PHYS_SCALE));
		def.type = BodyType.DynamicBody;
		def.fixedRotation = true;
		body = Helper.PhysHelp.createBoxBody(getState().getWorld(), size, def);
		Fixture foot = Helper.PhysHelp.createCircleFixture(body, new Vector2(0, -size.y/2f), size.x/2f);
		foot.setUserData("PLAYER_FOOT");
		body.setUserData(this);
		setJumpStrength(20);
		setTotalJumps(2);
		
		getState().manager.registerKey("Left", Device.KEYBOARD, Keys.A);
		getState().manager.registerKey("Right", Device.KEYBOARD, Keys.D);
		getState().manager.registerKey("Jump", Device.KEYBOARD, Keys.W);
		
		getState().manager.registerKey("Left", Device.CONTROLLER, XBoxController.POV_LEFT);
		getState().manager.registerKey("Right", Device.CONTROLLER, XBoxController.POV_RIGHT);
		getState().manager.registerKey("Jump", Device.CONTROLLER, XBoxController.POV_UP);
	}

	
	public Vector2 getWorldPosition() {
		return body.getWorldCenter();
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
		
		if(getState().inputPressed("Jump") && body.getLinearVelocity().y > 0) {
			body.setGravityScale(0.5f);
		}
		else {
			body.setGravityScale(1);
		}
		
		if(body.getLinearVelocity().y < 0) {
			body.setGravityScale(2);
		}
		
		return false;
	}
	
	@Override
	public void inputIn(Device device, String mapName) {
		if(mapName.equals("Left")) {
			left = true;
			direction = -1;
		}
		if(mapName.equals("Right")) {
			right = true;
			direction = 1;
		}
		if(mapName.equals("Jump")) {
			if(getRemainingJumps() > 0) {
				body.setLinearVelocity(body.getLinearVelocity().x, 0);
				body.applyLinearImpulse(new Vector2(0, body.getMass() * jumpStrength), body.getWorldCenter(), true);
				setJumps(getRemainingJumps() - 1);
			}
		}
	}
	
	public void inputOut(Device device, String mapName) {
		if(mapName.equals("Left")) {
			left = false;
		}
		if(mapName.equals("Right")) {
			right = false;
		}
	}
	public void setOnFloor(boolean b) {
		onFloor = b;
		if(b) {
			setJumps(totalJumps);
		}
	}

	public float getJumpStrength() {
		return jumpStrength;
	}

	public void setJumpStrength(float jumpStrength) {
		this.jumpStrength = jumpStrength;
	}

	public int getTotalJumps() {
		return totalJumps;
	}

	public void setTotalJumps(int totalJumps) {
		this.totalJumps = totalJumps;
		setJumps(totalJumps);
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
	
	public void dispose() {
		getState().deleteBody(body);
	}

	public Vector2 getBodyPosition() {
		return body.getWorldCenter();
	}

	public void setBodyPosition(Vector2 position) {
		body.setTransform(position.x, position.y, 0);
	}

	public int getRemainingJumps() {
		return jumps;
	}

	public void setJumps(int jumps) {
		this.jumps = jumps;
	}

	public boolean isOnFloor() {
		return onFloor;
	}
	
}
