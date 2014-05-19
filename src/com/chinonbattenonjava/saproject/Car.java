package com.chinonbattenonjava.saproject;

import Physic.PhysicCar;
import Physic.PhysicsWorld;
import android.opengl.Matrix;
import android.util.Log;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShapeX;

public class Car implements IDrawableGameComponent, IUpdatableGameComponent {
	// locals
	private GameCarPainter painter;
	private float[] mvpMatrix;
	private int i=-10;
	public String name;
	public Car()
	{
		GameState.getInstance().registerDrawable(this);
		GameState.getInstance().registerUpdatable(this);
		mvpMatrix = new float[16];
	    this.initPhysics();
	    new Trail(this);
	}
	
	
	private void initPhysics(){
		PhysicCar myCar=new PhysicCar();		
		Vector3 [] whell=new Vector3[4];

		whell[0]=new Vector3(-2f,1, 2f);
		whell[1]=new Vector3(2f,1,  2f);
		whell[2]=new Vector3(-2f,1,-2);
		whell[3]=new Vector3(2f,1,-2);

		
		name="car"+PhysicsWorld.instance("MainWorld").getVehicleCount();

		btBoxShape  collSh=new btBoxShape(new Vector3(1.9f,1,4));
		
		myCar.createCar(collSh, 800, whell, name, "MainWorld");
		myCar.setCarPosition(new Vector3(0,25.5f,1));

		
		
	}
	
	public float[] getMVPMatrix()
	{
		return mvpMatrix;
	}
	
	
	public float[][] getMVPwhellMatrix(){
		return whellMatrix;
		
	}
	public Vector3 getCarPos(){
		  return PhysicsWorld.instance("MainWorld").getVheiclePosition(name);
		
	}
	public PhysicCar getCar(){
		return PhysicsWorld.instance("MainWorld").getVheicle(name);
		
	}

	@Override
	public IPainter getPainter() {
		if (painter == null){
			painter = new GameCarPainter(this);
		}
		return painter;
	}
	float[][] whellMatrix;
	
	
	
	
	@Override
	public void update(float delta) {
		Log.i("a",""+PhysicsWorld.instance("MainWorld").getVheicle(name).getSpeedKMH());
		if(PhysicsWorld.instance("MainWorld").getVheicle(name).getSpeedKMH()<0.1f && PhysicsWorld.instance("MainWorld").getVheicle(name).getSpeedKMH()>0.0f && i==0){
			PhysicsWorld.instance("MainWorld").getVheicle(name).doRetro();
			i++;
			
		}
		else if(i==10){
			PhysicsWorld.instance("MainWorld").getVheicle(name).doRetro();	
			i=-20;
			
		}else if(i>0 && i<10){
			i++;
		}
		if(i<0){
			i++;;
		}
		// TODO Auto-generated  method stub
		whellMatrix=new float[4][16];
		
		
		float[][] whellPos=PhysicsWorld.instance("MainWorld").getVheicleWhells(name);
		
		float[] mModelMatrix = new float[16];
		//Matrix.setIdentityM(mModelMa trix, 0);
	
		
		mModelMatrix=PhysicsWorld.instance("MainWorld").getWheicleChaiss(name);
	 
		
	 
		Matrix.multiplyMM(mvpMatrix, 0, GameState.getInstance().getCamera("MainCam").getViewMatrix(), 0, mModelMatrix, 0);
		Matrix.multiplyMM(mvpMatrix, 0, GameState.getInstance().getCamera("MainCam").getProjectionMatrix(), 0, mvpMatrix, 0);
		
		Matrix.multiplyMM(whellMatrix[0], 0, GameState.getInstance().getCamera("MainCam").getViewMatrix(), 0, whellPos[0], 0);
		Matrix.multiplyMM(whellMatrix[0], 0, GameState.getInstance().getCamera("MainCam").getProjectionMatrix(), 0, whellMatrix[0], 0);
		
		Matrix.multiplyMM(whellMatrix[1], 0, GameState.getInstance().getCamera("MainCam").getViewMatrix(), 0, whellPos[1], 0);
		Matrix.multiplyMM(whellMatrix[1], 0, GameState.getInstance().getCamera("MainCam").getProjectionMatrix(), 0, whellMatrix[1], 0);
		
		Matrix.multiplyMM(whellMatrix[2], 0, GameState.getInstance().getCamera("MainCam").getViewMatrix(), 0, whellPos[2], 0);
		Matrix.multiplyMM(whellMatrix[2], 0, GameState.getInstance().getCamera("MainCam").getProjectionMatrix(), 0, whellMatrix[2], 0);
		
		Matrix.multiplyMM(whellMatrix[3], 0, GameState.getInstance().getCamera("MainCam").getViewMatrix(), 0, whellPos[3], 0);
		Matrix.multiplyMM(whellMatrix[3], 0, GameState.getInstance().getCamera("MainCam").getProjectionMatrix(), 0, whellMatrix[3], 0);
		
		whellPos=null;
		mModelMatrix=null;
	}
	
}
