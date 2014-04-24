package com.chinonbattenonjava.saproject;

import android.opengl.GLES20;
import android.util.Log;

public class GameShader {
	// class TAG for Log
	private static final String TAG = "GameShader";
	
	// locals
	private int shader;
	private int shaderType;
	private String shaderName;
	private String shaderCode;
	
	public GameShader(int shaderType)
	{
		if (shaderType != GLES20.GL_FRAGMENT_SHADER && shaderType != GLES20.GL_VERTEX_SHADER)
			Log.e(TAG, "Initialized GameShader with invalid shaderType!");
		
		this.shaderType = shaderType;
		this.shaderName = "";
		this.shaderCode = "";
	}
	
	public int getShader()
	{
		return shader;
	}
	
	public int getShaderType()
	{
		return shaderType;
	}
	
	public String getShaderName()
	{
		return shaderName;
	}
	
	public void setShaderName(String shaderName)
	{
		this.shaderName = shaderName;
	}
	
	public void setShaderCode(String shaderCode)
	{
		this.shaderCode = shaderCode;
	}
	
	public void compileShader()
	{
			// compile
			this.shader = GLES20.glCreateShader(shaderType);
			
			GLES20.glShaderSource(shader, shaderCode);
			GLES20.glCompileShader(shader);
			
			// check if successfully compiled
			int[] compiled = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {
            	Log.e(TAG, "Could not compile shader " + shaderName + ":");
                Log.e(TAG, GLES20.glGetShaderInfoLog(shader));
                Log.e(TAG, shaderCode);
                GLES20.glDeleteShader(shader);
            } else {
            	Log.d(TAG, "Shader compiled: " + shaderName + " " + shader);
            }
	}
}
