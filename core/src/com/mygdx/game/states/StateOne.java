package com.mygdx.game.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.helper.Helper.Position;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.test.Word;

public class StateOne extends State{

	Word word;
	
	String[] palavras = new String[]{"Meme yahoo", "monark mito", "isso", "joinha massa", "teste"};
	
	public StateOne(StateManager manager) {
		super(manager);
	}
	
	public void create() {
		word = new Word(new ObjectInfo(this, 0, 1f), "Abacate", Position.CENTERY.cpy().add(50, 0), 10);
		putInScene(word);
	}

	public void render(SpriteBatch sb) {
		
	}

	public void update(float delta) {		

	}

	public void dispose() {
		
	}
	
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		word.morphTo(palavras[(int)(Math.random() * palavras.length)]);
		return super.touchDown(screenX, screenY, pointer, button);
	}
}
