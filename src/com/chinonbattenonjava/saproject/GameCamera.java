package com.chinonbattenonjava.saproject;

import android.opengl.Matrix;

public class GameCamera {
	private float[] projMatrix;
	private float[] viewMatrix;
	
	private float[] eye;
	private float[] target;
	private float[] up;
	
	public GameCamera()
	{
		projMatrix = new float[16];
		viewMatrix = new float[16];
		
		eye = new float[3];
		target = new float[3];
		up = new float[3];
	}
	
	public float[] getProjectionMatrix()
	{
		return projMatrix;
	}
	
	public float[] getViewMatrix()
	{
		return viewMatrix;
	}
	
	public void setEye(final float[] eye)
	{
		this.eye[0] = eye[0];
		this.eye[1] = eye[1];
		this.eye[2] = eye[2];
	}
	
	public void setEye(final float eyeX, final float eyeY, final float eyeZ)
	{
		this.eye[0] = eyeX;
		this.eye[1]	= eyeY;
		this.eye[2] = eyeZ;
	}
	
	public void setTarget(final float[] target)
	{
		this.target[0] = target[0];
		this.target[1] = target[1];
		this.target[2] = target[2];
	}
	
	public void setTarget(final float targetX, final float targetY, final float targetZ)
	{
		this.target[0] = targetX;
		this.target[1] = targetY;
		this.target[2] = targetZ;
	}
	
	public void setFrustum(final float near, final float far, final float ratio)
	{
		Matrix.frustumM(projMatrix, 0, -ratio, ratio, -1, 1, near, far);
	}
	
	public void updateViewMatrix()
	{
		Matrix.setLookAtM(viewMatrix, 0, eye[0], eye[1], eye[2], target[0], target[1], target[2], up[0], up[1], up[2]);
	}
}
