package com.chinonbattenonjava.saproject;

import java.util.Random;

import Physic.PhysicsWorld;
import android.opengl.Matrix;

import com.badlogic.gdx.math.Vector3;

public class Sphere implements IUpdatableGameComponent, IDrawableGameComponent {
	private static final float MASS_PER_RADIUS = 400;
	
	private GameSpherePainter painter;
	
	private String id;
	private float radius;
	private float initialRadius;
	private Vector3 pos;
	private float[] color;
	
	private float[] mvpMatrix;
	private float[] mModelMatrix;
	private float[] mNormalMatrix;
	private float[] mm; // intermediate matrix for computation
	
	public Sphere(String id, float radius, Vector3 pos)
	{
		GameState.getInstance().registerDrawable(this);
		GameState.getInstance().registerUpdatable(this);
		
		this.id = id;
		this.radius = radius;
		this.initialRadius = radius;
		this.pos = pos;
		
		color = new float[3];
		Random r = new Random();
		color[0] = r.nextFloat();
		color[1] = r.nextFloat();
		color[2] = r.nextFloat();
		
		mvpMatrix = new float[16];
		mModelMatrix = new float[16];
		
		mNormalMatrix = new float[16];
		mm = new float[16];
		
		initPhysics();
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
	
	public float[] getNormalMatrix()
	{
		return mNormalMatrix;
	}
	
	public float[] getColor()
	{
		return color;
	}
	
	public float getRadius()
	{
		return radius;
	}
	
	@Override
	public void update(float delta) {
		radius -= delta/2;
		
		if (radius > 0.25)
		{
			PhysicsWorld.instance("MainWorld").setSphereScaling(id, new Vector3(radius/initialRadius, radius/initialRadius, radius/initialRadius));

			Matrix.setIdentityM(mModelMatrix, 0);

			Matrix.scaleM(mModelMatrix, 0, radius, radius, radius);
			
			Matrix.multiplyMM(mModelMatrix, 0, PhysicsWorld.instance("MainWorld").getMatrixName(id), 0, mModelMatrix, 0);
			
			Matrix.multiplyMM(mvpMatrix, 0, GameState.getInstance().getCamera("MainCam").getViewMatrix(), 0, mModelMatrix, 0);
			
			Matrix.invertM(mm, 0, mvpMatrix, 0);
			Matrix.transposeM(mNormalMatrix, 0, mm, 0);
			
			Matrix.multiplyMM(mvpMatrix, 0, GameState.getInstance().getCamera("MainCam").getProjectionMatrix(), 0, mvpMatrix, 0);
		}
		else
		{
			PhysicsWorld.instance("MainWorld").delete(id);
    		GameState.getInstance().removeDrawable(this);
    		GameState.getInstance().removeUpdatable(this);
		}
	}

	@Override
	public void initPhysics() {
		PhysicsWorld.instance("MainWorld").addSphere(radius, pos, radius * MASS_PER_RADIUS, id);
	}
	
}
