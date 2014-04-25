package com.chinonbattenonjava.saproject;

import com.badlogic.gdx.math.Vector3;

import Physics.PhysicCar;
import Physics.PhysicsWorld;
import android.opengl.Matrix;

public class Car implements IDrawableGameComponent, IUpdatableGameComponent {
	// locals
	private GameCarPainter painter;
	private float[] mvpMatrix;

	private String name;
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
		
	    mModelMatrix=PhysicsWorld.instance("MainWorld").getWheicleChaiss(name);
		//Matrix.setIdentityM(mModelMatrix, 0);
		
		Matrix.multiplyMM(mvpMatrix, 0, GameState.getInstance().getCamera("MainCam").getViewMatrix(), 0, mModelMatrix, 0);
		Matrix.multiplyMM(mvpMatrix, 0, GameState.getInstance().getCamera("MainCam").getProjectionMatrix(), 0, mvpMatrix, 0);
	}
	
}
