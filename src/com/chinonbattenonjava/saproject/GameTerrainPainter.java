package com.chinonbattenonjava.saproject;

import android.opengl.GLES20;

public class GameTerrainPainter implements IPainter {
	private static final String TERRAIN_MODEL = "pista2.obj";
	private Game3DModel m;
	private GameShaderProgram program;
	private Terrain terrain;
	private int texture;

	public GameTerrainPainter(Terrain t) {
		
		GameResourceManager.getInstance().load3DObjModel(TERRAIN_MODEL);
		GameResourceManager.getInstance().loadTexture("terrain.png");
		m = GameResourceManager.getInstance().get3DModelByName(TERRAIN_MODEL);
		program = GameResourceManager.getInstance().getShaderProgramByName("terrain");
		texture= GameResourceManager.getInstance().getTexture("terrain.png");
		
		terrain=t;
	}
	public Game3DModel getGame3dModel(){
		return this.m;
	}

	@Override
	public void draw() {
		
		GLES20.glUseProgram(program.getProgram());

		// vertex position
		int mPositionHandle = GLES20.glGetAttribLocation(program.getProgram(),
				"vPosition");

		GLES20.glEnableVertexAttribArray(mPositionHandle);
		m.getVertexBuffer().position(0);
		GLES20.glVertexAttribPointer(mPositionHandle, 3,
				GLES20.GL_FLOAT, false, m.COORDS_PER_VERTEX * 4,
				m.getVertexBuffer());
		
		
		m.getVertexBuffer().position(3);
		int mUvs = GLES20.glGetAttribLocation(program.getProgram(),
				"vUvs");
		 GLES20.glEnableVertexAttribArray(mUvs);

		 GLES20.glVertexAttribPointer(mUvs, 2,
					GLES20.GL_FLOAT, true, m.COORDS_PER_VERTEX * 4,
					m.getVertexBuffer());
		 
		m.getVertexBuffer().position(5);
		 int mNormal = GLES20.glGetAttribLocation(program.getProgram(),
					"vNormal");
		 GLES20.glEnableVertexAttribArray(mNormal);
			 GLES20.glVertexAttribPointer(mNormal, 3,
						GLES20.GL_FLOAT, true, m.COORDS_PER_VERTEX * 4,
						m.getVertexBuffer().position(5));

		// mvpMatsrix
		int mvpMatrixHandle = GLES20.glGetUniformLocation(program.getProgram(),
				"uMVPMatrix");
		
		GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false,
				terrain.getMVPMatrix(), 0);
		
		// normalMatrix
		int normalMatrixHandle = GLES20.glGetUniformLocation(program.getProgram(), "uNormalMatrix");
		GLES20.glUniformMatrix4fv(normalMatrixHandle, 1, false, terrain.getNormalMatrix(), 0);
		
		// color
		int colorHandle = GLES20.glGetUniformLocation(program.getProgram(), "uColor");
		GLES20.glUniform3fv(colorHandle, 1, terrain.getColor(), 0);
		
		// uLightPos
		int lightPosHandle = GLES20.glGetUniformLocation(program.getProgram(), "uLightPos");
		GLES20.glUniform4fv(lightPosHandle, 1, GameState.getInstance().getLight("MainLight").getEyeSpacePosition(), 0);
		
		GLES20.glEnable(GLES20.GL_TEXTURE_2D);
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

		// Bind the texture to this unit.
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
		
		//get set sample texture
		int mTextureUniformHandle = GLES20.glGetUniformLocation(program.getProgram(),
				"u_Texture");

		GLES20.glUniform1i(mTextureUniformHandle, 0);

		// draw
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, m.getVertexCount());
		GLES20.glDisable(GLES20.GL_TEXTURE_2D);
		GLES20.glDisableVertexAttribArray(mPositionHandle);
		GLES20.glDisableVertexAttribArray(mNormal);
		GLES20.glDisableVertexAttribArray(mUvs);
		
	}

}
