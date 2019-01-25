package com.mygdx.game.helper;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RopeJoint;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.mygdx.game.math.Transform;
import com.mygdx.game.states.State;

public class Helper {
	
	static GlyphLayout layout;
	
	static HashMap<String, Interpolation> allPossibles = new HashMap<String, Interpolation>();

	
	static {
		layout = new GlyphLayout();
		
		allPossibles.put("linear", Interpolation.linear);
		allPossibles.put("bounce", Interpolation.bounce);
		allPossibles.put("bounceIn", Interpolation.bounceIn);
		allPossibles.put("bounceOut", Interpolation.bounceOut);
		allPossibles.put("circle", Interpolation.circle);
		allPossibles.put("circleIn", Interpolation.circleIn);
		allPossibles.put("circleOut", Interpolation.circleOut);
		allPossibles.put("elastic", Interpolation.elastic);
		allPossibles.put("elasticIn", Interpolation.elasticIn);
		allPossibles.put("elasticOut", Interpolation.elasticOut);
		allPossibles.put("exp10", Interpolation.exp10);
		allPossibles.put("exp10In", Interpolation.exp10In);
		allPossibles.put("exp10Out", Interpolation.exp10Out);
		allPossibles.put("exp5", Interpolation.exp5);
		allPossibles.put("exp5In", Interpolation.exp5In);
		allPossibles.put("exp5Out", Interpolation.exp5Out);
		allPossibles.put("fade", Interpolation.fade);
		allPossibles.put("pow2", Interpolation.pow2);
		allPossibles.put("pow2In", Interpolation.pow2In);
		allPossibles.put("pow2InInverse", Interpolation.pow2InInverse);
		allPossibles.put("pow2Out", Interpolation.pow2Out);
		allPossibles.put("pow2OutInverse", Interpolation.pow2OutInverse);
		allPossibles.put("pow3", Interpolation.pow3);
		allPossibles.put("pow3In", Interpolation.pow3In);
		allPossibles.put("pow3InInverse", Interpolation.pow3InInverse);
		allPossibles.put("pow3Out", Interpolation.pow3Out);
		allPossibles.put("pow3OutInverse", Interpolation.pow3OutInverse);
		allPossibles.put("pow4", Interpolation.pow4);
		allPossibles.put("pow4In", Interpolation.pow4In);
		allPossibles.put("pow4Out", Interpolation.pow4Out);
		allPossibles.put("pow5", Interpolation.pow5);
		allPossibles.put("pow5In", Interpolation.pow5In);
		allPossibles.put("pow5Out", Interpolation.pow5Out);
		allPossibles.put("sine", Interpolation.sine);
		allPossibles.put("sineIn", Interpolation.sineIn);
		allPossibles.put("sineOut", Interpolation.sineOut);
		allPossibles.put("smooth", Interpolation.smooth);
		allPossibles.put("smoother", Interpolation.smoother);
		allPossibles.put("swing", Interpolation.swing);
		allPossibles.put("swingIn", Interpolation.swingIn);
		allPossibles.put("swingOut", Interpolation.swingOut);
	}
	
	
	public static ArrayList<Vector2> floatArrayToVectorList(float[] points){
		ArrayList<Vector2> ret = new ArrayList<Vector2>();
		
		for(int i = 0; i < points.length/2f; i ++) {
			ret.add(new Vector2(points[i*2], points[i*2 + 1]));
		}
		
		return ret;
		
	}
	
	public static Interpolation makeCustomInterpolation(String jsonPath) {
		
		final JSONArray array = new JSONArray(Gdx.files.internal(jsonPath).readString());
		final JSONObject o = (JSONObject) array.get(array.length() - 1);
		final float length = o.getFloat("x");
		
		return new Interpolation() {
			public float apply(float a) {
				a = Helper.clamp(a, 0.001f, length);
				
				System.out.println(a);
				
				JSONObject ob;
				
				int cont = array.length() - 1;
				do {
					ob = (JSONObject) array.get(cont);
					cont --;					
				}while(ob.getFloat("x") >= a);
				
				cont += 2;
								
				JSONObject bef = (JSONObject) array.get(cont);
				
				float fx = bef.getFloat("x");
				float ix = ob.getFloat("x");
								
				float factor = (a - ix) / (fx - ix);
				
				Vector2 first = new Vector2(ob.getFloat("x"), ob.getFloat("y"));
				Vector2 last = new Vector2(bef.getFloat("x"), bef.getFloat("y"));
				
				return first.interpolate(last, factor, allPossibles.get(ob.getString("interpolation"))).y;
			}
		};
		
	}
	
