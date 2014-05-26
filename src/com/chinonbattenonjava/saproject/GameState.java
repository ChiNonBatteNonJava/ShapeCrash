package com.chinonbattenonjava.saproject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import Physic.PhysicCar;

import com.badlogic.gdx.math.Vector3;

public class GameState {
	// singleton instance
	private static GameState instance;
	
	// locals
	private HashSet<IDrawableGameComponent> drawables;
	private Set<IUpdatableGameComponent> updatables;
	private ConcurrentHashMap<String, GameCamera> cameras;
	private ConcurrentHashMap<String, GameLight> lights;

	private ArrayList<Checkpoint> checkpoints;
	private int nLap = 3; 
	private boolean camState = false;
	
	private GameState()
	{
		drawables = new HashSet<IDrawableGameComponent>();
		updatables = Collections.newSetFromMap(new ConcurrentHashMap<IUpdatableGameComponent,Boolean>());
		cameras = new ConcurrentHashMap<String, GameCamera>();
		lights = new ConcurrentHashMap<String, GameLight>();
		checkpoints = new ArrayList<Checkpoint>();
		checkpoints = new ArrayList<Checkpoint>();
	}
	
	public void createCheckpoint(){
		checkpoints.add(new Checkpoint(new Vector3(-150,-5,30), 0));
		checkpoints.add(new Checkpoint(new Vector3(-150,2,850), 0));
		checkpoints.add(new Checkpoint(new Vector3(150,-5,-30), 0));
		checkpoints.add(new Checkpoint(new Vector3(150,-1, 880), 0));
	}
	
	public ArrayList<Checkpoint> getCheckpoints(){
		return checkpoints;
	}
	
	public static GameState getInstance()
	{
		if (instance == null)
			instance = new GameState();
		return instance;
	}
	
	public HashSet<IDrawableGameComponent> getDrawables()
	{
		return drawables;
	}
	
	public Set<IUpdatableGameComponent> getUpdatables()
	{
		synchronized(updatables){
			return updatables;
		}
	}
	
	public void removeUpdatable(IUpdatableGameComponent c){
		synchronized(updatables){
			updatables.remove(c);
		}
	}
	
	public void removeDrawable(IDrawableGameComponent c){
		synchronized(drawables){
			drawables.remove(c);
		}
	}
	
	public int getLap(){
		return nLap;
	}
	
	public void decreseLap(){
		nLap--;
	}
	
	public void setLap(int n){
		nLap= n;
	}
	
	public GameCamera getCamera(String cameraName)
	{
		if (!cameras.containsKey(cameraName))
			cameras.put(cameraName, new GameCamera());
		return cameras.get(cameraName);
	}
	
	public GameLight getLight(String lightName)
	{
		if (!lights.containsKey(lightName))
			lights.put(lightName, new GameLight());
		return lights.get(lightName);
	}
	
	public void registerDrawable(IDrawableGameComponent drawable)
	{
		synchronized(drawables){
			drawables.add(drawable);
		}
	}
	
	public void registerUpdatable(IUpdatableGameComponent updatable)
	{
		synchronized(updatables){
			updatables.add(updatable);
		}
	}
	
	public boolean camState(){
		return camState;
	}
	
	public void changeCamState(){
		Car car = GameResourceManager.getInstance().getCar( GameResourceManager.getInstance().getPlayerName());
		Vector3 camPos=new Vector3();
		Vector3 carPos = car.getCarPos();
		if(camState){
			camState = false;
			GameState.getInstance().getCamera("MainCam").setEye(carPos.x-camPos.x,carPos.y-camPos.y+6,carPos.z-camPos.z);			
		}else{
			camState = true;
			GameState.getInstance().getCamera("MainCam").setEye(carPos.x,carPos.y+45,carPos.z);
		}
	}
	
	public void reset(){
		
		instance=null;
	}
}
