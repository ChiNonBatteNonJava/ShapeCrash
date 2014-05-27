package com.chinonbattenonjava.saproject;

import Physic.PhysicsWorld;
import android.opengl.Matrix;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class Terrain implements IDrawableGameComponent, IUpdatableGameComponent {
	private GameTerrainPainter painter=null;
	private float[] mvpMatrix;
	private float[] mModelMatrix;
	private float[] mNormalMatrix;
	private float[] mm; //intermediate matrix for computation
	
	private float[] color;
	
	public Terrain(){

		GameState.getInstance().registerDrawable(this);
		GameState.getInstance().registerUpdatable(this);
		mvpMatrix = new float[16];
		mModelMatrix = new float[16];
		mNormalMatrix = new float[16];
		mm = new float[16];
		
		color = new float[3];
		color[0] = 1;
		color[1] = 0;
		color[2] = 0;
	
	}
	
	public void initPhysics(){
		
	  PhysicsWorld.instance("MainWorld").addMeshCollider(painter.getGame3dModel().getVerticesVector3(), new Vector3(0,-37,0), new Quaternion(0,0,0,1), 0, "terrain1",(short)1,(short)3);
		
	}
	@Override
	public void update(float delta) {
		mModelMatrix = PhysicsWorld.instance("MainWorld").getMatrixName("terrain1");
		Matrix.multiplyMM(mvpMatrix, 0, GameState.getInstance().getCamera("MainCam").getViewMatrix(), 0, mModelMatrix, 0);
		
		Matrix.invertM(mm, 0, mvpMatrix, 0);
		Matrix.transposeM(mNormalMatrix, 0, mm, 0);
		
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
	
	public float[] getNormalMatrix()
	{
		return mNormalMatrix;
	}
	
	public float[] getColor()
	{
		return color;
	}

}
