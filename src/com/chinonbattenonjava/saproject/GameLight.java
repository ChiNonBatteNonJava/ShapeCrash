package com.chinonbattenonjava.saproject;

import android.opengl.Matrix;

public class GameLight {
	private float[] pos;
	private float[] eyeSpacePosition;
	
	public GameLight()
	{
		pos = new float[4];
		pos[3] = 0;
		eyeSpacePosition = new float[4];
	}
	
	public float[] getPosition()
	{
		return pos;
	}
	
	public void setPosition(float x, float y, float z)
	{
		pos[0] = x;
		pos[1] = y;
		pos[2] = z;
	}

	public void updateEyeSpacePosition() {
		Matrix.multiplyMV(eyeSpacePosition, 0, GameState.getInstance().getCamera("MainCam").getViewMatrix(), 0, pos, 0);
	}
	
	public float[] getEyeSpacePosition() {
		return eyeSpacePosition;
	}
}
