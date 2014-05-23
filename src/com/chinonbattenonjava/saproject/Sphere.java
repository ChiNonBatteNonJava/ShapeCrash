package com.chinonbattenonjava.saproject;

import com.badlogic.gdx.math.Vector3;

public class Sphere implements IUpdatableGameComponent, IDrawableGameComponent {
	private GameSpherePainter painter;
	
	private String id;
	private float radius;
	private Vector3 pos;
	
	private float[] mvpMatrix;
	
	public Sphere(String id, float radius, Vector3 pos)
	{
		GameState.getInstance().registerDrawable(this);
		GameState.getInstance().registerUpdatable(this);
		
		this.id = id;
		this.radius = radius;
		this.pos = pos;
		
		mvpMatrix = new float[16];
	}
	
	@Override
	public IPainter getPainter() {
		if (painter == null)
			painter = new GameSpherePainter();
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
		
	}

	@Override
	public void initPhysics() {
		// TODO Auto-generated method stub
		
	}
	
}
