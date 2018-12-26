package com.mygdx.game.tiled;

import com.badlogic.gdx.maps.MapObject;

public abstract class TmxInstancedKeyword {
	
	String keyword;
	
	public TmxInstancedKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	public abstract Object getObject(MapObject mo);

	public String getKeyword() {
		return keyword;
	}

}
