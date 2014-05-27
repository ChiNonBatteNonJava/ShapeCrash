package com.chinonbattenonjava.saproject;

import java.util.ArrayList;
import java.util.Random;

import org.json.simple.JSONObject;

import ClientSide.Client;
import Physic.PhysicCar;
import Physic.PhysicsWorld;
import android.opengl.Matrix;
import android.util.Log;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShapeX;

class BoxShape extends btBoxShape{

	public BoxShape(Vector3 boxHalfExtents) {
		super(boxHalfExtents);
		
	}
	
	@Override
	protected void finalize() throws Throwable {
		this.release();
		this.destroyed = true;
		super.finalize();

	}
	
	
}

public class Car implements IDrawableGameComponent, IUpdatableGameComponent {
	// locals
	private GameCarPainter painter;
	private int counterCheckpoint;
	private float[] mvpMatrix;
	private float[] mModelMatrix;
	private float[] mNormalMatrix;
	private float[] mm; //intermediate matrix for computation
	
	private int i=-40;
	public String name;
	Vector3 startPos;
	public Car(String name,Vector3 startPos)
	{
		this.startPos=startPos;
		Random r = new Random();
		int a = r.nextInt(30) - 15;
		this.startPos = new Vector3(a, 0, a);
		GameState.getInstance().registerDrawable(this);
		GameState.getInstance().registerUpdatable(this);
		mvpMatrix = new float[16];
		mModelMatrix = new float[16];
		mNormalMatrix = new float[16];
		mm = new float[16];
		
	    new Trail(this);
	    this.name=name;
	    counterCheckpoint = GameState.getInstance().getCheckpoints().size();
	}
	
	
	public  void initPhysics(){
		
		PhysicCar myCar=new PhysicCar();		
		Vector3 [] whell=new Vector3[4];

		whell[0]=new Vector3(-2f,1, 2f);
		whell[1]=new Vector3(2f,1,  2f);
		whell[2]=new Vector3(-2f,1,-2);
		whell[3]=new Vector3(2f,1,-2);

		
		
		BoxShape  collSh=new BoxShape(new Vector3(1.9f,1,4));
		
		myCar.createCar(collSh, 800, whell, name, "MainWorld");
		myCar.setCarPosition(startPos);
	
		
		
	}
	
	public float[] getMVPMatrix()
	{
		return mvpMatrix;
	}
	
	public float[] getNormalMatrix()
	{
		return mNormalMatrix;
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
	
	@Override
	public void update(float delta) {
		
		if(PhysicsWorld.instance("MainWorld").getVheicle(name)!=null){
			mModelMatrix=PhysicsWorld.instance("MainWorld").getWheicleChaiss(name);
			
			Matrix.multiplyMM(mvpMatrix, 0, GameState.getInstance().getCamera("MainCam").getViewMatrix(), 0, mModelMatrix, 0);
			
			Matrix.invertM(mm, 0, mvpMatrix, 0);
			Matrix.transposeM(mNormalMatrix, 0, mm, 0);
			
			Matrix.multiplyMM(mvpMatrix, 0, GameState.getInstance().getCamera("MainCam").getProjectionMatrix(), 0, mvpMatrix, 0);
			
			if(name.equals(GameResourceManager.getInstance().getPlayerName())){
				ArrayList<Checkpoint> checkpoints = GameState.getInstance().getCheckpoints();
				for(Checkpoint c: checkpoints){
					if(c.checkCarPosition(this.getCarPos())){
						counterCheckpoint--;
					}
				}
				if(counterCheckpoint == 0){
					for(Checkpoint c: checkpoints){
						c.reset();						
					}
					counterCheckpoint = GameState.getInstance().getCheckpoints().size();
					GameState.getInstance().decreseLap();
					if(GameState.getInstance().getLap() == 0){
						JSONObject json = new JSONObject();
						json.put("code", 8);
						Client.getInstance().sendNoRecive(json.toJSONString());
					}
				}
			}
			
		    mModelMatrix=PhysicsWorld.instance("MainWorld").getWheicleChaiss(name);
			
			Matrix.multiplyMM(mvpMatrix, 0, GameState.getInstance().getCamera("MainCam").getViewMatrix(), 0, mModelMatrix, 0);
			Matrix.multiplyMM(mvpMatrix, 0, GameState.getInstance().getCamera("MainCam").getProjectionMatrix(), 0, mvpMatrix, 0);
		}else{
			this.initPhysics();
		}
	}
	
	
}
