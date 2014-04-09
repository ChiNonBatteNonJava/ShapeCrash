package com.chinonbattenonjava.saproject;

import java.util.HashMap;

import android.opengl.GLES20;

public class GameResourceManager {
	private HashMap<String, Integer> shaders;
	
	public GameResourceManager()
	{
		shaders = new HashMap<String, Integer>();
	}
	
	public void loadShader(String shaderName, int type, String shaderCode)
	{
		int shader = GLES20.glCreateShader(type);
		
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);
		
		shaders.put(shaderName, shader);
	}
	
	public int getShaderByName(String shaderName)
	{
		return shaders.get(shaderName);
	}
}
