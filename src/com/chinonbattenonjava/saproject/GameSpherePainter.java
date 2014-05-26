package com.chinonbattenonjava.saproject;

import android.opengl.GLES20;

public class GameSpherePainter implements IPainter {
	private static final String SPHERE_MODEL_FILE = "sphere.obj";

	private Sphere sphere;
	
	private Game3DModel m;
	private GameShaderProgram program;
	
	public GameSpherePainter(Sphere s)
	{
		this.sphere = s;
		
		GameResourceManager.getInstance().load3DObjModel(SPHERE_MODEL_FILE);
		m = GameResourceManager.getInstance().get3DModelByName(SPHERE_MODEL_FILE);
		
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
				sphere.getMVPMatrix(), 0);
		
		// normalMatrix
		int normalMatrixHandle = GLES20.glGetUniformLocation(program.getProgram(), "uNormalMatrix");
		GLES20.glUniformMatrix4fv(normalMatrixHandle, 1, false, sphere.getNormalMatrix(), 0);
		
		// color
		int colorHandle = GLES20.glGetUniformLocation(program.getProgram(), "uColor");
		GLES20.glUniform3fv(colorHandle, 1, sphere.getColor(), 0);
		
		// lightPos
		int lightPosHandle = GLES20.glGetUniformLocation(program.getProgram(), "uLightPos");
		GLES20.glUniform4fv(lightPosHandle, 1, GameState.getInstance().getLight("MainLight").getEyeSpacePosition(), 0);
		
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, m.getVertexCount());
		
		GLES20.glDisableVertexAttribArray(mPositionHandle);
		GLES20.glDisableVertexAttribArray(mNormal);
		
		/*// draw outline
		GameShaderProgram black = GameResourceManager.getInstance().getShaderProgramByName("black");
		GLES20.glUseProgram(black.getProgram());
		
		int mPosHandle = GLES20.glGetAttribLocation(black.getProgram(),
				"vPosition");

		GLES20.glEnableVertexAttribArray(mPosHandle);
		GLES20.glVertexAttribPointer(mPosHandle, 3,
				GLES20.GL_FLOAT, false, 3 * 4,
				m.getReversedVertexBuffer());
		
		// mvpMatrix
		int mvpMtxHandle = GLES20.glGetUniformLocation(black.getProgram(),
				"uMVPMatrix");
		GLES20.glUniformMatrix4fv(mvpMtxHandle, 1, false,
				sphere.getMVPMatrix(), 0);
		
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, m.getVertexCount());
		
		GLES20.glDisableVertexAttribArray(mPosHandle);*/
	}

}
