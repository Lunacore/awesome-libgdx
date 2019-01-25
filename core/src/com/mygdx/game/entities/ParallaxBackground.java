package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.states.State;

public class ParallaxBackground extends GameObject{

	Texture texture;
	float parallax = State.PHYS_SCALE;
	float zoom = 1;
	float alpha = 1f;
	
	public ParallaxBackground(ObjectInfo info, MapProperties properties) {
		super(info, properties);
		
		texture = new Texture(get("imgPath", String.class));
		texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	}
	
	public ParallaxBackground(ObjectInfo info, String imagePath) {
		super(info, new MapProperties());
		texture = new Texture(imagePath);
		texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	}

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		Helper.enableBlend();
		
		sb.setProjectionMatrix(Helper.getDefaultProjection());
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		sb.setColor(1, 1, 1, alpha);
		
		sb.draw(
				texture,
				0,
				0,
				0,//originx
				0,//originy
				w,//width
				h,//height
				1,//scalex
				1,//scaley
				0,//rotation
				(int)(transform.getPosition().x - (w / transform.getScale().x)/2f),//srcx
				(int)(-transform.getPosition().y - (h / transform.getScale().y)/2f),//srcy
				(int)(w / transform.getScale().x),//srcwidth
				(int)(h / transform.getScale().y),//srcheight
				false,
				false
				);
		
		sb.setColor(1, 1, 1, 1);
		
		sb.setProjectionMatrix(camera.combined);
	}
	
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public boolean update(float delta) {
		if(getCamera() != null)
		transform.setPosition(new Vector2(getCamera().position.x, getCamera().position.y));
		
		setScale(new Vector2(1 / zoom / State.PHYS_SCALE, 1 / zoom / State.PHYS_SCALE));

		return false;
	}
	
	@Override
	public void setPosition(Vector2 position) {
		super.setPosition(position.cpy().scl(parallax));
	}
	
	public void setParallax(float parallax) {
		this.parallax = parallax / zoom;
	}

	public void setZoom(float zoom) {
		this.zoom = zoom;
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
