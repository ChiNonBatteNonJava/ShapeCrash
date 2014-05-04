package com.chinonbattenonjava.saproject;

import Physic.PhysicsWorld;
import android.opengl.Matrix;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class Terrain implements IDrawableGameComponent, IUpdatableGameComponent {
	private GameTerrainPainter painter=null;
	private float[] mvpMatrix;
	
	public Terrain(){
		
		GameState.getInstance().registerDrawable(this);
		GameState.getInstance().registerUpdatable(this);
		mvpMatrix = new float[16];
	
	}
	
	private void initPhysics(){
		
	  PhysicsWorld.instance("MainWorld").addMeshCollider(painter.getGame3dModel().getVerticesVector3(), new Vector3(0,-27,0), new Quaternion(0,0,0,1), 0, "terrain1");
		
		
	}
	@Override
	public void update(float delta) {
	float[] mModelMatrix = new float[16];
		
	  // mModelMatrix=PhysicsWorld.instance("MainWorld").getM atrixName("terrain1");
	    Matrix.setIdentityM(mModelMatrix, 0);
		
		Matrix.multiplyMM(mvpMatrix, 0, GameState.getInstance().getCamera("MainCam").getViewMatrix(), 0, mModelMatrix, 0);
		Matrix.multiplyMM(mvpMatrix, 0, GameState.getInstance().getCamera("MainCam").getProjectionMatrix(), 0, mvpMatrix, 0);
		
	}

	@Override
	public IPainter getPainter() {
		if (painter == null){
			painter = new GameTerrainPainter(this);
			initPhysics();
		}
		return painter;
	}

	public float[] getMVPMatrix() {
		return mvpMatrix;
	}

}
