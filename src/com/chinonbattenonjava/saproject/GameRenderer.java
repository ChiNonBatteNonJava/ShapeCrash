package com.chinonbattenonjava.saproject;

import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import Physic.PhysicsWorld;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.badlogic.gdx.math.Vector3;

public class GameRenderer implements GLSurfaceView.Renderer {
	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		// This method will be called only once
		// Landscape mode is fixed
		GLES20.glViewport(0, 0, width, height);
		
		float ratio = (float)width/height;
		
		GameState.getInstance().getCamera("MainCam").setFrustum(0.10f,40.0f, ratio);
	}
	Car player1;
	Terrain t;
	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		// logic initialization
		GameState.getInstance().getCamera("MainCam").setEye(2.0f, 3.0f, 0.0f);
		GameState.getInstance().getCamera("MainCam").setTarget(0.0f, 0.0f, 0.0f);
		GameState.getInstance().getCamera("MainCam").setUp(0.0f, 1.0f, 0.0f);
		GameState.getInstance().getCamera("MainCam").updateViewMatrix();

				
	
		
		
		player1 = new Car();
		
		t =new Terrain();
		
		// render initialization
		GameState.getInstance().setRendererState(RendererState.READY);
		
		GameResourceManager.getInstance().loadShaders();
		GameResourceManager.getInstance().compileShaders();
		GameResourceManager.getInstance().loadShaderPrograms();
		GameResourceManager.getInstance().releaseShaders();
		
		//clear screen to white
		GLES20.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glDepthFunc(GLES20.GL_LEQUAL);
		GLES20.glDepthMask(true);
		GLES20.glClearDepthf(1.0f);
		// TODO load textures
	}
	long old=0;
	@Override
	public void onDrawFrame(GL10 unused) {
		// update logic
		//long heapSize = Runtime.getRuntime().totalMemory(); 
		//Log.i("heap",this.getClass().getName()+" c "+heapSize);
	
//		old=Runtime.getRuntime().totalMemory();
		//Log.i("alloc1",""+Runtime.getRuntime().totalMemory());
		
		
		
		Vector3 carPos=player1.getCarPos();
	
		
		GameState.getInstance().getCamera("MainCam").setEye(carPos.x+0.01f, carPos.y+35, carPos.z);
		GameState.getInstance().getCamera("MainCam").setTarget(carPos.x, carPos.y, carPos.z);
		GameState.getInstance().getCamera("MainCam").updateViewMatrix();
		
		
		for (IUpdatableGameComponent updatable : GameState.getInstance().getUpdatables())
		{
			updatable.update(0.0f);
		}
//		if((Runtime.getRuntime().totalMemory()-old)!=0){
//			Log.i("alloc2",""+(Runtime.getRuntime().totalMemory()-old));
//			
//		}
//		old=Runtime.getRuntime().totalMemory();
		
		//PhysicsUpdate 
		
		PhysicsWorld.instance("MainWorld").update();
		
//		if((Runtime.getRuntime().totalMemory()-old)!=0){
//			Log.i("alloc3",""+(Runtime.getRuntime().totalMemory()-old));
//			
//		}
//		old=Runtime.getRuntime().totalMemory();
		// draw logic
		//clear color buffer
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		
		for (IDrawableGameComponent drawable : GameState.getInstance().getDrawables())
		{
			drawable.getPainter().draw();
		}
//		if((Runtime.getRuntime().totalMemory()-old)!=0){
//			Log.i("alloc4",""+(Runtime.getRuntime().totalMemory()-old));
//			
//		}
		
		//heapSize = Runtime.getRuntime().totalMemory(); 
		//Log.i("heap",this.getClass().getName()+" c1 "+heapSize);
	}
}
