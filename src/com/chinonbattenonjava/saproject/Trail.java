package com.chinonbattenonjava.saproject;

import java.util.Iterator;
import java.util.LinkedList;

import android.opengl.Matrix;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Trail implements IDrawableGameComponent, IUpdatableGameComponent {
	private final int MAX_LENGTH = 50;
	private final float NEW_VERTEX_TIME = 0.06f;
	
	private LinkedList<Vector3> pathVertices;
	private float[] pathVerticesArray;
	private boolean dirtyArray;
	private float[] lastLine;

	private float timer;
	private Car car;
	private GameTrailPainter painter;
	private int currentLength;
	private float[] mvpMatrix;
	
	public Trail(Car car)
	{
		GameState.getInstance().registerDrawable(this);
		GameState.getInstance().registerUpdatable(this);
		
		lastLine = new float[4];
		
		pathVertices = new LinkedList<Vector3>();
		
		// first vertex at car location
		pathVertices.add(new Vector3(car.getCarPos()));
		currentLength = 1;
		
		// used for getPathVertices() optimization
		dirtyArray = true;
		
		mvpMatrix = new float[16];
		
		timer = 0.0f;
		
		this.car = car;
	}
	
	public float[] getPathVertices()
	{
		if (dirtyArray == true){
			pathVerticesArray = new float[pathVertices.size()*3];
			
			int i = 0;
			Iterator<Vector3> iter = pathVertices.iterator();
			while (iter.hasNext())
			{
				Vector3 v = iter.next();
				pathVerticesArray[i] = v.x;
				i++;
				pathVerticesArray[i] = v.y;
				i++;
				pathVerticesArray[i] = v.z;
				i++;
			}
			
			dirtyArray = false;
			
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
			
			//start updating lastLine[]
			lastLine[0] = pathVertices.getFirst().x;
			lastLine[1] = pathVertices.getFirst().z;
			
			//add new head
			pathVertices.addFirst(new Vector3(car.getCarPos()));
			currentLength++;
			dirtyArray = true;
			
			//complete updating lastLine[]
			lastLine[2] = pathVertices.getFirst().x;
			lastLine[3] = pathVertices.getFirst().z;
			
			//check trail collisions
			checkNewShape();
		}
		
		// remove tail when the trail is too long
		if (currentLength > MAX_LENGTH)
		{
			//remove last vertex
			pathVertices.removeLast();
			currentLength--;
			dirtyArray = true;
		}
		
		// compute mvpMatrix
		float[] mModelMatrix = new float[16];
		Matrix.setIdentityM(mModelMatrix, 0);
		Matrix.multiplyMM(mvpMatrix, 0, GameState.getInstance().getCamera("MainCam").getViewMatrix(), 0, mModelMatrix, 0);
		Matrix.multiplyMM(mvpMatrix, 0, GameState.getInstance().getCamera("MainCam").getProjectionMatrix(), 0, mvpMatrix, 0);
	}
	
	//little optimization, reduce dynamic allocation
	private Vector2 a1 = new Vector2();
	private Vector2 a2 = new Vector2();
	private Vector2 b1 = new Vector2();
	private Vector2 b2 = new Vector2();
	private void checkNewShape() {
		
		// head line
		a1.x = lastLine[0];
		a1.y = lastLine[1];
		a2.x = lastLine[2];
		a2.y = lastLine[3];
		
		float[] v = getPathVertices();
		
		// start calculating bound box
		float minX, maxX, minY, maxY;
		
		if (a1.x <= a2.x) { minX = a1.x; maxX = a2.x; } else { minX = a2.x; maxX = a1.x; }
		if (a1.y <= a2.y) { minY = a1.y; maxY = a2.y; } else { minY = a2.y; maxY = a1.y; }
		
		int idx = v.length - 1; // we skip the first line (head), it's the one we test against all others
		while (idx >= 9)
		{
			//other lines
			b1.y = v[idx]; idx--;idx--;
			b1.x = v[idx]; idx--;
			
			b2.y = v[idx]; idx--;idx--;
			b2.x = v[idx]; idx--;
			
			idx+=3; // beautiful code
			
			// if there is an intersection, reset trail and create new shape
			if (checkIntersect(a1, a2, b1, b2))
			{
				// calculate bounding box area
				for (int i = 6; i < idx - 3; i+=3)
				{
					if (v[i] <= minX)
						minX = v[i];
					else if (v[i] > maxX)
						maxX = v[i];
					
					if (v[i+2] <= minY)
						minY = v[i+2];
					else if (v[i+2] > maxY)
						maxY = v[i+2];
				}
				float area = (maxX - minX) * (maxY - minY);
				
				// new Sphere
				if (area > 50)
				{
					if (area > 2500) area = 2500;
					new Sphere(String.valueOf(hashCode() + System.nanoTime()), area/150, new Vector3((maxX + minX)/2, car.getCarPos().y + area/25, (maxY + minY)/2));
				}
				//reset trail
				pathVertices.clear();
				
				//add new head
				pathVertices.addFirst(new Vector3(car.getCarPos()));
				currentLength = 1;
				dirtyArray = true;
				
				return;
			}
		}
	}
	
	// declarations and stuff
	private float a;
	private float b;
	private float c;
	private float cB1;
	private float cB2;
	private boolean checkIntersect(Vector2 a1, Vector2 a2, Vector2 b1, Vector2 b2)
	{
		// line through a1, a2
		a = a2.x - a1.x;
		b = a1.y - a2.y;
		c = a1.y*(a1.x-a2.x) + a1.x*(a2.y - a1.y);
		
		// check if b1 and b2 are on the same side of the a1-a2 line
		// if not, they intersect
		cB1 = a*b1.y + b*b1.x + c;
		cB2 = a*b2.y + b*b2.x + c;
		
		//same side
		if (Math.signum(cB1) == Math.signum(cB2))
			return false;
		
		// line through b1, b2
		a = b2.x - b1.x;
		b = b1.y - b2.y;
		c = b1.y*(b1.x-b2.x) + b1.x*(b2.y - b1.y);
		
		// check if b1 and b2 are on the same side of the a1-a2 line
		// if not, they intersect
		cB1 = a*a1.y + b*a1.x + c;
		cB2 = a*a2.y + b*a2.x + c;

		//same side
		if (Math.signum(cB1) == Math.signum(cB2))
			return false;
		
		return true;
	}

	@Override
	public IPainter getPainter() {
		if (painter == null)
			painter = new GameTrailPainter(this);
		
		return painter;
	}

	@Override
	public void initPhysics() {
		// TODO Auto-generated method stub
		
	}
}
