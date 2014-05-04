package com.chinonbattenonjava.saproject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

public class GameTrailPainter implements IPainter {
	private final int COORDS_PER_VERTEX = 3;
	
	private Trail trail;
	private GameShaderProgram program;
	
	private float[] vertices;
	private FloatBuffer vertexBuffer;
	
	//optimizations
	ByteBuffer bb;
	
	public GameTrailPainter(Trail trail)
	{
		this.trail = trail;
		
		program = GameResourceManager.getInstance().getShaderProgramByName("trail");
	}
	
	@Override
	public void draw() {
		// get vertices from trail and build a vertexBuffer
		vertices = trail.getPathVertices();
		
		bb = ByteBuffer.allocateDirect(vertices.length * 4);
		bb.order(ByteOrder.nativeOrder());

		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		// begin drawing
		GLES20.glUseProgram(program.getProgram());
		// vertex position
		int mPositionHandle = GLES20.glGetAttribLocation(program.getProgram(), "vPosition");
		
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		
		GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, COORDS_PER_VERTEX * 4, vertexBuffer);
		
		// mvpMatrix
		int mvpMatrixHandle = GLES20.glGetUniformLocation(program.getProgram(), "uMVPMatrix");
		GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, trail.getMVPMatrix(), 0);
		
		// draw
		GLES20.glLineWidth(2.0f);
		GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, vertices.length/3);
		
		GLES20.glDisableVertexAttribArray(mPositionHandle);
	}

}
