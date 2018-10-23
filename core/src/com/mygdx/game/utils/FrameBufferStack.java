package com.mygdx.game.utils;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class FrameBufferStack {

	static ArrayList<FrameBuffer> frame;

	static Texture lastTexture;
	
	public static void begin(FrameBuffer frame2) {
		if(frame == null) {
			frame = new ArrayList<FrameBuffer>();
		}
		frame.add(frame2);
		if(frame.size() > 1) {
			frame.get(frame.size() - 2).end();
		}
		frame.get(frame.size() - 1).begin();
	}
	
	public static void end() {
		frame.get(frame.size() - 1).end();
		lastTexture = frame.get(frame.size() - 1).getColorBufferTexture();
		frame.remove(frame.size() - 1);
		if(frame.size() > 0) {
			frame.get(frame.size() - 1).begin();
		}
	}
	
	public static Texture getTexture() {
		return lastTexture;
	}
	
}
