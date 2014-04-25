package com.chinonbattenonjava.saproject;

import android.opengl.GLES20;

public class GameCarPainter implements IPainter {
	private static final String CAR_MODEL_FILE = "car.m";
	
	
	private Car car;
	
	private Game3DModel m;
	private GameShaderProgram program;
	
	public GameCarPainter(Car car)
	{
		this.car = car;
		GameResourceManager.getInstance().load3DModel(CAR_MODEL_FILE);
		m = GameResourceManager.getInstance().get3DModelByName(CAR_MODEL_FILE);
		
	
		
		program = new GameShaderProgram(GameResourceManager.getInstance().getShaderByName("car.vs"), GameResourceManager.getInstance().getShaderByName("car.ps"));
	}
	
	@Override
	public void draw() {
		GLES20.glUseProgram(program.getProgram());
		
		// vertex position
		int mPositionHandle = GLES20.glGetAttribLocation(program.getProgram(), "vPosition");
		
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		
		GLES20.glVertexAttribPointer(mPositionHandle, m.COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, m.COORDS_PER_VERTEX * 4, m.getVertexBuffer());
		
		// mvpMatrix
		int mvpMatrixHandle = GLES20.glGetUniformLocation(program.getProgram(), "uMVPMatrix");
		GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, car.getMVPMatrix(), 0);
		
		// draw
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
		
		GLES20.glDisableVertexAttribArray(mPositionHandle);
	}

}
