package com.chinonbattenonjava.saproject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.math.Vector3;

public class GameState {
	// singleton instance
	private static GameState instance;
	
	// locals
	private RendererState rendererState;
	private HashSet<IDrawableGameComponent> drawables;
	private Set<IUpdatableGameComponent> updatables;
	private ConcurrentHashMap<String, GameCamera> cameras;
	private ArrayList<Checkpoint> checkpoints;
	
	private GameState()
	{
		rendererState = RendererState.NOT_READY;
		
		drawables = new HashSet<IDrawableGameComponent>();
		updatables = Collections.newSetFromMap(new ConcurrentHashMap<IUpdatableGameComponent,Boolean>());
		cameras = new ConcurrentHashMap<String, GameCamera>();
		
		checkpoints = new ArrayList<Checkpoint>();
	}
	
	public void createCheckpoint(){
		checkpoints.add(new Checkpoint(new Vector3(0,-10,0), 0));
		checkpoints.add(new Checkpoint(new Vector3(50,-10,0), 0));
		checkpoints.add(new Checkpoint(new Vector3(100,-10,0), 0));
		checkpoints.add(new Checkpoint(new Vector3(150,-10,0), 0));
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
	
	public RendererState getRendererState()
	{
		return rendererState;
	}
	
	public void setRendererState(RendererState state)
	{
		rendererState = state;
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
	
	public GameCamera getCamera(String cameraName)
	{
		if (!cameras.containsKey(cameraName))
			cameras.put(cameraName, new GameCamera());
		return cameras.get(cameraName);
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
	
	public void reset(){
		
		instance=null;
	}
}
