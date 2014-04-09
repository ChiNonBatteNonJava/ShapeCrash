package com.chinonbattenonjava.saproject;

import java.util.HashSet;


public class GameState {
	private HashSet<GameComponent> components;
	
	public GameState()
	{
		components = new HashSet<GameComponent>();
	}
	
	public void registerComponent(GameComponent component)
	{
		components.add(component);
	}
	
	public HashSet<GameComponent> getGameComponents()
	{
		return components;
	}
}
