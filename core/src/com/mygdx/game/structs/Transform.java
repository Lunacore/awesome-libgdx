package com.mygdx.game.structs;

import com.badlogic.gdx.math.Vector2;

public class Transform {
	
	Vector2 position;
	float angle;
	Vector2 scale;
	
	public Transform(Vector2 position, float angle, Vector2 scale) {
		super();
		this.position = position;
		this.angle = angle;
		this.scale = scale;
	}
	
	public Transform(Vector2 position) {
		this.position = position;
		this.angle = 0;
		this.scale = new Vector2(1, 1);
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public Vector2 getScale() {
		return scale;
	}

	public void setScale(Vector2 scale) {
		this.scale = scale;
	}
	
	public String toString() {
		return "Position: " + position.toString() + ", Angle: " + angle + ", Scale: " + scale.toString();
	}

	public void setPosition(float x, float y) {
		position.set(x, y);
	}

}
