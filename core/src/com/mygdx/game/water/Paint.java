package com.mygdx.game.water;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entities.GameObject;
import com.mygdx.game.entities.ObjectInfo;
import com.mygdx.game.helper.Helper;

public class Paint extends GameObject{

	ShaderProgram shader;
	ArrayList<Blob> blobs;
	
	int MAX_BLOBS = 256;
	
	float[] toPassPosition;
	float[] toPassColor;
	
	Texture tex;
	
	
	public Paint(ObjectInfo info) {
		super(info);
		
		ShaderProgram.pedantic = false;
		
		blobs = new ArrayList<Blob>();

		toPassPosition = new float[MAX_BLOBS * 3];
		toPassColor = new float[MAX_BLOBS * 3];
		
		shader = new ShaderProgram(Gdx.files.internal("shaders/default.vs"), Gdx.files.internal("shaders/metaball.fs"));
		
		if(shader.getLog().length() > 0) {
			System.err.println(shader.getLog());
			Gdx.app.exit();
		}
		
		tex = new Texture("badlogic.jpg");
		
	}
	
	public void create() {
		
	}

	public void dispose() {
		
	}

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		sb.end();
		Helper.enableBlend();
		
		shader.begin();
		
		shader.setUniform3fv("metaballs", toPassPosition, 0, toPassPosition.length);
		shader.setUniform3fv("metacolors", toPassColor, 0, toPassColor.length);
		shader.setUniformf("cameraPosition", getCamera().position.x, Gdx.graphics.getHeight() - getCamera().position.y);
		shader.setUniformf("viewport", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		shader.setUniformf("cameraZoom", getCamera().zoom);
		
		shader.setUniformf("threshold", 1500f);
		
		sb.setProjectionMatrix(Helper.getDefaultProjection());
		
		sb.setShader(shader);
		sb.begin();
		
		sb.draw(tex, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		sb.end();
		sb.setShader(null);
		shader.end();
		
		clearArray();
		
		sb.setProjectionMatrix(getCamera().combined);
		sb.begin();
	}
	
	public void clearArray() {
		for(int i = 0; i < toPassPosition.length; i ++) {
			toPassPosition[i] = 0;
			toPassColor[i] = 0;
		}
	}
	
	public void updateArray() {
		for(int i = blobs.size() - 1; i > 0; i --) {
			
			if(!blobs.get(i).deleted) {
			
				if(blobs.get(i).getBody() != null) {
					toPassPosition[i*3] = blobs.get(i).getPosition().x;
					toPassPosition[i*3+1] = Gdx.graphics.getHeight() - blobs.get(i).getPosition().y;
					toPassPosition[i*3+2] = blobs.get(i).getRadius();
					
					toPassColor[i*3] = blobs.get(i).getColor().r;
					toPassColor[i*3+1] = blobs.get(i).getColor().g;
					toPassColor[i*3+2] = blobs.get(i).getColor().b;
				}
			}
			else {
				blobs.remove(i);
			}
		}
	}
	
	public boolean update(float delta) {
		updateArray();

		while(blobs.size() > MAX_BLOBS) {
			blobs.remove(0);
		}
		return false;
	}

	public void addBlob(Blob blob) {
		if(blobs.size() < MAX_BLOBS) {
			blobs.add(blob);
			getState().putInScene(blob);
		}
		else {
			blob.dispose();
		}
	}
	
}
