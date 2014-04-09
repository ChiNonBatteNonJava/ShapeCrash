package com.chinonbattenonjava.saproject;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class MyGLSurfaceView extends GLSurfaceView {

	public MyGLSurfaceView(Context context) {
		super(context);
		
		setEGLContextClientVersion(2);
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		setRenderer(new MyRenderer());
		
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}

}
