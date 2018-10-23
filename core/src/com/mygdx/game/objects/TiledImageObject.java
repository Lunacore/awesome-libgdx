package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.states.State;

public class TiledImageObject extends GameObject{
	
	TiledMapTileMapObject imgObj;
	
	public TiledImageObject(ObjectInfo info, TiledMapTileMapObject imgObj) {
		super(info, new MapProperties());
		this.imgObj = imgObj;
	}

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		if(imgObj.getProperties().get("render") == null || imgObj.getProperties().get("render", Boolean.class) ) {
			Helper.renderRegion(
					sb,
					imgObj.getTile().getTextureRegion(),
					new Vector2(imgObj.getX(), imgObj.getY()).cpy().scl(getScale()/State.PHYS_SCALE),
					360 - imgObj.getRotation(),
					new Vector2(getScale() * imgObj.getScaleX() / State.PHYS_SCALE, getScale() * imgObj.getScaleY() / State.PHYS_SCALE),
					imgObj.isFlipHorizontally(),
					imgObj.isFlipVertically(),
					new Vector2(getScale() * imgObj.getOriginX() / State.PHYS_SCALE, getScale() * imgObj.getOriginY() / State.PHYS_SCALE)
					);
	}
		
	}

	@Override
	public boolean update(float delta) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}

}
