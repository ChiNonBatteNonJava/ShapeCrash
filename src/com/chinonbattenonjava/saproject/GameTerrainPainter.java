package com.chinonbattenonjava.saproject;

import android.opengl.GLES20;

public class GameTerrainPainter implements IPainter {
	private static final String TERRAIN_MODEL = "pi11.obj";
	private Game3DModel m;
	private GameShaderProgram program;
	private Terrain terrain;

	public GameTerrainPainter(Terrain t) {
		
		GameResourceManager.getInstance().load3DObjModel(TERRAIN_MODEL);
		m = GameResourceManager.getInstance().get3DModelByName(TERRAIN_MODEL);
		program = new GameShaderProgram(GameResourceManager.getInstance()
				.getShaderByName("terrain.vs"), GameResourceManager.getInstance()
				.getShaderByName("terrain.ps"));
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
		
	/*	
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
*/
		// mvpMatrix
		int mvpMatrixHandle = GLES20.glGetUniformLocation(program.getProgram(),
				"uMVPMatrix");
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false,
				terrain.getMVPMatrix(), 0);

		// draw
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, m.getVertexCount());
		


		GLES20.glDisableVertexAttribArray(mPositionHandle);
	//	GLES20.glDisableVertexAttribArray(mNormal);
		//GLES20.glDisableVertexAttribArray(mUvs);
	}

}
