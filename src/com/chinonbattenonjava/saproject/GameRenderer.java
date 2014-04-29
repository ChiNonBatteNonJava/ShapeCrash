package com.chinonbattenonjava.saproject;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

public class GameRenderer implements GLSurfaceView.Renderer {
	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		// This method will be called only once
		// Landscape mode is fixed
		GLES20.glViewport(0, 0, width, height);
		
		float ratio = (float)width/height;
		
		GameState.getInstance().getCamera("MainCam").setFrustum(1.0f, 10.0f, ratio);
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		// logic initialization
		GameState.getInstance().getCamera("MainCam").setEye(0.0f, 0.0f, -3.0f);
		GameState.getInstance().getCamera("MainCam").setTarget(0.0f, 0.0f, 0.0f);
		GameState.getInstance().getCamera("MainCam").setUp(0.0f, 1.0f, 0.0f);
		GameState.getInstance().getCamera("MainCam").updateViewMatrix();
		
		Car player1 = new Car();
		
		// render initialization
		GameState.getInstance().setRendererState(RendererState.READY);
		
		GameResourceManager.getInstance().loadShaders();
		GameResourceManager.getInstance().compileShaders();
		
		//clear screen to white
		GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		
		// TODO load textures
	}
	
	@Override
	public void onDrawFrame(GL10 unused) {
		// update logic
		GameState.getInstance().getCamera("MainCam").updateViewMatrix();
		
		for (IUpdatableGameComponent updatable : GameState.getInstance().getUpdatables())
		{
			updatable.update(0.0f);
		}
		
		// draw logic
		//clear color buffer
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		
		for (IDrawableGameComponent drawable : GameState.getInstance().getDrawables())
		{
			drawable.getPainter().draw();
		}
	}
}
