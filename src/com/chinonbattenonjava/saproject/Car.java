package com.chinonbattenonjava.saproject;

import android.opengl.Matrix;

public class Car implements IDrawableGameComponent, IUpdatableGameComponent {
	// locals
	private GameCarPainter painter;
	private float[] mvpMatrix;
	
	public Car()
	{
		GameState.getInstance().registerDrawable(this);
		GameState.getInstance().registerUpdatable(this);
		
		mvpMatrix = new float[16];
	}
	
	@Override
	public float[] getMVPMatrix()
	{
		return mvpMatrix;
	}

	@Override
	public IPainter getPainter() {
		if (painter == null)
			painter = new GameCarPainter(this);
		return painter;
	}
	
	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub

		float[] mModelMatrix = new float[16];
		Matrix.setIdentityM(mModelMatrix, 0);
		
		Matrix.multiplyMM(mvpMatrix, 0, GameState.getInstance().getCamera().getViewMatrix(), 0, mModelMatrix, 0);
		Matrix.multiplyMM(mvpMatrix, 0, GameState.getInstance().getCamera().getProjectionMatrix(), 0, mvpMatrix, 0);
	}
	
}