	public static ArrayList<Vector2> vector2ListHardClone(ArrayList<Vector2> list){
		ArrayList<Vector2> novo = new ArrayList<Vector2>();
		
		for(Vector2 v : list) {
			novo.add(new Vector2(v.x, v.y));
		}
		
		return novo;
	}
	
	public static void setShaderUniformTexture(int index, Texture texture, ShaderProgram shader, String uniformName) {
		
		switch(index) {
		case 0:
			Gdx.graphics.getGL20().glActiveTexture(GL20.GL_TEXTURE0);
			break;
		case 1:
			Gdx.graphics.getGL20().glActiveTexture(GL20.GL_TEXTURE1);
			break;
		case 2:
			Gdx.graphics.getGL20().glActiveTexture(GL20.GL_TEXTURE2);
			break;
		case 3:
			Gdx.graphics.getGL20().glActiveTexture(GL20.GL_TEXTURE3);
			break;
		case 4:
			Gdx.graphics.getGL20().glActiveTexture(GL20.GL_TEXTURE4);
			break;
		case 5:
			Gdx.graphics.getGL20().glActiveTexture(GL20.GL_TEXTURE5);
			break;
		case 6:
			Gdx.graphics.getGL20().glActiveTexture(GL20.GL_TEXTURE6);
			break;
		}
		
		texture.bind(index);
		shader.setUniformi(uniformName, index);
		
		Gdx.graphics.getGL20().glActiveTexture(GL20.GL_TEXTURE0);

	}
	
	public static float random(float min, float max) {
		if(min < max) {
			return (float)(Math.random() * (max - min) + min);
		}
		else {
			new Exception("Min > Max").printStackTrace();
			Gdx.app.exit();
		}
		return -1;
	}
	
