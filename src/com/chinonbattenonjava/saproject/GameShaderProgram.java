package com.chinonbattenonjava.saproject;

import android.opengl.GLES20;

public class GameShaderProgram {
	
	private int mProgram;
	
	public GameShaderProgram(GameShader vertexShader, GameShader pixelShader)
	{
		mProgram = GLES20.glCreateProgram();
		GLES20.glAttachShader(mProgram, vertexShader.getShader());
		GLES20.glAttachShader(mProgram, pixelShader.getShader());
		GLES20.glLinkProgram(mProgram);
	}
	
	public int getProgram()
	{
		return mProgram;
	}
}
