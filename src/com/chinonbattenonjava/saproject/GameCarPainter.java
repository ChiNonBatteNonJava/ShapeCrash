package com.chinonbattenonjava.saproject;


import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

public class GameCarPainter implements IPainter {
	private static final String CAR_MODEL_FILE = "car.m";
	private static final String CAR_WHELL_FILE = "whell.obj";
	
	private Car car;
	
	private Game3DModel m;
	private Game3DModel whell;
	private GameShaderProgram program;
	
	public GameCarPainter(Car car)
	{
		this.car = car;
		GameResourceManager.getInstance().load3DModel(CAR_MODEL_FILE);
		m = GameResourceManager.getInstance().get3DModelByName(CAR_MODEL_FILE);
		
		GameResourceManager.getInstance().load3DObjModel(CAR_WHELL_FILE);
		whell = GameResourceManager.getInstance().get3DModelByName(CAR_WHELL_FILE);
		
		program = GameResourceManager.getInstance().getShaderProgramByName("car");
	}
	
	private void drawWhell(){
		float[][] whellPos=car.getMVPwhellMatrix();
	
		for (float[] mat:whellPos){
			
			
			int mPositionHandle = GLES20.glGetAttribLocation(program.getProgram(), "vPosition");
			
			GLES20.glEnableVertexAttribArray(mPositionHandle);
			
			GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, whell.COORDS_PER_VERTEX * 4, whell.getVertexBuffer());
			
			// mvpMatrix
			int mvpMatrixHandle = GLES20.glGetUniformLocation(program.getProgram(), "uMVPMatrix");
			GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mat, 0);
			
			// draw
			GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, whell.getVertexCount());
			
			GLES20.glDisableVertexAttribArray(mPositionHandle);
			
		}
		whellPos=null;
		
	}
	
	
	@Override
	public void draw() {
	
		GLES20.glUseProgram(program.getProgram());
		drawWhell();
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
