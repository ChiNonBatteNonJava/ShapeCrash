package com.chinonbattenonjava.saproject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.util.Log;

public class Game3DModel {
	// class TAG for Log
	private static final String TAG = "Game3DModel";
	
	public   int COORDS_PER_VERTEX = 3;
	
	private float[] vertices;
	private FloatBuffer vertexBuffer;
	private int vertexCount;
	
	public Game3DModel(float[] vertices)
	{
		this.vertices = vertices;
		this.vertexCount = 0;
	}
	public Game3DModel(float[] vertices,int cordXvertex)
	{
		this.vertices = vertices;
		this.vertexCount = 0;
		this.COORDS_PER_VERTEX=cordXvertex;
	}
	
	
	public float[] getVertices()
	{
		return vertices;
	}
	
	public FloatBuffer getVertexBuffer()
	{
		if (vertices == null)
		{
			Log.e(TAG, "getVertexBuffer(): vertices not yet defined!");
			return null;
		}
		
		if (vertexBuffer == null)
		{
			ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
			bb.order(ByteOrder.nativeOrder());
			
			vertexBuffer = bb.asFloatBuffer();
			
			vertexBuffer.put(vertices);
			
			vertexBuffer.position(0);
			
			vertexCount = vertices.length / COORDS_PER_VERTEX;
		}
		
		return vertexBuffer;
	}
	
	public int getVertexCount()
	{
		return vertexCount;
	}
}
