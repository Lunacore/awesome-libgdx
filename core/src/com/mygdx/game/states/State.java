package com.mygdx.game.states;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.objects.GameObject;
import com.mygdx.game.objects.GameParticle;
import com.mygdx.game.objects.KeyMapper.Device;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.objects.TmxRenderer;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

public abstract class State{
	
	public static final float PHYS_SCALE = 45f;
	
	public StateManager manager;
	ShapeRenderer sr;
	OrthographicCamera camera;
	ArrayList<GameObject> gos;
	TmxRenderer tmxRenderer;
	
	//Física
	private World world;
	Box2DDebugRenderer b2dr;
	boolean debugDraw = false;
	ArrayList<Body> forRemoval;
	boolean pause = false;
	
	public float worldStepFPS = 60;
	
	//Iluminação
	RayHandler rayHandler;
	
	public PointLight addPointLight(Color color, Vector2 position) {
		if(rayHandler != null)
		return new PointLight(rayHandler, 20, color, 5, position.x, position.y);
		
		return null;
	}
	
	public PointLight addPointLight(Color color, Vector2 position, float distance) {
		if(rayHandler != null)
		return new PointLight(rayHandler, 20, color, distance, position.x, position.y);
		
		return null;
	}
	
	public void removeObject(GameObject obj) {
		obj.dispose();
		gos.remove(obj);
		
	}
	
	
	
	@SuppressWarnings("rawtypes")
	public ArrayList<GameObject> getByClass(Class clazz){
		ArrayList<GameObject> result = new ArrayList<GameObject>();
		for(GameObject go : gos) {
			if(clazz.isInstance(go)) {
				result.add(go);
			}
		}
		return result;
	}
	
	public ConeLight addConeLight(Color color, Vector2 position, float distance, float angle, float coneAngle) {
		if(rayHandler != null)
		return new ConeLight(rayHandler, 20, color, distance, position.x, position.y, angle, coneAngle);
		
		return null;
	}
	
	public TmxRenderer getTmxRenderer() {
		return tmxRenderer;
	}
	
	public void setTmxMap(String map, float scale) {
		tmxRenderer = new TmxRenderer(new ObjectInfo(this, 0, scale), map);
		tmxRenderer.instanceObjects();
	}
	
	public void updateTmxLightInfo() {
		
		if(rayHandler == null) {
			System.err.println("Lights need to be activated first with enableLights()");
			Gdx.app.exit();
		}
		if(tmxRenderer == null) {
			System.err.println("A Tmx map need to be loaded first with setTmxMap()");
			Gdx.app.exit();
		}
		Color ambientLight = tmxRenderer.getTiledMap().getProperties().get("ambientLight", Color.class);
		ambientLight.a = tmxRenderer.getTiledMap().getProperties().get("ambientAlpha", Float.class);
		
		rayHandler.setAmbientLight(ambientLight);
	}
	
	public void enableLights() {
		rayHandler = new RayHandler(world);
	}
	
	public void pausePhysics() {
		pause = true;
	}
	
	public void resumePhysics() {
		pause = false;
	}
	
	public void changeState(int nextState) {
		manager.changeState(nextState);
	}
	
	public void addParticle(GameParticle gp) {
		gos.add(gp);
	}
	
	public void putInScene(GameObject go) {
		gos.add(go);
	}
	
	public State(StateManager manager) {
		this.manager = manager;
		sr = new ShapeRenderer();
		camera = new OrthographicCamera();
		camera.setToOrtho(false);
		
		forRemoval = new ArrayList<Body>();
		gos = new ArrayList<GameObject>();
	}
	
	public void enablePhysics(ContactListener listener) {
		setWorld(new World(new Vector2(0, -4f), false));
		
		if(listener != null)
		getWorld().setContactListener(listener);
		
		b2dr = new Box2DDebugRenderer();
		camera.zoom = 1/PHYS_SCALE;
		camera.position.set(new Vector3(Helper.Position.CENTER.cpy().scl(1/PHYS_SCALE), 0));
		
	}
	
	public void setGravity(Vector2 gravity) {
		getWorld().setGravity(gravity);
	}
	
	public void enableDebugDraw() {
		debugDraw = true;
	}
	
	public void disableDebugDraw() {
		debugDraw = false;
	}
	
	public abstract void create();
	
	public void preRender(SpriteBatch sb) {
		sb.setProjectionMatrix(camera.combined);
		sr.setProjectionMatrix(camera.combined);
	}
	
	public abstract void render(SpriteBatch sb);
	
