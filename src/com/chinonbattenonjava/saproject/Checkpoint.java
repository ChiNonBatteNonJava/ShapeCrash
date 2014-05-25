package com.chinonbattenonjava.saproject;

import android.location.GpsStatus.NmeaListener;
import android.opengl.Matrix;

import com.badlogic.gdx.math.Vector3;

public class Checkpoint implements IDrawableGameComponent, IUpdatableGameComponent{

	private GameCheckpointPainter painter;
	
	private float[] mvpMatrix;
	private float[] mModelMatrix;
	private float[] mNormalMatrix;
	private float[] mm; // intermediate matrix for computation
	private float[] color;
	
	public Checkpoint(Vector3 pos, float orientation){
		GameState.getInstance().registerUpdatable(this);
		GameState.getInstance().registerDrawable(this);
		
		mvpMatrix = new float[16];
		
		color = new float[3];
		color[0] = 0;
		color[1] = 1;
		color[2] = 0;
		
		mModelMatrix = new float[16];
		
		Matrix.setIdentityM(mModelMatrix, 0);
		Matrix.rotateM(mModelMatrix, 0, orientation, 0.0f, 1.0f, 0.0f);
		Matrix.translateM(mModelMatrix, 0, pos.x, pos.y, pos.z);
		
		mNormalMatrix = new float[16];
		mm = new float[16];
	}
	
	@Override
	public void update(float delta) {
		Matrix.multiplyMM(mvpMatrix, 0, GameState.getInstance().getCamera("MainCam").getViewMatrix(), 0, mModelMatrix, 0);
		
		//generate normal matrix
		Matrix.invertM(mm, 0, mvpMatrix, 0);
		Matrix.transposeM(mNormalMatrix, 0, mm, 0);
		
		Matrix.multiplyMM(mvpMatrix, 0, GameState.getInstance().getCamera("MainCam").getProjectionMatrix(), 0, mvpMatrix, 0);

	}

	@Override
	public void initPhysics() {
		
		
	}
	
	public float[] getMVPMatrix()
	{
		return mvpMatrix;
	}
	
	public float[] getNormalMatrix()
	{
		return mNormalMatrix;
	}

	@Override
	public IPainter getPainter() {
		if (painter == null)
			painter = new GameCheckpointPainter(this);
		return painter;
	}
	
	public float[] getColor(){
		return color;
	}
}
