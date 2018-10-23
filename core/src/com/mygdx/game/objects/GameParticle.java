package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.states.State;

public class GameParticle extends GameObject{


	//User defined
	Texture image;
	String text;
	BitmapFont font;
	Color fontColor;
	Vector2 velocity;
	Vector2 gravity;
	float life;
	float drag;
	
	//Interno	
	float globalTimer;
	GlyphLayout layout;
	
	
	
	private GameParticle(ObjectInfo info, Vector2 position) {
		super(info, new MapProperties());
		transform.setPosition(position.cpy());
		layout = new GlyphLayout();
		life = 1;
		velocity = new Vector2(0, 0);
		gravity = new Vector2(0, 0);
	}

	public GameParticle(ObjectInfo info, Vector2 position, Texture texture) {
		this(info, position, texture, 1);
	}
	
	public GameParticle(ObjectInfo info, Vector2 position, Texture texture, float scale) {
		this(info, position);
		setTexture(texture);
		transform.setScale(new Vector2(scale / State.PHYS_SCALE, scale / State.PHYS_SCALE));
	}
	
	public GameParticle(ObjectInfo info, Vector2 position, Texture texture, float scale, float life) {
		this(info, position, texture, scale);
		this.life = life;
	}
	
	public GameParticle(ObjectInfo info, Vector2 position, String text, BitmapFont font, Color color) {
		this(info, position);
		setText(text);
		setTextLayout(font, color);
	}
	
	public GameParticle(ObjectInfo info, Vector2 position, String text, BitmapFont font, Color color, float life) {
		this(info, position, text, font, color);
		this.life = life;
	}
	
	public GameParticle(ObjectInfo info, Vector2 position, String text, BitmapFont font) {
		this(info, position, text, font, Color.WHITE);
	}
	
	public GameParticle(ObjectInfo info, Vector2 position, String text, BitmapFont font, float life) {
		this(info, position, text, font, Color.WHITE);
		this.life = life;
	}
	
	public void setTexture(Texture texture) {
		image = texture;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setTextLayout(BitmapFont font, Color color) {
		this.font = font;
		this.fontColor = color;
	}

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		Helper.enableBlend();
		sb.setColor(1, 1, 1, Math.min(1, life - globalTimer));
		
		if(image != null) {
			renderTexture(sb, image);
		}

		if(text != null) {
			font.setColor(fontColor.r, fontColor.g, fontColor.b, Math.min(1, life - globalTimer));
			Helper.drawFont(sb, font, camera, text, transform.getPosition());
			font.setColor(1, 1, 1, 1);

		}
			/*
			sb.setProjectionMatrix(Helper.getDefaultProjection());
			layout.setText(font, text);
			
			Vector3 rpos = camera.project(new Vector3(transform.getPosition(), 0));
			font.draw(sb, text, rpos.x - layout.width/2f, rpos.y);
			sb.setProjectionMatrix(camera.combined);
		}
		*/
		
		sb.setColor(1, 1, 1, 1);
		Helper.disableBlend();
	}

	public boolean update(float delta) {
		globalTimer += delta;
		
		if(life - globalTimer <= 0) {
			return true;
		}
		
	
		
		transform.getPosition().add(velocity);
		velocity.add(gravity);
		
		velocity.scl((float) Math.exp(-drag));
		
		
		return false;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public Vector2 getGravity() {
		return gravity;
	}

	public void setGravity(Vector2 gravity) {
		this.gravity = gravity;
	}

	public float getLife() {
		return life;
	}

	public void setLife(float life) {
		this.life = life;
	}

	public float getDrag() {
		return drag;
	}

	public void setDrag(float drag) {
		this.drag = drag;
	}

	public Color getFontColor() {
		return fontColor;
	}

	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
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