	public void postRender(SpriteBatch sb) {
		
		if(tmxRenderer != null)
		tmxRenderer.render(sb, sr, camera);
		
		sb.begin();
		for(int i = gos.size() - 1; i>= 0; i --) {
			if(gos.get(i).isRendering()) {
				gos.get(i).preRender(sb, sr, camera);
				gos.get(i).render(sb, sr, camera);
				gos.get(i).postRender(sb, sr, camera);
			}
		}
		sb.end();
		
		if(b2dr != null && debugDraw) {
			b2dr.render(getWorld(), camera.combined);
		}
		
		if(rayHandler != null) {
			rayHandler.setCombinedMatrix(camera);
			rayHandler.updateAndRender();
		}
	}
	
	
	
	public void preUpdate(float delta) {
		camera.update();
		
		if(tmxRenderer != null)
		tmxRenderer.update(delta);
		
		Collections.sort(gos, new Comparator<GameObject>() {
			public int compare(GameObject arg0, GameObject arg1) {
				return arg1.getZ() - arg0.getZ();
			}
		});
		
		for(int i = gos.size() - 1; i>= 0; i --) {
			if(gos.get(i).update(delta)) {
				gos.get(i).dispose();
				gos.remove(i);
			}
		}
		
		if(getWorld() != null) {
			if(!pause) {
				for(int i = forRemoval.size() -1; i >= 0; i --) {
					Array<Body> bodies = new Array<Body>();
					getWorld().getBodies(bodies);
					if(bodies.contains(forRemoval.get(i), true)) {
						getWorld().destroyBody(forRemoval.get(i));
					}
				}
				forRemoval.clear();
				
				getWorld().step(1/worldStepFPS, 6, 2);
			}
		}
	}
	
	public void deleteBody(Body body) {
		if(!forRemoval.contains(body)) {
			forRemoval.add(body);
		}
	}
	
	public Body addDynamicRectangleBody(Vector2 position, Vector2 size) {
		return Helper.PhysHelp.createDynamicBoxBody(getWorld(), position, size);
	}
	
	public Body addStaticRectangleBody(Vector2 position, Vector2 size) {
		return Helper.PhysHelp.createStaticBoxBody(getWorld(), position, size);
	}
	
	public abstract void update(float delta);
	
	public void dispose() {
		if(gos != null) {
			for(int i = gos.size() - 1; i >= 0; i --) {
				removeObject(gos.get(i));
			}
		}
	}

	public boolean keyDown(int keycode) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).keyDown(keycode);
		}
		return false;
	}

	public boolean keyUp(int keycode) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).keyUp(keycode);
		}
		return false;
	}

	public boolean keyTyped(char character) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).keyTyped(character);
		}
		return false;
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).touchDown(screenX, screenY, pointer, button);
		}
		return false;
	}

	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).touchUp(screenX, screenY, pointer, button);
		}
		return false;
	}

	public boolean touchDragged(int screenX, int screenY, int pointer) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).touchDragged(screenX, screenY, pointer);
		}
		return false;
	}

	public boolean mouseMoved(int screenX, int screenY) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).mouseMoved(screenX, screenY);
		}
		return false;
	}

	public boolean scrolled(int amount) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).scrolled(amount);
		}
		return false;
	}

	public void connected(Controller controller) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).connected(controller);
		}
	}

	public void disconnected(Controller controller) {	
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).disconnected(controller);
		}
	}

	public boolean buttonDown(Controller controller, int buttonCode) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).buttonDown(controller, buttonCode);
		}
		return false;
	}

	public boolean buttonUp(Controller controller, int buttonCode) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).buttonUp(controller, buttonCode);
		}
		return false;
	}

	public boolean axisMoved(Controller controller, int axisCode, float value) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).axisMoved(controller, axisCode, value);
		}
		return false;
	}

	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).povMoved(controller, povCode, value);
		}
		return false;
	}

	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).xSliderMoved(controller, sliderCode, value);
		}
		return false;
	}

	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).ySliderMoved(controller, sliderCode, value);
		}
		return false;
	}

	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).accelerometerMoved(controller, accelerometerCode, value);
		}
		return false;
	}
	

	public void inputIn(Device device, String mapName) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).inputIn(device, mapName);
		}
	}
	public void inputOut(Device device, String mapName) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).inputOut(device, mapName);
		}
	}
			
	public void inputAxis(Device device, String axisName, float value) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).inputAxis(device, axisName, value);
		}
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public RayHandler getRayHandler() {
		return rayHandler;
	}

	
}
