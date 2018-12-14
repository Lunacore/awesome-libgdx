package com.mygdx.game.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.PlayerTweener;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.objects.KeyMapper.Device;
import com.mygdx.game.states.State;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.objects.PlatformPlayer;
import com.mygdx.game.objects.SpriterAnimation;
import com.mygdx.game.structs.Transform;

public class Personagem extends PlatformPlayer{

	SpriterAnimation animation;
	float endVelocityX = 0;
	protected boolean requestJump;
	PlayerTweener idle_run;
	
	//PersonagemAnimationController controller;
	
	public Personagem(ObjectInfo info, MapProperties properties) {
		super(info, properties);
				
		animation = new SpriterAnimation(info, "spriter/luna/luna.scml", new Vector2(get("x", Float.class) / State.PHYS_SCALE, get("y", Float.class) / State.PHYS_SCALE));
		getState().putInScene(animation);
		setJumpStrength(10);
		setSpeed(5);
		body.setUserData(this);
		idle_run = animation.createInterpolatedAnimation("Idle", "Run", 0);
		animation.setPlayer(idle_run);
		//controller = new PersonagemAnimationController(this);
	}
	
	public SpriterAnimation getSpriterAnimation() {
		return animation;
	}

	public void create() {
		
	}

	public void dispose() {
		
	}

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {

	}
	
	float distanceToFloor = 0;

	@Override
	public void setOnFloor(boolean b) {
		super.setOnFloor(b);
	}

	public boolean update(float delta) {
		super.update(delta);
		animation.getTransform().setPosition(
				body.getWorldCenter().x,
				body.getWorldCenter().y - 0.5f);
		animation.setScale(new Vector2(1/7f, 1/7f));
		animation.flip(direction == -1, false);
		
		endVelocityX += (Math.abs(body.getLinearVelocity().x) - endVelocityX) / 15f;
		idle_run.setWeight(endVelocityX / speed);
		
		getState().getWorld().rayCast(new RayCastCallback() {
			
			float minimal = 100;
			
			public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
				if(!(fixture.getBody().getUserData() instanceof Personagem)){
					if(body.getWorldCenter().cpy().sub(point).y < minimal) {
						minimal = body.getWorldCenter().cpy().sub(point).y;
						distanceToFloor = minimal;
					}
					return -1;
				}
				return -1;
			}
			
		}, body.getWorldCenter(), body.getWorldCenter().cpy().add(0, -10));
		
		//controller.update(delta);
		//animation.setPlayer(controller.getCurrentPlayer());
		return false;
	}
	
	public void jump() {
		if(getRemainingJumps() > 0) {
			body.setLinearVelocity(body.getLinearVelocity().x, 0);
			body.applyLinearImpulse(new Vector2(0, body.getMass() * jumpStrength), body.getWorldCenter(), true);
			setJumps(getRemainingJumps() - 1);
		}
		requestJump = false;
	}
	
	@Override
	public void inputIn(Device device, String mapName) {
		if(mapName.equals("Left")) {
			left = true;
			direction = -1;
		}
		if(mapName.equals("Right")) {
			right = true;
			direction = 1;
		}
		if(mapName.equals("Jump")) {
			if(getRemainingJumps() > 0) {
				requestJump = true;
			}
		}
	}

	public void setPlayer(Player anim) {
		animation.setPlayer(anim);
		animation.flip(direction == -1, false);
	}

	public Animation getAnimation() {
		return animation.getAnimation();
	}

	public Player getPlayer() {
		return animation.getPlayer();
	}

	public float getDistanceToFloor() {
		return distanceToFloor;
	}

}
