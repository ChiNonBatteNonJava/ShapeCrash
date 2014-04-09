package com.chinonbattenonjava.saproject;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

public class MyRenderer implements GLSurfaceView.Renderer {

	@Override
	public void onDrawFrame(GL10 unused) {
		//clear color buffer
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		//clear screen to black
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
	}

}
