package com.chinonbattenonjava.saproject;

import android.opengl.Matrix;

import com.badlogic.gdx.math.Vector3;

public class CheckpointBox {
	
	float orientation;
	float[] min, max;
	
	public CheckpointBox(Vector3 pos, float ori){
		orientation = ori;
		
		this.min = new float[4];
		this.max = new float[4];
		
		float[] min = {-1f, -1f, -15f, 0f};
		float[] max = {1f, 15f, 15f, 0f};
		
		float[] m = new float[16];
		Matrix.setIdentityM(m, 0);
		Matrix.rotateM(m, 0, ori, 0, 1, 0);
		Matrix.multiplyMV(this.min, 0, m, 0, min, 0);
		Matrix.multiplyMV(this.max, 0, m, 0, max, 0);
		
		this.min[0] = this.min[0]+pos.x;
		this.min[1] = this.min[1]+pos.y;
		this.min[2] = this.min[2]+pos.z;
		this.max[0] = this.max[0]+pos.x;
		this.max[1] = this.max[1]+pos.y;
		this.max[2] = this.max[2]+pos.z;
		
				
	}
	
	public boolean isInBox(Vector3 pos){
		if(min[0]<pos.x && max[0]>pos.x && min[1]<pos.y && max[1]>pos.y && min[2]<pos.z && max[2]>pos.z){
			return true;
		}
		return false;
	}
}