	public static Color randomColor(float alpha) {
		return new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), alpha);
	}
	
	public static Color randomColor() {
		return new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), 1);
	}
	
	public static Color randomColor(Color base, float deviation) {
		return base.cpy().lerp(randomColor(), deviation);
	}
	
	public static Color randomSaturated(Color base, float saturation) {
		boolean light = Math.random() < 00f;
		
		if(light) {
			return base.cpy().lerp(Color.WHITE, Helper.random(0, saturation));
		}
		else {
			return base.cpy().lerp(Color.BLACK, Helper.random(0, saturation));
		}
	}
	
	public static void renderRegion(SpriteBatch sb, TextureRegion region, Transform transform, boolean flipX, boolean flipY) {
		renderRegion(sb, region, transform.getPosition(), transform.getAngle(), transform.getScale(), flipX, flipY);
	}
		
	public static void renderRegion(SpriteBatch sb, TextureRegion region, Vector2 position, float angle, Vector2 size,
			boolean flipX, boolean flipY) {
	
		sb.draw(
				region,
				position.x - region.getRegionWidth()/2f,
				position.y - region.getRegionHeight()/2f,
				region.getRegionWidth()/2f,//originx
				region.getRegionHeight()/2f,//originy
				region.getRegionWidth(),//width
				region.getRegionHeight(),//height
				size.x * (flipX ? -1 : 1),//scalex
				size.y * (flipY ? -1 : 1),//scaley
				angle);
		
	}
	
	static public Vector2 xy(Vector3 vector) {
		return new Vector2(vector.x, vector.y);
	}
	
	static public Vector2 xy(Vector3 vector, Vector2 out) {
		out.set(vector.x, vector.y);
		return out;
	}
	
	static public Vector2 getMouseWorldPos(OrthographicCamera camera) {
		return xy(camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)));
	}
	
	static public Vector2 getMouseWorldPos(OrthographicCamera camera, Vector2 out) {
		return xy(camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)), out);
	}
	
	static Vector2 aux1 = new Vector2();
	static Vector2 aux2 = new Vector2();

	
	public static void renderRegionNoCenter(SpriteBatch sb, TextureRegion region, Vector2 position, float angle, Vector2 size,
			boolean flipX, boolean flipY) {
	
		aux1.set(region.getRegionWidth() * size.x,0).rotate(angle);
		aux2.set(0,region.getRegionHeight() * size.y).rotate(angle);
		
		sb.draw(
				region,
				position.x + (flipX ? aux1.x : 0) + (flipY ? aux2.x : 0),
				position.y + (flipX ? aux1.y : 0) + (flipY ? aux2.y : 0),
				0,//originx
				0,//originy
				region.getRegionWidth(),//width
				region.getRegionHeight(),//height
				size.x * (flipX ? -1 : 1),//scalex
				size.y * (flipY ? -1 : 1),//scaley
				angle);
		
		
	}
	
	public static void renderRegion(SpriteBatch sb, TextureRegion region, Vector2 position, float angle, Vector2 size,
			boolean flipX, boolean flipY, Vector2 origin) {
		sb.draw(
				region,
				position.x - origin.x,
				position.y - origin.y,
				origin.x,//originx
				origin.y,//originy
				region.getRegionWidth(),//width
				region.getRegionHeight(),//height
				size.x * (flipX ? -1 : 1),//scalex
				size.y * (flipY ? -1 : 1),//scaley
				angle);
	}

	public static void renderTex(SpriteBatch sb, Texture tex, Transform transform, boolean flipX, boolean flipY) {
		renderTex(sb, tex, transform.getPosition(), transform.getAngle(), transform.getScale(), flipX, flipY);
	}
	
	public static void renderTex(SpriteBatch sb, Texture tex, Vector2 position, Vector2 rectSize) {
		sb.draw(tex,
				position.x,
				position.y,
				0,
				0,
				rectSize.x,
				rectSize.y,
				1,
				1,
				0,
				0,
				0,
				(int)(rectSize.x / (float)tex.getWidth()),
				(int)(rectSize.y / (float)tex.getHeight()),
				false,
				false);
	}
	
	public static void renderTexNoCenter(SpriteBatch sb, Texture texture, Vector2 position, float angle, Vector2 size,
			boolean flipX, boolean flipY) {
			
		sb.draw(
				texture,
				position.x,
				position.y,
				0,//originx
				0,//originy
				texture.getWidth(),//width
				texture.getHeight(),//height
				size.x * (flipX ? -1 : 1),//scalex
				size.y * (flipY ? -1 : 1),//scaley
				angle,
				0,
				0,
				texture.getWidth(),
				texture.getHeight(),
				false, false);
		
	}
	
	public static void renderTex(SpriteBatch sb, Texture tex, Vector2 position, Vector2 rectSize, float scale) {
		sb.draw(tex,
				position.x,
				position.y,
				0,
				0,
				rectSize.x,
				rectSize.y,
				1,
				1,
				0,
				0,
				0,
				(int)(rectSize.x / ((float)tex.getWidth()*scale)),
				(int)(rectSize.y / ((float)tex.getHeight()*scale)),
				false,
				false);
	}
	
	//sem transform
	public static void renderTex(SpriteBatch sb, Texture tex, Vector2 position, float angle, Vector2 size, boolean flipX, boolean flipY) {
		sb.draw(
				tex,
				position.x - tex.getWidth()/2f,
				position.y - tex.getHeight()/2f,
				tex.getWidth()/2f,//originx
				tex.getHeight()/2f,//originy
				tex.getWidth(),//width
				tex.getHeight(),//height
				size.x,//scalex
				size.y,//scaley
				angle,//rotation
				0,//srcx
				0,//srcy
				tex.getWidth(),//srcwidth
				tex.getHeight(),//srcheight
				flipX,
				flipY
				);
	}
	
	public static float tweenToAngle(float inicial, float aFinal, float factor) {
		//TODO: descobrir como fazer tween de angulo
		return 0;
	}
	
	public static void enableBlend() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public static void disableBlend() {
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
	
	public static BitmapFont newFont(String path, int size) {
		FreeTypeFontGenerator ftfg = new FreeTypeFontGenerator(Gdx.files.internal(path));
		FreeTypeFontParameter param;
		param = new FreeTypeFontParameter();
		param.size = size;
		param.color = Color.WHITE;
		return ftfg.generateFont(param);
	}
	
	public static BitmapFont newFont(String path, int size, Color color) {
		FreeTypeFontGenerator ftfg = new FreeTypeFontGenerator(Gdx.files.internal(path));
		FreeTypeFontParameter param;
		param = new FreeTypeFontParameter();
		param.size = size;
		param.color = color;
		return ftfg.generateFont(param);
	}
	
	public static BitmapFont newFont(String path, int size, FreeTypeFontParameter parameters) {
		FreeTypeFontGenerator ftfg = new FreeTypeFontGenerator(Gdx.files.internal(path));
		return ftfg.generateFont(parameters);
	}
	
	public static Vector2 randomUnit() {
		return new Vector2((float)Math.random() *2 - 1, (float)Math.random() * 2 - 1).nor();
	}
	
	public static Matrix4 getDefaultProjection() {
		return new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public static Vector2 newPolarVector(float angle, float magnitude) {
		return new Vector2((float)Math.cos(Math.toRadians(angle)) * magnitude, (float)Math.sin(Math.toRadians(angle)) * magnitude);
	}
	
	public static float clamp(float value, float min, float max) {
		return Math.min(max, Math.max(min, value));
	}

	public static void drawFont(SpriteBatch sb, BitmapFont font, OrthographicCamera camera, String text, Vector2 position) {
		Matrix4 lastProj = sb.getProjectionMatrix().cpy();
		sb.setProjectionMatrix(Helper.getDefaultProjection());
		
		layout.setText(font, text);
		Vector3 rpos = camera.project(new Vector3(position, 0));
		
		font.draw(sb, text, rpos.x - layout.width/2f, rpos.y);
		sb.setProjectionMatrix(lastProj);
	}
	
	public static void drawUIFont(SpriteBatch sb, BitmapFont font, OrthographicCamera camera, String text, Vector2 position) {
		Matrix4 lastProj = sb.getProjectionMatrix().cpy();
		sb.setProjectionMatrix(Helper.getDefaultProjection());
		
		
		font.draw(sb, text, position.x, position.y);
		sb.setProjectionMatrix(lastProj);
	}
	
	public static class Game {
		public static float globalTimer = 0;
		
		static Vector2 mousePosition = new Vector2();
		
		public static Vector2 mousePosition() {
			mousePosition.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			return mousePosition;
		}
		
		public static Vector2 mouseWorldPosition(OrthographicCamera camera) {
			mousePosition.set(Gdx.input.getX(), Gdx.input.getY());
			
			Vector3 mpos = camera.unproject(new Vector3(mousePosition, 0));
			
			mousePosition.set(mpos.x, mpos.y);
			
			return mousePosition;
		}
	}
	
	public static class PhysHelp {
		
		
		//BOXES
		public static Body createDynamicBoxBody(World world, Vector2 position, Vector2 size) {
			
			BodyDef def = new BodyDef();
			def.position.set(position.cpy().scl(1/State.PHYS_SCALE));
			def.type = BodyType.DynamicBody;
			
			Body b =  world.createBody(def);
			
			createBoxFixture(b, Vector2.Zero, size);
			
			return b;
		}

		public static Body createStaticBoxBody(World world, Vector2 position, Vector2 size) {
			
			BodyDef def = new BodyDef();
			def.position.set(position.cpy().scl(1/State.PHYS_SCALE));
			def.type = BodyType.StaticBody;
			
			Body b =  world.createBody(def);
			
			createBoxFixture(b, Vector2.Zero, size);
			
			return b;
		}
		
		public static Body createBoxBody(World world, Vector2 size, BodyDef def) {
			Body b =  world.createBody(def);
			createBoxFixture(b, Vector2.Zero, size);
			return b;
		}
		
		public static Body createCircleBody(World world, float radius, BodyDef def) {
			Body b =  world.createBody(def);
			createCircleFixture(b, Vector2.Zero, radius);
			return b;
		}
		
		//BALLS
		public static Body createDynamicCircleBody(World world, Vector2 position, float radius) {
			
			BodyDef def = new BodyDef();
			def.position.set(position.cpy().scl(1/State.PHYS_SCALE));
			def.type = BodyType.DynamicBody;
			
			Body b =  world.createBody(def);
			
			createCircleFixture(b, Vector2.Zero, radius);
			
			return b;
		}
		
		public static Body createDynamicCircleBody(World world, Vector2 position, float radius, boolean fixedRotation) {
			
			BodyDef def = new BodyDef();
			def.position.set(position.cpy().scl(1/State.PHYS_SCALE));
			def.fixedRotation = fixedRotation;
			def.type = BodyType.DynamicBody;
			
			Body b =  world.createBody(def);
			
			createCircleFixture(b, Vector2.Zero, radius);
			
			return b;
		}
		
		public static Body createStaticCircleBody(World world, Vector2 position, float radius) {
			
			BodyDef def = new BodyDef();
			def.position.set(position.cpy().scl(1/State.PHYS_SCALE));
			def.type = BodyType.StaticBody;
			
			Body b =  world.createBody(def);
			
			createCircleFixture(b, Vector2.Zero, radius);
			
			return b;
		}
		
		public static Body createCircleBody(World world, Vector2 position, float radius, BodyType type) {
			BodyDef def = new BodyDef();
			def.type = type;
			def.position.set(position.cpy().scl(1/State.PHYS_SCALE));
			Body b =  world.createBody(def);
			createCircleFixture(b, Vector2.Zero, radius);
			return b;
		}
		
		//FIXTURES
		
		public static Fixture createCircleFixture(Body body, Vector2 relative, float radius) {
			CircleShape shape = new CircleShape();
			shape.setPosition(relative.cpy().scl(1/State.PHYS_SCALE));
			shape.setRadius(radius / State.PHYS_SCALE);
			return body.createFixture(shape, 1);
		}
		
		public static Fixture createBoxFixture(Body body, Vector2 relative, Vector2 size) {
			PolygonShape shape = new PolygonShape();
			shape.setAsBox(size.x/2 / State.PHYS_SCALE, size.y/2 / State.PHYS_SCALE, relative.cpy().scl(1/State.PHYS_SCALE), 0);
			return body.createFixture(shape, 1);
		}
		
		public static RopeJoint ropeJoinBodies(Body bodyA, Body bodyB, float maxLength) {
			
			
			RopeJointDef def = new RopeJointDef();
			
			def.bodyA = bodyA;
			def.bodyB = bodyB;
			def.localAnchorA.set(bodyA.getLocalCenter());
			def.localAnchorB.set(bodyB.getLocalCenter());
			def.collideConnected = true;

			def.maxLength = maxLength;
			
			RopeJoint joint = (RopeJoint) bodyA.getWorld().createJoint(def);
			
			return joint;
		}

	}
	public static class Position {
		
		public static Vector2 CENTER;
		public static Vector2 CENTERX;
		public static Vector2 CENTERY;
		public static Vector2 TOPLEFT;
		
		static {
			CENTER = new Vector2(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f);
			CENTERX = new Vector2(Gdx.graphics.getWidth()/2f, 0);
			CENTERY = new Vector2(0, Gdx.graphics.getHeight()/2f);
			
			TOPLEFT = new Vector2(0, Gdx.graphics.getHeight());
		}

	}
	
	public static class Size {
		
		public static Vector2 HALFSCREEN;
		public static Vector2 SCREEN;
		public static Vector2 HALFWIDTH;
		public static Vector2 WIDTH;
		public static Vector2 HALFHEIGHT;
		public static Vector2 HEIGHT;
		
		static {
			HALFSCREEN = new Vector2(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f);
			SCREEN = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			
			HALFWIDTH = new Vector2(Gdx.graphics.getWidth()/2f, 0);
			WIDTH = new Vector2(Gdx.graphics.getWidth(), 0);
			
			HALFHEIGHT = new Vector2(0, Gdx.graphics.getHeight()/2f);
			HEIGHT = new Vector2(0, Gdx.graphics.getHeight());
		}

	}

	public static float lerp(float a, float b, float alpha) {
		return (1 - alpha)  * a + alpha * b;
	}

	public static MapObject cloneMapObject(MapObject mo) {
		
		MapObject clone;

		if(mo instanceof CircleMapObject) {
			clone = new CircleMapObject();
			((CircleMapObject) clone).getCircle().set(
					((CircleMapObject) mo).getCircle().x,
					((CircleMapObject) mo).getCircle().y,
					((CircleMapObject) mo).getCircle().radius);
		}
		else if(mo instanceof EllipseMapObject) {
			clone = new EllipseMapObject();
			((EllipseMapObject)clone).getEllipse().set(
					((EllipseMapObject) mo).getEllipse().x,
					((EllipseMapObject) mo).getEllipse().y,
					((EllipseMapObject) mo).getEllipse().width,
					((EllipseMapObject) mo).getEllipse().height);
			
		}
		else if(mo instanceof PolygonMapObject) {
			clone = new PolygonMapObject(((PolygonMapObject) mo).getPolygon().getVertices().clone());
						
			((PolygonMapObject)clone).getPolygon().setPosition(
					((PolygonMapObject) mo).getPolygon().getX(),
					((PolygonMapObject) mo).getPolygon().getY());
			
			((PolygonMapObject)clone).getPolygon().setOrigin(
					((PolygonMapObject) mo).getPolygon().getOriginX(),
					((PolygonMapObject) mo).getPolygon().getOriginY());
			
			((PolygonMapObject)clone).getPolygon().setRotation(
					((PolygonMapObject) mo).getPolygon().getRotation());
			
			((PolygonMapObject)clone).getPolygon().setScale(
					((PolygonMapObject) mo).getPolygon().getScaleX(),
					((PolygonMapObject) mo).getPolygon().getScaleY());
		}
		else if(mo instanceof PolylineMapObject) {
			clone = new PolylineMapObject(((PolylineMapObject) mo).getPolyline().getVertices().clone());
			
			((PolylineMapObject)clone).getPolyline().setPosition(
					((PolylineMapObject) mo).getPolyline().getX(),
					((PolylineMapObject) mo).getPolyline().getY());
			
			((PolylineMapObject)clone).getPolyline().setOrigin(
					((PolylineMapObject) mo).getPolyline().getOriginX(),
					((PolylineMapObject) mo).getPolyline().getOriginY());
			
			((PolylineMapObject)clone).getPolyline().setRotation(
					((PolylineMapObject) mo).getPolyline().getRotation());
			
			((PolylineMapObject)clone).getPolyline().setScale(
					((PolylineMapObject) mo).getPolyline().getScaleX(),
					((PolylineMapObject) mo).getPolyline().getScaleY());
		}
		else if(mo instanceof RectangleMapObject) {
			clone = new RectangleMapObject();
			
			((RectangleMapObject)clone).getRectangle().set(
					((RectangleMapObject) mo).getRectangle().x,
					((RectangleMapObject) mo).getRectangle().y,
					((RectangleMapObject) mo).getRectangle().width,
					((RectangleMapObject) mo).getRectangle().height);
			
		}
		else if(mo instanceof TextureMapObject) {
			clone = new TextureMapObject();
			
			((TextureMapObject)clone).setTextureRegion(((TextureMapObject) mo).getTextureRegion());
		}
		else {
			clone = new MapObject();
		}
		
		clone.setColor(mo.getColor().cpy());
		clone.setName(mo.getName());
		clone.setOpacity(mo.getOpacity());
		
		return clone;
	}

	public static int randomFromIntArray(int[] blends) {
		return blends[(int) (Math.random() * blends.length)];
	}


}
