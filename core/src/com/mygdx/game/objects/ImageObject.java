package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.states.State;

public class ImageObject extends GameObject{

	Texture image;
	
	public ImageObject(ObjectInfo info, MapProperties properties) {
		super(info, properties);
		
		image = new Texture(get("image", String.class));
		transform.setScale(new Vector2(1/State.PHYS_SCALE, 1/State.PHYS_SCALE));
		transform.setPosition(new Vector2(get("x", Float.class) + get("width", Float.class)/2f, get("y", Float.class) + get("height", Float.class)/2f));
	}
	
	public ImageObject(ObjectInfo info, String imgPath) {
		super(info, new MapProperties());
		image = new Texture(imgPath);
		transform.setScale(new Vector2(1/State.PHYS_SCALE, 1/State.PHYS_SCALE));
	}
	
	public ImageObject(ObjectInfo info, Texture image) {
		super(info, new MapProperties());
		this.image = image;
		transform.setScale(new Vector2(1/State.PHYS_SCALE, 1/State.PHYS_SCALE));

	}

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		renderTexture(sb, image);
	}

	public boolean update(float delta) {
		return false;
	}

	public Texture getImage() {
		return image;
	}

	public void setImage(Texture image) {
		this.image = image;
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
