package com.mygdx.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Data;
import com.brashmonkey.spriter.Drawer;
import com.brashmonkey.spriter.Entity;
import com.brashmonkey.spriter.Loader;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.PlayerTweener;
import com.brashmonkey.spriter.SCMLReader;
import com.mygdx.game.states.State;

public class SpriterAnimation extends GameObject{

	Player player;
	Drawer<?> drawer;
	Loader<Sprite> loader;
	
	public SpriterAnimation(ObjectInfo info, MapProperties properties) {
		super(info, properties);
		
		FileHandle handle = Gdx.files.internal(get("path", String.class));
		Data data = new SCMLReader(handle.read()).getData();

		loader = new LibGdxLoader(data);
		loader.load(handle.file());
		
		player = new Player(data.getEntity(0));
		
		transform.setPosition(new Vector2(
				(get("x", Float.class, 0f)) / State.PHYS_SCALE,
				(get("y", Float.class, 0f)) / State.PHYS_SCALE));

	}
	
	public PlayerTweener createInterpolatedAnimation(String animationName1, String animationName2, float alpha) {
		
		PlayerTweener tweener = new PlayerTweener(player.getEntity());
		
		Player p1 = new Player(player.getEntity());
		p1.setAnimation(animationName1);
		Player p2 = new Player(player.getEntity());
		p2.setAnimation(animationName2);
		
		tweener.setPlayers(p1, p2);
		tweener.setWeight(alpha);
		
		return tweener;
	}
	
	public SpriterAnimation(ObjectInfo info, String path, Vector2 position) {
		super(info, new MapProperties());
		
		FileHandle handle = Gdx.files.internal(path);
		Data data = new SCMLReader(handle.read()).getData();

		loader = new LibGdxLoader(data);
		loader.load(handle.file());
		
		player = new Player(data.getEntity(0));
		
		transform.setPosition(new Vector2(
				position.x,
				position.y));
	}
	
	public Animation getAnimation(String animationName) {
		return player.getEntity().getAnimation(animationName);
	}
	
	public void setAnimation(String animationName) {
		player.setAnimation(animationName);
	}
	
	public void setAnimation(Animation animation) {
		player.setAnimation(animation);
	}
	
	public void flip(boolean x, boolean y) {
		
		if(x && player.flippedX() == 1) {
			player.flipX();
		}
		else if(!x && player.flippedX() == -1) {
			player.flipX();
		}
		
		
		if(y && player.flippedY() == 1) {
			player.flipY();
		}
		else if(!y && player.flippedY() == -1) {
			player.flipY();
		}
	}

	public void create() {
		
	}

	public void dispose() {
		
	}

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		if(drawer == null) {
			drawer = new LibGdxDrawer(loader, sb, sr);
		}
		
		drawer.draw(player);
	}

	public boolean update(float delta) {
		
		player.setPosition(transform.getPosition().x, transform.getPosition().y);
		player.setScale(transform.getScale().x / State.PHYS_SCALE);
		player.setAngle(transform.getAngle());
		player.update();
		return false;
	}

	public Entity getEntity() {
		return player.getEntity();
	}
	
	public void setPlayer(Player anim) {
		player = anim;
		player.setPosition(transform.getPosition().x, transform.getPosition().y);
		player.setScale(transform.getScale().x / State.PHYS_SCALE);
		player.setAngle(transform.getAngle());
		player.update();
	}

	public Animation getAnimation() {
		return player.getAnimation();
	}

	public Player getPlayer() {
		return player;
	}

}
