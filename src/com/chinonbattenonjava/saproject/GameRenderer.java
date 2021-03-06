package com.chinonbattenonjava.saproject;

import java.util.ArrayList;
import java.util.Iterator;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import Physic.PhysicCar;
import Physic.PhysicsWorld;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.badlogic.gdx.math.Vector2;
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

		GameState.getInstance().getCamera("MainCam").setFrustum(0.10f, 500.0f, ratio);
	}

	Car player1;
	Terrain t;
	
	ArrayList<GUIQuad> guiQuads;

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		gameGUI=new GameGUI();
		guiQuads = new ArrayList<GUIQuad>();
		
		// logic initialization
		delta = 0.000001f; // small value in case of division by delta
		
		GameState.getInstance().getCamera("MainCam").setEye(2.0f, 3.0f, 0.0f);
		GameState.getInstance().getCamera("MainCam").setTarget(0.0f, 0.0f, 0.0f);
		GameState.getInstance().getCamera("MainCam").setUp(0.0f, 1.0f, 0.0f);
		GameState.getInstance().getCamera("MainCam").updateViewMatrix();
		
		GameState.getInstance().getLight("MainLight").setPosition(1, 1, 1);
		GameState.getInstance().getLight("MainLight").updateEyeSpacePosition();
		
		for(IUpdatableGameComponent drawable : GameState.getInstance().getUpdatables()){
			
			drawable.initPhysics();
		}
		
		
		player1 = GameResourceManager.getInstance().getCar(GameResourceManager.getInstance().getPlayerName());
		
		
		t = new Terrain();
		GameState.getInstance().createCheckpoint();

		//Client x=new Client(player1);
		// rendSDFer initia lization

		GameResourceManager.getInstance().loadShaders();
		GameResourceManager.getInstance().compileShaders();
		GameResourceManager.getInstance().loadShaderPrograms();
		GameResourceManager.getInstance().releaseShaders();

		// clear screen to white
		GLES20.glClearColor(0.19f, 0.65f, 0.85f, 1.0f);
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glDepthFunc(GLES20.GL_LEQUAL);
		GLES20.glDepthMask(true);
		GLES20.glClearDepthf(1.0f);
		
		gameGUI.AddElement(player1);
		// TODO load textures
		
		guiQuads.add(new GUIQuad(new Vector2(740, 360), new Vector2(1, 1),  "1.png"));
		
		
		
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
		}
		
		if(!GameState.getInstance().camState()){
			GameState.getInstance().getCamera("MainCam").setEye(carPos.x-camPos.x,carPos.y-camPos.y+6,carPos.z-camPos.z);
		}else{
			GameState.getInstance().getCamera("MainCam").setEye(carPos.x,carPos.y+45,carPos.z);
		}

		GameState.getInstance().getCamera("MainCam").setTarget(0.01f+carPos.x, carPos.y, carPos.z);
		GameState.getInstance().getCamera("MainCam").updateViewMatrix();
		
		GameState.getInstance().getLight("MainLight").updateEyeSpacePosition();

		Iterator<IUpdatableGameComponent> iterUp = GameState.getInstance().getUpdatables().iterator();
		
		while (iterUp.hasNext())
		{
			iterUp.next().update(delta);
		}
		
		// PhysicsUpdate
		if(GameState.getInstance().getGameStatus()){
			PhysicsWorld.instance("MainWorld").update(delta);
		}

		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		
		Iterator<IDrawableGameComponent> iterDraw = GameState.getInstance().getDrawables().iterator();
		while (iterDraw.hasNext())
		{
			iterDraw.next().getPainter().draw();
		}

		gameGUI.Draw();
		
		for (GUIQuad q : guiQuads)
		{
			q.draw();
		}
		
		GameState.getInstance().cleanCollections();
		
	}
}
