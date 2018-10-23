package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationLoader {
	
	public static Animation<TextureRegion> load(String sheetPath, int cellWidth, int cellHeight, int startIndex, int endIndex, float duration){
		
		Texture spritesheet = new Texture(sheetPath);
		
		TextureRegion[] regions = new TextureRegion[endIndex - startIndex];
		
		int cont = 0;
		int frameCount = 0;
		for(int y = 0; y < spritesheet.getHeight() / cellHeight; y ++) {
			for(int x = 0; x < spritesheet.getWidth() / cellWidth; x ++) {

				if(cont >= startIndex && cont < endIndex) {
					regions[frameCount] = new TextureRegion(spritesheet, x*cellWidth, y*cellHeight, cellWidth, cellHeight);
					frameCount++;
				}
				
				cont++;
			}
		}
		
		Animation<TextureRegion> animation = new Animation<TextureRegion>(duration, regions);
		
		return animation;
	}

}
