package com.chinonbattenonjava.saproject;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class GameCarGUI extends GameComponentGUI<Car> {
	float[] mModelMatrix = new float[16];
	float[] mvpMatrix = new float[16];
	GameCamera gm;
	private static final String CAR_MODEL_FILE = "freccia.obj";
	private static final String CAR_WHELL_FILE = "quadrante.obj";
	private static final String RETRO_BUTTON="retroButton.obj";
	
	
	private Game3DModel m;
	private Game3DModel quad;
	private Game3DModel button;
	private GameShaderProgram program;
//	private GameShaderProgram program1;
	private int texture;
	private int texture1;
	public GameCarGUI(Car element) {
		super(element);
		GameResourceManager.getInstance().load3DObjModel(CAR_MODEL_FILE);
		m = GameResourceManager.getInstance().get3DModelByName(CAR_MODEL_FILE);
		
		GameResourceManager.getInstance().load3DObjModel(RETRO_BUTTON);
		button = GameResourceManager.getInstance().get3DModelByName(RETRO_BUTTON);

		GameResourceManager.getInstance().load3DObjModel(CAR_WHELL_FILE);
		
		GameResourceManager.getInstance().loadTexture("car.png");
		texture= GameResourceManager.getInstance().getTexture("car.png");
		GameResourceManager.getInstance().loadTexture("quadrante.png");
		texture1= GameResourceManager.getInstance().getTexture("quadrante.png");
		
		
		
		
		quad = GameResourceManager.getInstance().get3DModelByName(
				CAR_WHELL_FILE);

		program = GameResourceManager.getInstance().getShaderProgramByName(
				"speed");
		
		Matrix.setIdentityM(mModelMatrix, 0);
		Matrix.translateM(mModelMatrix, 0, 0, 1, 1);
		// Matrix.rotate M(mModelMatrix, 0, 30, 1, 0, 0);

	}

	private void drawRetroButton(float[] proj, float[] look){
		Matrix.setIdentityM(mModelMatrix, 0);

		Matrix.translateM(mModelMatrix, 0, 0, 1.5f, 0);
		

		Matrix.multiplyMM(mvpMatrix, 0, look, 0, mModelMatrix, 0);
		Matrix.multiplyMM(mvpMatrix, 0, proj, 0, mvpMatrix, 0);

		GLES20.glUseProgram(program.getProgram());
	
		int mPositionHandle = GLES20.glGetAttribLocation(program.getProgram(),
				"vPosition");

		GLES20.glEnableVertexAttribArray(mPositionHandle);
		button.getVertexBuffer().position(0);
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
				false, quad.COORDS_PER_VERTEX * 4, button.getVertexBuffer());

		button.getVertexBuffer().position(3);
		int mUvs = GLES20.glGetAttribLocation(program.getProgram(), "vUvs");
		GLES20.glEnableVertexAttribArray(mUvs);

		GLES20.glVertexAttribPointer(mUvs, 2, GLES20.GL_FLOAT, true,
				quad.COORDS_PER_VERTEX * 4, button.getVertexBuffer());

		button.getVertexBuffer().position(5);
		int mNormal = GLES20.glGetAttribLocation(program.getProgram(),
				"vNormal");
		GLES20.glEnableVertexAttribArray(mNormal);
		GLES20.glVertexAttribPointer(mNormal, 3, GLES20.GL_FLOAT, true,
				button.COORDS_PER_VERTEX * 4, button.getVertexBuffer().position(5));

		int mvpMatrixHandle = GLES20.glGetUniformLocation(program.getProgram(),
				"uMVPMatrix");
		GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);
		
		GLES20.glEnable(GLES20.GL_TEXTURE_2D);
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

		// Bind the texture to this unit.
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture1 );
		int mTextureUniformHandle;

		//get set sample texture
		mTextureUniformHandle = GLES20.glGetUniformLocation(program.getProgram(),
				"u_Texture");

		GLES20.glUniform1i(mTextureUniformHandle, 0);

		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0,button.getVertexCount());

		GLES20.glDisable(GLES20.GL_TEXTURE_2D);
		GLES20.glDisableVertexAttribArray(mPositionHandle);
		GLES20.glDisableVertexAttribArray(mNormal);
		GLES20.glDisableVertexAttribArray(mUvs);
	}
	
	private void drawQuadrants(float[] proj, float[] look) {
		
		Matrix.setIdentityM(mModelMatrix, 0);

		Matrix.translateM(mModelMatrix, 0, 0, -2, -2.5f);
		

		Matrix.multiplyMM(mvpMatrix, 0, look, 0, mModelMatrix, 0);
		Matrix.multiplyMM(mvpMatrix, 0, proj, 0, mvpMatrix, 0);

		GLES20.glUseProgram(program.getProgram());
	
		int mPositionHandle = GLES20.glGetAttribLocation(program.getProgram(),
				"vPosition");

		GLES20.glEnableVertexAttribArray(mPositionHandle);
		quad.getVertexBuffer().position(0);
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
				false, quad.COORDS_PER_VERTEX * 4, quad.getVertexBuffer());

		quad.getVertexBuffer().position(3);
		int mUvs = GLES20.glGetAttribLocation(program.getProgram(), "vUvs");
		GLES20.glEnableVertexAttribArray(mUvs);

		GLES20.glVertexAttribPointer(mUvs, 2, GLES20.GL_FLOAT, true,
				quad.COORDS_PER_VERTEX * 4, quad.getVertexBuffer());

		quad.getVertexBuffer().position(5);
		int mNormal = GLES20.glGetAttribLocation(program.getProgram(),
				"vNormal");
		GLES20.glEnableVertexAttribArray(mNormal);
		GLES20.glVertexAttribPointer(mNormal, 3, GLES20.GL_FLOAT, true,
				quad.COORDS_PER_VERTEX * 4, quad.getVertexBuffer().position(5));

		int mvpMatrixHandle = GLES20.glGetUniformLocation(program.getProgram(),
				"uMVPMatrix");
		GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);
		
		GLES20.glEnable(GLES20.GL_TEXTURE_2D);
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

		// Bind the texture to this unit.
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture1 );
		int mTextureUniformHandle;

		//get set sample texture
		mTextureUniformHandle = GLES20.glGetUniformLocation(program.getProgram(),
				"u_Texture");

		GLES20.glUniform1i(mTextureUniformHandle, 0);

		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0,quad.getVertexCount());

		GLES20.glDisable(GLES20.GL_TEXTURE_2D);
		GLES20.glDisableVertexAttribArray(mPositionHandle);
		GLES20.glDisableVertexAttribArray(mNormal);
		GLES20.glDisableVertexAttribArray(mUvs);

	}

	@Override
	public void draw(float[] proj, float[] look) {
		if(this.elemet.getCar()!=null){
		drawQuadrants(proj,look);
		drawRetroButton(proj,look);
		GLES20.glUseProgram(program.getProgram());
		float speed = this.elemet.getCar().getSpeedKMH();

		GLES20.glUseProgram(program.getProgram());
		Matrix.setIdentityM(mModelMatrix, 0);

		Matrix.translateM(mModelMatrix, 0, 0, -2, -2.5f);
		Matrix.rotateM(mModelMatrix, 0,Math.max(70 - speed, -70), 1, 0, 0);

		Matrix.multiplyMM(mvpMatrix, 0, look, 0, mModelMatrix, 0);
		Matrix.multiplyMM(mvpMatrix, 0, proj, 0, mvpMatrix, 0);

		
		int mPositionHandle = GLES20.glGetAttribLocation(program.getProgram(),
				"vPosition");

		GLES20.glEnableVertexAttribArray(mPositionHandle);
		m.getVertexBuffer().position(0);
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
				false, m.COORDS_PER_VERTEX * 4, m.getVertexBuffer());

		m.getVertexBuffer().position(3);
		int mUvs = GLES20.glGetAttribLocation(program.getProgram(), "vUvs");
		GLES20.glEnableVertexAttribArray(mUvs);

		GLES20.glVertexAttribPointer(mUvs, 2, GLES20.GL_FLOAT, true,
				m.COORDS_PER_VERTEX * 4, m.getVertexBuffer());

		m.getVertexBuffer().position(5);
		int mNormal = GLES20.glGetAttribLocation(program.getProgram(),
				"vNormal");
		GLES20.glEnableVertexAttribArray(mNormal);
		GLES20.glVertexAttribPointer(mNormal, 3, GLES20.GL_FLOAT, true,
				m.COORDS_PER_VERTEX * 4, m.getVertexBuffer().position(5));

		int mvpMatrixHandle = GLES20.glGetUniformLocation(program.getProgram(),
				"uMVPMatrix");
		GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);
		GLES20.glEnable(GLES20.GL_TEXTURE_2D);
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

		// Bind the texture to this unit.
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
		int mTextureUniformHandle;

		//get set sample texture
		mTextureUniformHandle = GLES20.glGetUniformLocation(program.getProgram(),
				"u_Texture");

		GLES20.glUniform1i(mTextureUniformHandle, 0);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, m.getVertexCount());

		GLES20.glDisable(GLES20.GL_TEXTURE_2D);
		GLES20.glDisableVertexAttribArray(mPositionHandle);
		GLES20.glDisableVertexAttribArray(mNormal);
		GLES20.glDisableVertexAttribArray(mUvs);
		}
	}
}
