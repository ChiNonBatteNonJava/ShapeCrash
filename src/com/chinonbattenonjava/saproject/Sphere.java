package com.chinonbattenonjava.saproject;

import Physic.PhysicsWorld;
import android.opengl.Matrix;

import com.badlogic.gdx.math.Vector3;

public class Sphere implements IUpdatableGameComponent, IDrawableGameComponent {
	private static final float MASS_PER_RADIUS = 400;
	
	private GameSpherePainter painter;
	
	private String id;
	private float radius;
	private Vector3 pos;
	
	private float[] mvpMatrix;
	private float[] mModelMatrix;
	
	public Sphere(String id, float radius, Vector3 pos)
	{
		GameState.getInstance().registerDrawable(this);
		GameState.getInstance().registerUpdatable(this);
		
		this.id = id;
		this.radius = radius;
		this.pos = pos;
		
		mvpMatrix = new float[16];
		mModelMatrix = new float[16];
	}
	
	@Override
	public IPainter getPainter() {
		if (painter == null)
			painter = new GameSpherePainter(this);
		return painter;
	}
	
	public float[] getMVPMatrix()
	{
		return mvpMatrix;
	}

	public float getRadius()
	{
		return radius;
	}
	
	@Override
	public void update(float delta) {
		mModelMatrix=PhysicsWorld.instance("MainWorld").getMatrixName(id);
	 
		Matrix.multiplyMM(mvpMatrix, 0, GameState.getInstance().getCamera("MainCam").getViewMatrix(), 0, mModelMatrix, 0);
		Matrix.multiplyMM(mvpMatrix, 0, GameState.getInstance().getCamera("MainCam").getProjectionMatrix(), 0, mvpMatrix, 0);
	}

	@Override
	public void initPhysics() {
		PhysicsWorld.instance("MainWorld").addSphere(radius, pos, radius * MASS_PER_RADIUS, id);
	}
	
}
