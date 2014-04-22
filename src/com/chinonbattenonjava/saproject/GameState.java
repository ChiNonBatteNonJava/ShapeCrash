package com.chinonbattenonjava.saproject;


public class GameState {
	private static GameState instance;
	
	private RendererState rendererState;
	
	private GameState()
	{
		rendererState = RendererState.NOT_READY;
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
}
