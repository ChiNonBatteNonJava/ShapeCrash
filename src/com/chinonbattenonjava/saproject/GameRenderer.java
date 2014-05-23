package com.chinonbattenonjava.saproject;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import Physic.PhysicCar;
import Physic.PhysicsWorld;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.badlogic.gdx.math.Vector3;

public class GameRenderer implements GLSurfaceView.Renderer {
	private static final float NANOS_PER_SECONDS = 1000000000.0f;
	private float delta;
	private GameGUI gameGUI;
	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		// This method will be called only once
		// Landscape mode is fixed
		GLES20.glViewport(0, 0, width, height);

		float ratio = (float) width / height;
		GameResourceManager.getInstance().setAspectRatio(ratio);

		GameState.getInstance().getCamera("MainCam").setFrustum(0.10f, 260.0f, ratio);
	}

	Car player1;
	Terrain t;

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		gameGUI=new GameGUI();
		
		// logic initialization
		delta = 0.000001f; // small value in case of division by delta
		
		GameState.getInstance().getCamera("MainCam").setEye(2.0f, 3.0f, 0.0f);
		GameState.getInstance().getCamera("MainCam").setTarget(0.0f, 0.0f, 0.0f);
		GameState.getInstance().getCamera("MainCam").setUp(0.0f, 1.0f, 0.0f);
		GameState.getInstance().getCamera("MainCam").updateViewMatrix();
		
		for(IUpdatableGameComponent drawable : GameState.getInstance().getUpdatables()){
			
			drawable.initPhysics();
		}
		
		
		player1 = GameResourceManager.getInstance().getCar(GameResourceManager.getInstance().getPlayerName());
		
		
		
		t = new Terrain();

		//Client x=new Client(player1);
		// rendSDFer initia lization

		GameState.getInstance().setRendererState(RendererState.READY);

		GameResourceManager.getInstance().loadShaders();
		GameResourceManager.getInstance().compileShaders();
		GameResourceManager.getInstance().loadShaderPrograms();
		GameResourceManager.getInstance().releaseShaders();

		// clear screen to white
		GLES20.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glDepthFunc(GLES20.GL_LEQUAL);
		GLES20.glDepthMask(true);
		GLES20.glClearDepthf(1.0f);
		
		gameGUI.AddElement(player1);
		// TODO load textures
	}

	long startTime = 0;

	@Override
	public void onDrawFrame(GL10 unused) {
		
		// compute delta
		delta = (System.nanoTime() - startTime) / NANOS_PER_SECONDS;
		if(startTime == 0){
			delta=1.0f/30.0f;
		}
		startTime = System.nanoTime();
		
		// update logic  
		
		Vector3 carPos = player1.getCarPos();
		Vector3 camPos=new Vector3();
		PhysicCar c=player1.getCar();
		if(c!=null){
			camPos=player1.getCar().getVectorForward().mul(15);
			Log.i("err",""+c);
		}
		

	//	GameState.getInstance().getCamera("MainCam").setEye(carPos.x-camPos.x,carPos.y-camPos.y+6,carPos.z-camPos.z);
		GameState.getInstance().getCamera("MainCam").setEye(carPos.x,carPos.y+45,carPos.z);

		GameState.getInstance().getCamera("MainCam").setTarget(0.01f+carPos.x, carPos.y, carPos.z);
		GameState.getInstance().getCamera("MainCam").updateViewMatrix();

		
		for (IUpdatableGameComponent updatable : GameState.getInstance()
				.getUpdatables()) {
			updatable.update(delta);
		}

		// PhysicsUpdate

		PhysicsWorld.instance("MainWorld").update(delta);

		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		
		for (IDrawableGameComponent drawable : GameState.getInstance()
				.getDrawables()) {
			drawable.getPainter().draw();
		}
		gameGUI.Draw();
		
		
	}
}
