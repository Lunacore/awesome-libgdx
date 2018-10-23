package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;

public class ScreenSize {
	
	static int width = 1920;
	static int height = 1080;
	static boolean dynamicScreen = false;
	
	public static int getWidth() {
		if(!dynamicScreen)
			return width;
		else
			return Gdx.graphics.getWidth();
	}
	
	public static int getHeight() {
		if(!dynamicScreen)
			return height;
		else
			return Gdx.graphics.getHeight();
		}
	
	public static void setScreenSize(int width, int height) {
		ScreenSize.width = width;
		ScreenSize.height = height;
	}
	
	public static void setDynamicScreen(boolean dynamicScreen) {
		ScreenSize.dynamicScreen = dynamicScreen;
	}

}
