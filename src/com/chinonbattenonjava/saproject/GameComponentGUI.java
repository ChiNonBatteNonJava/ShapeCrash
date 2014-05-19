package com.chinonbattenonjava.saproject;

public abstract class GameComponentGUI<T> {
	protected T elemet;
	
	public GameComponentGUI(T element){
		this.elemet=element;
	}
	
	public abstract void draw(float[] proj,float[] look);
	
	
	
	
	
	
}
