package com.chinonbattenonjava.saproject;

import java.util.Iterator;
import java.util.LinkedList;

import android.opengl.Matrix;

import com.badlogic.gdx.math.Vector3;

public class Trail implements IDrawableGameComponent, IUpdatableGameComponent {
	private final int MAX_LENGTH = 50;
	private final float NEW_VERTEX_TIME = 0.06f;
	
	private LinkedList<Vector3> pathVertices;
	private float[] pathVerticesArray;
	private float timer;
	private Car car;
	private GameTrailPainter painter;
	private int currentLength;
	private float[] mvpMatrix;
	
	public Trail(Car car)
	{
		GameState.getInstance().registerDrawable(this);
		GameState.getInstance().registerUpdatable(this);
		
		pathVertices = new LinkedList<Vector3>();
		
		// first vertex at car location
		pathVertices.add(new Vector3(car.getCarPos()));
		currentLength = 1;
		
		mvpMatrix = new float[16];
		
		timer = 0.0f;
		
		this.car = car;
	}
	
	//little optimization
	private Vector3 v;
	private Iterator<Vector3> iter;
	public float[] getPathVertices()
	{
		pathVerticesArray = new float[pathVertices.size()*3];
		
		int i = 0;
		iter = pathVertices.iterator();
		while (iter.hasNext())
		{
			v = iter.next();
			pathVerticesArray[i] = v.x;
			i++;
			pathVerticesArray[i] = v.y;
			i++;
			pathVerticesArray[i] = v.z;
			i++;
		}
		
		return pathVerticesArray;
	}
	
	public float[] getMVPMatrix()
	{
		return mvpMatrix;
	}
	
	public Car getCar()
	{
		return car;
	}
	
	@Override
	public void update(float delta) {
		
		timer += delta;
		
		// every NEW_VERTEX_TIME add a vertex at current car location
		if (timer > NEW_VERTEX_TIME)
		{
			//reset timer
			timer -= NEW_VERTEX_TIME;

			//add new head
			pathVertices.addFirst(car.getCarPos());
			currentLength++;
		}
		
		// remove tail when the trail is too long
		if (currentLength > MAX_LENGTH)
		{
			//remove last vertex
			pathVertices.removeLast();
			currentLength--;
		}
		
		
		// compute mvpMatrix
		float[] mModelMatrix = new float[16];
		Matrix.setIdentityM(mModelMatrix, 0);
		Matrix.multiplyMM(mvpMatrix, 0, GameState.getInstance().getCamera("MainCam").getViewMatrix(), 0, mModelMatrix, 0);
		Matrix.multiplyMM(mvpMatrix, 0, GameState.getInstance().getCamera("MainCam").getProjectionMatrix(), 0, mvpMatrix, 0);
	}

	@Override
	public IPainter getPainter() {
		if (painter == null)
			painter = new GameTrailPainter(this);
		
		return painter;
	}
}
