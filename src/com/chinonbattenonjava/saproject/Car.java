package com.chinonbattenonjava.saproject;

import com.badlogic.gdx.math.Vector3;

import Physic.PhysicCar;
import Physic.PhysicsWorld;
import android.opengl.Matrix;

public class Car implements IDrawableGameComponent, IUpdatableGameComponent {
	// locals
	private GameCarPainter painter;
	private float[] mvpMatrix;

	public String name;
	public Car()
	{
		GameState.getInstance().registerDrawable(this);
		GameState.getInstance().registerUpdatable(this);
		mvpMatrix = new float[16];
	    this.initPhysics();
	}
	
	
	private void initPhysics(){
		PhysicCar myCar=new PhysicCar();		
		Vector3 [] whell=new Vector3[3];
		whell[0]=new Vector3(0,0.3f,2);
		whell[1]=new Vector3(-1,0.3f,-2);
		whell[2]=new Vector3(1,0.3f,-2);
		
		name="car"+PhysicsWorld.instance("MainWorld").getVehicleCount();
		myCar.createCar(PhysicsWorld.getBoxCollisionShape(new Vector3(1,0.5f,1)), 800, whell, name, "MainWorld");
		myCar.setCarPosition(new Vector3(0,3,1));
		
		
	}
	@Override
	public float[] getMVPMatrix()
	{
		return mvpMatrix;
	}
	
	
	public float[][] getMVPwhellMatrix(){
		return whellMatrix;
		
	}
	public Vector3 getCarPos(){
		  return PhysicsWorld.instance("MainWorld").getVheiclePosition(name);
		//return new Vector3(0,10,0);
	}

	@Override
	public IPainter getPainter() {
		if (painter == null)
			painter = new GameCarPainter(this);
		return painter;
	}
	float[][] whellMatrix;
	
	
	
	
	@Override
	public void update(float delta) {
		// TODO Auto-generated  method stub
		whellMatrix=new float[3][16];
		
		float[][] whellPos=PhysicsWorld.instance("MainWorld").getVheicleWhells(name);
		
		float[] mModelMatrix = new float[16];
		//Matrix.setIdentityM(mModelMatrix, 0);
		
	  mModelMatrix=PhysicsWorld.instance("MainWorld").getWheicleChaiss(name);
		Matrix.multiplyMM(mvpMatrix, 0, GameState.getInstance().getCamera("MainCam").getViewMatrix(), 0, mModelMatrix, 0);
		Matrix.multiplyMM(mvpMatrix, 0, GameState.getInstance().getCamera("MainCam").getProjectionMatrix(), 0, mvpMatrix, 0);
		
		Matrix.multiplyMM(whellMatrix[0], 0, GameState.getInstance().getCamera("MainCam").getViewMatrix(), 0, whellPos[0], 0);
		Matrix.multiplyMM(whellMatrix[0], 0, GameState.getInstance().getCamera("MainCam").getProjectionMatrix(), 0, whellMatrix[0], 0);
		
		Matrix.multiplyMM(whellMatrix[1], 0, GameState.getInstance().getCamera("MainCam").getViewMatrix(), 0, whellPos[1], 0);
		Matrix.multiplyMM(whellMatrix[1], 0, GameState.getInstance().getCamera("MainCam").getProjectionMatrix(), 0, whellMatrix[1], 0);
		
		Matrix.multiplyMM(whellMatrix[2], 0, GameState.getInstance().getCamera("MainCam").getViewMatrix(), 0, whellPos[2], 0);
		Matrix.multiplyMM(whellMatrix[2], 0, GameState.getInstance().getCamera("MainCam").getProjectionMatrix(), 0, whellMatrix[2], 0);
		
		
		
	}
	
}
