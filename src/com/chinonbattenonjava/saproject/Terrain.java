package com.chinonbattenonjava.saproject;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

import Physics.PhysicsWorld;
import android.opengl.Matrix;

public class Terrain implements IDrawableGameComponent, IUpdatableGameComponent {
	private GameTerrainPainter painter;
	private float[] mvpMatrix;
	
	public Terrain(){
		
		GameState.getInstance().registerDrawable(this);
		GameState.getInstance().registerUpdatable(this);
		mvpMatrix = new float[16];
		
	}
	
	private void initPhysics(){
		
		PhysicsWorld.instance("MainWorld").addMeshCollider(painter.getGame3dModel().getVerticesVector3(), new Vector3(0,-7,0), new Quaternion(0,0,0,1), 0, "terrain1");
		
		
	}
	@Override
	public void update(float delta) {
	float[] mModelMatrix = new float[16];
		
	    //mModelMatrix=PhysicsWorld.instance("MainWorld").getWheicleChaiss(name);
		Matrix.setIdentityM(mModelMatrix, 0);
		
		Matrix.multiplyMM(mvpMatrix, 0, GameState.getInstance().getCamera("MainCam").getViewMatrix(), 0, mModelMatrix, 0);
		Matrix.multiplyMM(mvpMatrix, 0, GameState.getInstance().getCamera("MainCam").getProjectionMatrix(), 0, mvpMatrix, 0);
		
	}

	@Override
	public IPainter getPainter() {
		if (painter == null)
			painter = new GameTerrainPainter(this);
			initPhysics();
		return painter;
	}

	@Override
	public float[] getMVPMatrix() {
		return mvpMatrix;
	}

}
