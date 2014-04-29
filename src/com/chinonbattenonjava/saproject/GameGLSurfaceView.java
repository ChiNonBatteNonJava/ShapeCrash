package com.chinonbattenonjava.saproject;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class GameGLSurfaceView extends GLSurfaceView {

	public GameGLSurfaceView(Context context) {
		super(context);
		
		GameResourceManager.getInstance().bindAndroidResources(getResources());
		
		setEGLContextClientVersion(2);
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		setRenderer(new GameRenderer());
		
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}

}
