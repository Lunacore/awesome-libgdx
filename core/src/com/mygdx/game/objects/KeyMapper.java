package com.mygdx.game.objects;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.states.StateManager;

public class KeyMapper implements InputProcessor, ControllerListener{
	
	StateManager manager;
	
	HashMap<String, ArrayList<KeyMap>> keymaps;
	HashMap<String, ArrayList<AxisMap>> axismaps;
	
	public KeyMapper(StateManager manager) {
		this.manager = manager;
		Gdx.input.setInputProcessor(this);
		Controllers.addListener(this);
		
		keymaps = new HashMap<String,ArrayList<KeyMap>>();
		axismaps = new HashMap<String, ArrayList<AxisMap>>();
	}
	
	public void registerKeyMap(String name, Device device, int keycode) {
		if(keymaps.get(name) == null){
			keymaps.put(name, new ArrayList<KeyMapper.KeyMap>());
		}
		
		if(!arrayContainsKey(keymaps.get(name), device, keycode)) {
			keymaps.get(name).add(new KeyMap(device, keycode));
		}
	}

	public boolean keyDown(int keycode) {
		//Dispara os keymaps que usam esse codigo
		for(String key : findKeyMaps(Device.KEYBOARD, keycode)) {
			inputIn(Device.KEYBOARD, key);
		}
		
		manager.keyDown(keycode);
		return false;
	}

	public boolean keyUp(int keycode) {
		//Dispara os keymaps que usam esse codigo
		for(String key : findKeyMaps(Device.KEYBOARD, keycode)) {
			inputOut(Device.KEYBOARD, key);
		}
		manager.keyUp(keycode);
		return false;
	}

	public boolean keyTyped(char character) {
		manager.keyTyped(character);
		return false;
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		manager.touchDown(screenX, screenY, pointer, button);
		return false;
	}

	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		manager.touchUp(screenX, screenY, pointer, button);
		return false;
	}

	public boolean touchDragged(int screenX, int screenY, int pointer) {
		manager.touchDragged(screenX, screenY, pointer);
		return false;
	}

	public boolean mouseMoved(int screenX, int screenY) {
		manager.mouseMoved(screenX, screenY);
		return false;
	}

	public boolean scrolled(int amount) {
		manager.scrolled(amount);
		return false;
	}

	public void connected(Controller controller) {
		manager.connected(controller);
	}

	public void disconnected(Controller controller) {
		manager.disconnected(controller);
	}

	public boolean buttonDown(Controller controller, int buttonCode) {
		//Dispara os keymaps que usam esse codigo
		for(String key : findKeyMaps(Device.CONTROLLER, buttonCode)) {
			inputIn(Device.CONTROLLER, key);
		}
				
		manager.buttonDown(controller, buttonCode);
		return false;
	}

	public boolean buttonUp(Controller controller, int buttonCode) {
		//Dispara os keymaps que usam esse codigo
		for(String key : findKeyMaps(Device.CONTROLLER, buttonCode)) {
			inputOut(Device.CONTROLLER, key);
		}
		
		manager.buttonUp(controller, buttonCode);
		return false;
	}

	public boolean axisMoved(Controller controller, int axisCode, float value) {
		manager.axisMoved(controller, axisCode, value);
		return false;
	}
	
	int lastPovPressed = XBoxController.POV_CENTER;

	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		
		if(
			value == PovDirection.north ||
			value == PovDirection.south ||
			value == PovDirection.east ||
			value == PovDirection.west) {
			
			handlePov(getPov(value));
		}
		
		else {
			
			if(value == PovDirection.northEast) {
				handlePov(getPov(PovDirection.north));
				handlePov(getPov(PovDirection.east));
			}
			if(value == PovDirection.northWest) {
				handlePov(getPov(PovDirection.north));
				handlePov(getPov(PovDirection.west));
			}
			if(value == PovDirection.southEast) {
				handlePov(getPov(PovDirection.south));
				handlePov(getPov(PovDirection.east));
			}
			if(value == PovDirection.southWest) {
				handlePov(getPov(PovDirection.south));
				handlePov(getPov(PovDirection.west));
			}
			
		}
		
		manager.povMoved(controller, povCode, value);
		return false;
	}
	
	public void handlePov(int povcode) {
		if(povcode != lastPovPressed && lastPovPressed != XBoxController.POV_CENTER) {
			
			//Dispara os keymaps que usam esse codigo
			for(String key : findKeyMaps(Device.CONTROLLER, lastPovPressed)) {
				inputOut(Device.CONTROLLER, key);
			}
			
			
		}
		else{
			//Dispara os keymaps que usam esse codigo
			for(String key : findKeyMaps(Device.CONTROLLER, povcode)) {
				inputIn(Device.CONTROLLER, key);
			}
		}
		
		lastPovPressed = povcode;
		
		
	}
	
	public int getPov(PovDirection pov) {
		if (pov == PovDirection.north) return XBoxController.POV_UP;
		if (pov == PovDirection.south) return XBoxController.POV_DOWN;
		if (pov == PovDirection.west) return XBoxController.POV_LEFT;
		if (pov == PovDirection.east) return XBoxController.POV_RIGHT;
		if (pov == PovDirection.center) return XBoxController.POV_CENTER;
		
		return -1;
	}

	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
		manager.xSliderMoved(controller, sliderCode, value);
		return false;
	}

	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
		manager.ySliderMoved(controller, sliderCode, value);
		return false;
	}

	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
		manager.accelerometerMoved(controller, accelerometerCode, value);
		return false;
	}
	
	public void inputIn(Device device, String mapName) {
		manager.inputIn(device, mapName);
	}
	
	public void inputOut(Device device, String mapName) {
		manager.inputOut(device, mapName);
	}
	
	public ArrayList<String> findKeyMaps(Device device, int keycode){
		ArrayList<String>  ret = new ArrayList<String>();
		for(String mapName : keymaps.keySet()) {
			
			if(arrayContainsKey(keymaps.get(mapName), device, keycode)) {
				ret.add(mapName);
			}
			
		}
		return ret;
	}
	
	public boolean arrayContainsKey(ArrayList<KeyMap> list, Device device, int keycode) {
		for(KeyMap k : list) {
			if(k.keycode == keycode) {
				return true;
			}
		}
		
		return false;
		
	}
	
	public enum Device{
		KEYBOARD,
		CONTROLLER
	};
	
	public static class KeyMap {
		public int keycode;
		public Device device;
		
		public KeyMap(Device device, int keycode) {
			this.device = device;
			this.keycode = keycode;
		}
	}
	
	public static class AxisMap {
		public int axisCode;
		public Device device;
		
		public AxisMap(Device device, int axisCode) {
			this.device = device;
			this.axisCode = axisCode;
		}
	}

	
	
}
