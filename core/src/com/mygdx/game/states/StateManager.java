package com.mygdx.game.states;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.utils.FrameBufferStack;
import com.mygdx.game.utils.ScreenSize;

public class StateManager implements InputProcessor, ControllerListener{
	
	ArrayList<State> states;
	int currentState;
	int nextState;
	
	float alpha = 0;
	
	FrameBuffer stateBuffer;
	
	float seconds = 0.1f;
	float transitionSpeed = 1 / seconds;
	
	
	
	public StateManager() {
		
		Gdx.input.setInputProcessor(this);
		Controllers.addListener(this);
		
		states = new ArrayList<State>();
		states.add(new StateOne(this));
				
		stateBuffer = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
	}
	
	public void setTransitionSpeed(float seconds) {
		this.seconds = seconds;
	}
	
	public void changeState(int nextState) {
		states.get(nextState).create();
		this.nextState = nextState;
	}
	
	public State current() {
		return states.get(currentState);
	}
	
	public void create() {
		current().create();
	}
	
	public void render(SpriteBatch sb) {
		
		sb.setColor(1, 1, 1, 1);
		current().preRender(sb);
		current().render(sb);
		current().postRender(sb);
		
		if(nextState != currentState) {
			FrameBufferStack.begin(stateBuffer);
			states.get(nextState).render(sb);
			FrameBufferStack.end();
			
			sb.begin();
			sb.setColor(1, 1, 1, alpha);
			sb.draw(FrameBufferStack.getTexture(), 0, 0, ScreenSize.getWidth(),ScreenSize.getHeight(),
					0, 0, ScreenSize.getWidth(), ScreenSize.getHeight(), false, true);
			sb.end();
			
			alpha += Gdx.graphics.getDeltaTime() * transitionSpeed;
			if(alpha > 1) {
				alpha = 0;
				currentState = nextState;
			}
		}
	}
	
	public void update(float delta) {
		current().preUpdate(delta);
		current().update(delta);
	}
	
	public void dispose() {
		current().dispose();
	}

	public void connected(Controller controller) {
		current().connected(controller);
	}

	public void disconnected(Controller controller) {
		current().disconnected(controller);		
	}

	public boolean buttonDown(Controller controller, int buttonCode) {
		return current().buttonDown(controller, buttonCode);
	}

	public boolean buttonUp(Controller controller, int buttonCode) {
		return current().buttonUp(controller, buttonCode);
	}

	public boolean axisMoved(Controller controller, int axisCode, float value) {
		return current().axisMoved(controller, axisCode, value);
	}

	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		return current().povMoved(controller, povCode, value);
	}

	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
		return current().xSliderMoved(controller, sliderCode, value);
	}

	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
		return current().ySliderMoved(controller, sliderCode, value);
	}

	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
		return current().accelerometerMoved(controller, accelerometerCode, value);
	}

	public boolean keyDown(int keycode) {
		if(keycode == Keys.ESCAPE) {
			Gdx.app.exit();
		}
		return current().keyDown(keycode);
	}

	public boolean keyUp(int keycode) {
		return current().keyUp(keycode);
	}

	public boolean keyTyped(char character) {
		return current().keyTyped(character);
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return current().touchDown(screenX, Gdx.graphics.getHeight() - screenY, pointer, button);
	}

	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return current().touchUp(screenX, Gdx.graphics.getHeight() - screenY, pointer, button);
	}

	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return current().touchDragged(screenX, Gdx.graphics.getHeight() - screenY, pointer);
	}

	public boolean mouseMoved(int screenX, int screenY) {
		return current().mouseMoved(screenX, Gdx.graphics.getHeight() - screenY);
	}

	public boolean scrolled(int amount) {
		return current().scrolled(amount);
	}

}
