package com.mygdx.game.water;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.entities.GameObject;
import com.mygdx.game.entities.ObjectInfo;
import com.mygdx.game.helper.Helper.PhysHelp;
import com.mygdx.game.states.State;

public class Blob extends GameObject {
	
	Color color;
	float radius;
	
	protected Body body;
	
	float timer = 0;
	public boolean deleted;
	
	Vector2 initialPos;
	Vector2 initialVel;
	
	public Blob(ObjectInfo info, Vector2 position, Color color, Vector2 velocity, float radius, boolean createBody) {
		super(info);
		
		if(createBody) {
			createBody(position, velocity);
		}
		else {
			initialPos = position;
			initialVel = velocity;
		}
		
		this.color = color;
		this.radius = radius / State.PHYS_SCALE;
	}

	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color.cpy();
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		getState().deleteBody(body);
		deleted = true;
	}

	@Override
	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		// TODO Auto-generated method stub
		
	}

	public void createBody(Vector2 position, Vector2 velocity){
		body = PhysHelp.createDynamicCircleBody(getState().getWorld(), position.cpy(), radius * State.PHYS_SCALE);
		body.setLinearVelocity(velocity.cpy());
		body.getFixtureList().get(0).setFriction(0);
		body.setUserData(this);
	}
	
	@Override
	public boolean update(float delta) {
		if(body == null) {
			createBody(initialPos, initialVel);
		}		
		return false;
	}

	public Vector2 getPosition() {
		return body.getWorldCenter();
	}

	public Body getBody() {
		return body;
	}

}
