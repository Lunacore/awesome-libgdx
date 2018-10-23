package com.mygdx.game.objects;

import com.mygdx.game.states.State;

public class ObjectInfo {
	
	State state;
	Integer z;
	float scale;
	
	public ObjectInfo(State state, Integer z, Float scale) {
		super();
		this.state = state;
		this.z = z;
		this.scale = scale;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public Integer getZ() {
		return z;
	}
	public void setZ(Integer z) {
		this.z = z;
	}
	public float getScale() {
		return scale;
	}
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public ObjectInfo withScale(float scale) {
		return new ObjectInfo(state, z, scale);
	}
	public ObjectInfo withZ(int z) {
		return new ObjectInfo(state, z, scale);
	}
	public ObjectInfo withState(State state) {
		return new ObjectInfo(state, z, scale);
	}
	public ObjectInfo withZAndScale(int z, float scale) {
		return new ObjectInfo(state, z, scale);
	}
	
	

}
