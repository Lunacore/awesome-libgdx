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
import com.mygdx.game.helper.Helper;
import com.mygdx.game.objects.KeyMapper.Device;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.objects.PlatformPlayer;
import com.mygdx.game.objects.SpriterAnimation;
import com.mygdx.game.structs.Transform;

public class Personagem extends PlatformPlayer{

	SpriterAnimation animation;
		
	float endVelocityX = 0;

	protected boolean requestJump;
	
	PersonagemAnimationController controller;
	
	BitmapFont font;
	
	public Personagem(ObjectInfo info, MapProperties properties) {
		super(info, properties);
		
		animation = new SpriterAnimation(info, properties);
		getState().putInScene(animation);
		
		setJumpStrength(10);
		setSpeed(5);
		
		body.setUserData(this);

		controller = new PersonagemAnimationController(this);
		
		font = Helper.newFont("Allan-Bold.ttf", 28);
	}

	public void create() {
		
	}

	public void dispose() {
		
	}

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {

		sb.end();
		
		sr.begin(ShapeType.Line);
		sr.setColor(Color.RED);
			sr.line(body.getWorldCenter(), body.getWorldCenter().cpy().add(0, -distanceToFloor));
		sr.end();
		
		sb.begin();
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
		animation.setScale(new Vector2(1/4f, 1/4f));
		animation.flip(direction == -1, false);
		
		endVelocityX += (Math.abs(body.getLinearVelocity().x) - endVelocityX) / 15f;
		
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
		
		controller.update(delta);
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
