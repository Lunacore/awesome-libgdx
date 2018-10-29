package com.mygdx.game.test;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.objects.GameObject;
import com.mygdx.game.objects.ObjectInfo;

public class Word extends GameObject{

	ArrayList<Letter> letters;
	float boxSize;
	
	
	public Word(ObjectInfo info, String word, Vector2 position, float boxSize) {
		super(info, new MapProperties());
		transform.setPosition(position);
		this.boxSize = boxSize;
		
		letters = new ArrayList<Letter>();
		
		float offset = 0;
		for(int i = 0; i < word.length(); i ++) {
			if(word.substring(i, i+1).toLowerCase().charAt(0) >= 97 && word.substring(i, i+1).toLowerCase().charAt(0) <= 97 + 26) {
				letters.add(new Letter(info, word.substring(i, i+1).toLowerCase(), position.cpy().add(offset, 0), boxSize));
				getState().putInScene(letters.get(letters.size() - 1));
			}
			offset += boxSize * 6;
		}
	}
	
	public void morphTo(String word) {
		
		float offset = 0;
		for(int i = 0; i < word.length(); i ++) {
				if(i >= letters.size()) {
					if(word.substring(i, i+1).charAt(0) >= 97 && word.substring(i, i+1).charAt(0) <= 97 + 26) {
						letters.add(new Letter(info, word.substring(i, i+1).toLowerCase(), transform.getPosition().cpy().add(offset, 0), boxSize));
						getState().putInScene(letters.get(letters.size() - 1));
					}
				}
				else {
					letters.get(i).morph(word.substring(i, i+1).toLowerCase());
				}
			
			offset += boxSize * 6;
		}
		
		while(nonSpaceSize(word) < letters.size()-1) {
			getState().removeObject(letters.get(letters.size() - 1)); 
			letters.remove(letters.get(letters.size() - 1)); 
		}
	}
	
	public int nonSpaceSize(String word) {
		int cont = 0;
		for(int i = 0; i < word.length(); i ++) {
			if(!word.substring(i, i+1).equals(" ")) {
				cont++;
			}
		}
		return cont;
	}

	public void create() {
		
	}

	public void dispose() {
		
	}

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		
	}

	@Override
	public boolean update(float delta) {
		// TODO Auto-generated method stub
		return false;
	}

}
