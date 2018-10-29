package com.mygdx.game.test;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.objects.GameObject;
import com.mygdx.game.objects.ObjectInfo;

public class Letter extends GameObject{

	Integer[][] map;
	
	ArrayList<Vector2> boxesTarget;
	ArrayList<Vector2> boxes;
	
	float boxSize = 20;
	
	static final HashMap<String, Integer[][]> letters = new HashMap<String, Integer[][]>();
	
	static {
		for(int i = 0; i < 26; i ++) {
			String lt = "" + (char) (i + 97);
			letters.put(lt, getIntArray(lt));
		}
	}
	
	private static Integer[][] getIntArray(String letter) {
		JSONObject obj = new JSONObject(Gdx.files.internal("letters.txt").readString());
		JSONArray arr = obj.getJSONArray(letter);
		
		Integer[][] result = new Integer[5][5];
		for(int i = 0; i < 5; i ++) {
			JSONArray inner = arr.getJSONArray(i);
			for(int j = 0; j < 5; j ++) {
				result[i][j] = inner.getInt(j);
			}
		}
		
		return result;
	}
	
	public Letter(ObjectInfo info, String letter, Vector2 position, float boxSize) {
		super(info, new MapProperties());

		transform.setPosition(position);
		this.boxSize = boxSize;
		
		boxesTarget = new ArrayList<Vector2>();
		boxes = new ArrayList<Vector2>();
		
		map = getIntArray(letter.toLowerCase());
		
		loadTargets();
		
	}
	
	public void loadTargets() {
		int cont = 0;
		for(int i = 0; i < map.length; i ++) {
			for(int j = 0; j < map[0].length; j ++) {
				if(map[i][j] == 1) {
					if(cont >= boxesTarget.size()) {
						boxesTarget.add(new Vector2(j * boxSize, (5 - i) * boxSize));
					}
					else {
						boxesTarget.get(cont).set(j * boxSize, (5 - i) * boxSize);
					}
					cont++;
				}
			}
		}
				
		while(cont < boxesTarget.size()) {
			boxesTarget.remove(boxesTarget.size() - 1);
		}
		
		while(boxesTarget.size() < boxes.size()) {
			boxes.remove(boxes.size() - 1);
		}
	}
	
	public void morph(String lt) {
		if(lt.charAt(0) >= 97 && lt.charAt(0) <= 97 + 26) {
			map = letters.get(lt.toLowerCase());
		}
		else {
			boxesTarget.clear();
		}
		loadTargets();
	}

	public void create() {
		
	}

	public void dispose() {
		
	}
	
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	public int rnd() {
		return Math.random() < 0.5f ? 0 : 1;
	}
	
	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		
		sr.begin(ShapeType.Filled);
		for(Vector2 box : boxes) {
			sr.rect(box.x - boxSize/2f + transform.getPosition().x, box.y - boxSize/2f + transform.getPosition().y, boxSize, boxSize);
		}
		sr.end();
		
	}

	public boolean update(float delta) {
		
		for(int i = 0; i < boxesTarget.size(); i ++) {
			if(i >= boxes.size()) {
				boxes.add(new Vector2(3 * boxSize, 3 * boxSize));
			}
			boxes.get(i).add(
					boxesTarget.get(i).cpy().sub(boxes.get(i)).scl(1/5f)
					);
		}
		
		return false;
	}

}
