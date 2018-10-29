package com.mygdx.game.objects;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.mygdx.game.states.State;

public abstract class EmptyContact implements ContactListener{
	
	protected State state;
	
	public EmptyContact(State state) {
		this.state = state;
	}
	
	@SuppressWarnings("rawtypes")
	public boolean compareCollision(Contact contact, Class class1, Class class2) {
		if(class1.isInstance(contact.getFixtureA().getBody().getUserData())) {
			if(class2.isInstance(contact.getFixtureB().getBody().getUserData())) {
				return true;
			}
		}
		
		if(class2.isInstance(contact.getFixtureA().getBody().getUserData())) {
			if(class1.isInstance(contact.getFixtureB().getBody().getUserData())) {
				return true;
			}
		}
		
		return false;
	}
	
	@SuppressWarnings("rawtypes")
	public <T> T getInstanceFromContact(Contact contact, Class class1) {
		if(class1.isInstance(contact.getFixtureA().getBody().getUserData())) {
			return (T) contact.getFixtureA().getBody().getUserData();
		}
		if(class1.isInstance(contact.getFixtureB().getBody().getUserData())) {
			return (T) contact.getFixtureB().getBody().getUserData();
		}
		new Exception("Object with class specified: \"" + class1.getSimpleName() + "\" not found in contact").printStackTrace();
		System.exit(1);
		return null;
	}

}
