package com.mygdx.game.test;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.objects.TiledImageObject;

public class Animacao extends TiledImageObject{

	public Animacao(ObjectInfo info, MapProperties properties) {
		super(info, properties);
	}

	public void create() {
		
	}

	public void dispose() {
		
	}

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		super.render(sb, sr, camera);
	}

	public boolean update(float delta) {
		return false;
	}

}
