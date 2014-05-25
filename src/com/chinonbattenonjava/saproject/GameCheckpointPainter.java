package com.chinonbattenonjava.saproject;

import android.opengl.GLES20;

public class GameCheckpointPainter implements IPainter{
	
	private Checkpoint checkpoint;
	private static final String CHECKPOINT_MODEL_FILE = "checkpoint4.obj";
	private Game3DModel m;
	private GameShaderProgram program;
	
	public GameCheckpointPainter(Checkpoint check){
		checkpoint = check;
		
		GameResourceManager.getInstance().load3DObjModel(CHECKPOINT_MODEL_FILE);
		m = GameResourceManager.getInstance().get3DModelByName(CHECKPOINT_MODEL_FILE);
		
		program = GameResourceManager.getInstance().getShaderProgramByName("sphere");
	}
	
	@Override
	public void draw() {
		GLES20.glUseProgram(program.getProgram());

		int mPositionHandle = GLES20.glGetAttribLocation(program.getProgram(),
				"vPosition");

		GLES20.glEnableVertexAttribArray(mPositionHandle);
		m.getVertexBuffer().position(0);
		GLES20.glVertexAttribPointer(mPositionHandle, 3,
				GLES20.GL_FLOAT, false, m.COORDS_PER_VERTEX * 4,
				m.getVertexBuffer());
		 
		m.getVertexBuffer().position(5);
		int mNormal = GLES20.glGetAttribLocation(program.getProgram(),
					"vNormal");
		GLES20.glEnableVertexAttribArray(mNormal);
		GLES20.glVertexAttribPointer(mNormal, 3,
						GLES20.GL_FLOAT, true, m.COORDS_PER_VERTEX * 4,
						m.getVertexBuffer().position(5));

		// mvpMatrix
		int mvpMatrixHandle = GLES20.glGetUniformLocation(program.getProgram(),
				"uMVPMatrix");
		GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false,
				checkpoint.getMVPMatrix(), 0);
		
		// normalMatrix
		int normalMatrixHandle = GLES20.glGetUniformLocation(program.getProgram(), "uNormalMatrix");
		GLES20.glUniformMatrix4fv(normalMatrixHandle, 1, false, checkpoint.getNormalMatrix(), 0);
		
		// color
		int colorHandle = GLES20.glGetUniformLocation(program.getProgram(), "uColor");
		GLES20.glUniform3fv(colorHandle, 1, checkpoint.getColor(), 0);

		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, m.getVertexCount());
		
		GLES20.glDisableVertexAttribArray(mPositionHandle);
		GLES20.glDisableVertexAttribArray(mNormal);
	}

}
