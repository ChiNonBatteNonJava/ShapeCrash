package com.chinonbattenonjava.saproject;

import java.util.HashSet;

public class GameState {
	// singleton instance
	private static GameState instance;
	
	// locals
	private RendererState rendererState;
	private HashSet<IDrawableGameComponent> drawables;
	private HashSet<IUpdatableGameComponent> updatables;
	private GameCamera camera;
	
	private GameState()
	{
		rendererState = RendererState.NOT_READY;
		
		drawables = new HashSet<IDrawableGameComponent>();
		updatables = new HashSet<IUpdatableGameComponent>();
		
		camera = new GameCamera();
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
	
	public HashSet<IUpdatableGameComponent> getUpdatables()
	{
		return updatables;
	}
	
	public GameCamera getCamera()
	{
		return camera;
	}
	
	public void registerDrawable(IDrawableGameComponent drawable)
	{
		drawables.add(drawable);
	}
	
	public void registerUpdatable(IUpdatableGameComponent updatable)
	{
		updatables.add(updatable);
	}
}
