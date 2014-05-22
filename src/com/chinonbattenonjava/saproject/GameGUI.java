package com.chinonbattenonjava.saproject;

import java.util.ArrayList;
import java.util.List;

import android.opengl.Matrix;
import android.util.Log;

public class GameGUI {
	List<GameComponentGUI<?>> listGui;
	private float []  projMatrix = new float[16];
	private float [] viewMatrix = new float[16];
	
	public GameGUI(){
		listGui=new ArrayList<GameComponentGUI<?>>();	
		float f=GameResourceManager.getInstance().getAspectRatio();
		Matrix.orthoM(projMatrix, 0, -4, 4, -2, 2, 0.1f, 10);
		Matrix.setLookAtM(viewMatrix, 0, 3, 0, 0, 0, 0, 0, 0, 1,0);

	}


	public <T>  void AddElement(T component){

			Log.i("GameGUI","No such GUI  for this type "+component.getClass().getName());
		
	}
	public <T extends Car>  void AddElement(T component){
			GameCarGUI myComponent=new GameCarGUI(component);
			listGui.add(myComponent);
			
		}		

	
	public void Draw(){
		for (GameComponentGUI<?> g:listGui){
			g.draw(projMatrix, viewMatrix);
			
		}
		
		
	}

}
