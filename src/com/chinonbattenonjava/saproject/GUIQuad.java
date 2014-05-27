package com.chinonbattenonjava.saproject;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.badlogic.gdx.math.Vector2;

public class GUIQuad {
	
	private Vector2 center;
	private Vector2 scale;
	
	private int texture;
	
	private GameShaderProgram program;
	private Game3DModel m;
	
	private float[] projM;
	private float[] mModelMatrix;
	private float[] mViewMatrix;
	private float[] mvpMatrix;
	
	public GUIQuad(Vector2 center, Vector2 scale, String textureName)
	{
		this.center = center;
		this.scale = scale;
		
		program = GameResourceManager.getInstance().getShaderProgramByName("quad");
		GameResourceManager.getInstance().load3DObjModel("quad.obj");
		m = GameResourceManager.getInstance().get3DModelByName("quad.obj");
		
		GameResourceManager.getInstance().loadTexture(textureName);
		texture = GameResourceManager.getInstance().getTexture(textureName);
		
		projM = new float[16];
		mModelMatrix = new float[16];
		mViewMatrix = new float[16];
		mvpMatrix = new float[16];
		
		
		Matrix.setIdentityM(mModelMatrix, 0);
		Matrix.scaleM(mModelMatrix, 0, scale.x, scale.y, 1.0f);
		Matrix.translateM(mModelMatrix, 0, 0, 1, 0.0f);
		
		Matrix.setLookAtM(mViewMatrix, 0, 3, 0, 0, 0, 0, 0, 1, 0,0);
		Matrix.orthoM(projM, 0, -4, 4, -2, 2, 0.1f, 10);
		
		Matrix.multiplyMM(mvpMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
		Matrix.multiplyMM(mvpMatrix, 0, projM, 0, mvpMatrix, 0);
	}
	
	public void draw()
	{	
		GLES20.glUseProgram(program.getProgram());
		
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		
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
		 
		// mvpMatsrix
		int mvpMatrixHandle = GLES20.glGetUniformLocation(program.getProgram(),
				"uMVPMatrix");
		GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false,
				mvpMatrix, 0);
		
		// texture
		GLES20.glEnable(GLES20.GL_TEXTURE_2D);
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

		// Bind the texture to this unit.
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
		int mTextureUniformHandle;

		//get set sample texture
		mTextureUniformHandle = GLES20.glGetUniformLocation(program.getProgram(),
				"u_Texture");

		GLES20.glUniform1i(mTextureUniformHandle, 0);
		
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, m.getVertexCount());
		
		GLES20.glDisableVertexAttribArray(mPositionHandle);
		GLES20.glDisableVertexAttribArray(mUvs);
		
		GLES20.glDisable(GLES20.GL_BLEND);
		
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
	}
}
