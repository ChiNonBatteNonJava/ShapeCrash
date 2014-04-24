package com.chinonbattenonjava.saproject;

import android.opengl.GLES20;
import android.util.Log;

public class GameShaderProgram {
	// class TAG for Log
	private static final String TAG = "GameShaderProgram";
	
	private int mProgram;
	
	public GameShaderProgram(GameShader vertexShader, GameShader pixelShader)
	{
		mProgram = GLES20.glCreateProgram();
		GLES20.glAttachShader(mProgram, vertexShader.getShader());
		GLES20.glAttachShader(mProgram, pixelShader.getShader());
		GLES20.glLinkProgram(mProgram);
		
		int[] linked = new int[1];
		GLES20.glGetProgramiv(mProgram, GLES20.GL_LINK_STATUS, linked, 0);
        if (linked[0] != GLES20.GL_TRUE) {
        	Log.e(TAG, "Could not LINK shaders: " + vertexShader.getShaderName() + " + " + pixelShader.getShaderName());
            Log.e(TAG, GLES20.glGetProgramInfoLog(mProgram));
            GLES20.glDeleteProgram(mProgram);
        } else {
        	Log.d(TAG, "Shaders linked: " + vertexShader.getShaderName() + " + " + pixelShader.getShaderName());
        }
		
		Log.d("Shader Program", "" + mProgram);
	}
	
	public int getProgram()
	{
		return mProgram;
	}
}
