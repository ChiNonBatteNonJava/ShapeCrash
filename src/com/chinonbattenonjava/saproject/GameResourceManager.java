package com.chinonbattenonjava.saproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.Log;

public class GameResourceManager {
	// class TAG for Log
	private static final String TAG = "GameResourceManager";
	
	// constants
	private static final String shadersPath = "shaders";
	private static final String modelsPath = "models";
	
	// singleton instance
	private static GameResourceManager instance;
	
	// locals
	private Resources androidRes;
	private HashMap<String, GameShader> shaders;
	private HashMap<String, Game3DModel> models;
	
	private GameResourceManager()
	{
		shaders = new HashMap<String, GameShader>();
		models = new HashMap<String, Game3DModel>();
		
		androidRes = null;
	}
	
	public static GameResourceManager getInstance()
	{
		if (instance == null)
			instance = new GameResourceManager();
		return instance;
	}
	
	public static String getShadersPath()
	{
		return shadersPath;
	}
	
	public static String getModelsPath()
	{
		return modelsPath;
	}
	
	public void bindAndroidResources(Resources res)
	{
		androidRes = res;
	}
	
	public Resources getAndroidResources()
	{
		return androidRes;
	}
	
	public GameShader getShaderByName(String shaderName)
	{
		return shaders.get(shaderName);
	}
	
	public Game3DModel get3DModelByName(String modelName)
	{
		return models.get(modelName);
	}
	
	public void loadShaders()
	{
		if (androidRes != null)
		{
			try {
				for (String shaderFileName : androidRes.getAssets().list(shadersPath))
				{
					// check shader type
					int type = 0;
					if (shaderFileName.endsWith(".ps"))
						type = GLES20.GL_FRAGMENT_SHADER;
					else if (shaderFileName.endsWith(".vs"))
						type = GLES20.GL_VERTEX_SHADER;
					
					if (type != 0) // valid shader type
					{
						// read file
						BufferedReader reader = new BufferedReader(new InputStreamReader(androidRes.getAssets().open(GameResourceManager.getShadersPath() + File.separator + shaderFileName)));
						
						String shaderCode = "";
						
						String line = null;
						while ((line = reader.readLine()) != null) {
						    shaderCode = shaderCode + line + " \n ";
						}
						
						// initialize GameShader object
						GameShader s = new GameShader(type);
						
						s.setShaderName(shaderFileName);
						
						s.setShaderCode(shaderCode);
						
						// add to collection
						shaders.put(s.getShaderName(), s);
						
						Log.d(TAG, "Shader code loaded: " + s.getShaderName());
					}
				}
			} catch (IOException e) {
	            e.printStackTrace();
			}
		}
		else
		{
			Log.e(TAG, "loadShadersCode() called before Android Resources binding!");
		}
	}
	
	public void compileShaders()
	{
		for (HashMap.Entry<String, GameShader> entry : shaders.entrySet())
		{
			GameShader s = entry.getValue();
			
			s.compileShader();
		}
	}
	
	
	public void load3DObjModel(String fileName){
		if (androidRes != null && GameResourceManager.getInstance().get3DModelByName(fileName) == null)
		{
			try {
				class IndexHelper{
					public int vertex;
					public int normal;
					public int uvs;
					public IndexHelper(String v){
						String[] l=v.split("/");
						vertex=Integer.parseInt(l[0])-1;
						normal=Integer.parseInt(l[2])-1;
						uvs=Integer.parseInt(l[1])-1;	
					}
					
				}
				// read file
				BufferedReader reader = new BufferedReader(new InputStreamReader(androidRes.getAssets().open(GameResourceManager.getModelsPath() + File.separator + fileName)));
				List<Vector3> vertices=new ArrayList<Vector3>();
				List<Vector3> normal=new ArrayList<Vector3>();
				List<Vector2> uvs=new ArrayList<Vector2>();
				List<IndexHelper> index=new ArrayList<IndexHelper>();
				
				String line = null;
				while ((line = reader.readLine()) != null) {
					line=line.trim();
					//uvs
					if(line.startsWith("vt")){
						line=line.replace("vt", "");
						line=line.trim();
						String[] v=line.split(" ");
						uvs.add(new Vector2(Float.parseFloat(v[0]),Float.parseFloat(v[1])));
					}else if(line.startsWith("vn")){
						
						line=line.replace("vn", "");
						line=line.trim();
						String[] v=line.split(" ");
						normal.add(new Vector3(Float.parseFloat(v[0]),Float.parseFloat(v[1]),Float.parseFloat(v[2])));
						
					}else if(line.startsWith("v")){
						
						line=line.replace("v", "");
						line=line.trim();
						String[] v=line.split(" ");
						vertices.add(new Vector3(Float.parseFloat(v[0]),Float.parseFloat(v[1]),Float.parseFloat(v[2])));
						
					}else if(line.startsWith("f")){
						line=line.replace("f", "");
						line=line.trim();
						String[] v=line.split(" ");
						index.add(new IndexHelper(v[0]));
						index.add(new IndexHelper(v[1]));
						index.add(new IndexHelper(v[2]));
						
					}	
				}
				float[] verticesFloat = new float[index.size()*8];
				int a=0;
				for (IndexHelper i:index){
					verticesFloat[a]=vertices.get(i.vertex).x; a++;
					verticesFloat[a]=vertices.get(i.vertex).y; a++;
					verticesFloat[a]=vertices.get(i.vertex).z; a++;
					
					verticesFloat[a]=uvs.get(i.uvs).x; a++;
					verticesFloat[a]=uvs.get(i.uvs).y; a++;
					
					verticesFloat[a]=normal.get(i.normal).x; a++;
					verticesFloat[a]=normal.get(i.normal).y; a++;
					verticesFloat[a]=normal.get(i.normal).z; a++;

				}
				// initialize Game3DModel
				Game3DModel m = new Game3DModel(verticesFloat,8);
				
				// add to collection
				models.put(fileName, m);
				
				Log.e(TAG, "load3DModelsObj() Sucesfull!");	
			} catch (IOException e) {
	            e.printStackTrace();
			}
		
		}
		else
		{
			Log.e(TAG, "load3DModels() called before Android Resources binding!");
		}
	
		
	}
	

	

	
	public void load3DModel(String modelFileName)
	{
		if (androidRes != null && GameResourceManager.getInstance().get3DModelByName(modelFileName) == null)
		{
			try {
				// read file
				BufferedReader reader = new BufferedReader(new InputStreamReader(androidRes.getAssets().open(GameResourceManager.getModelsPath() + File.separator + modelFileName)));
				
				String modelString = "";
				
				String line = null;
				while ((line = reader.readLine()) != null) {
					modelString = modelString + line + " ";
				}
				
				String[] verticesString = modelString.split(",");
				float[] vertices = new float[verticesString.length];
				
				Log.d(TAG, modelString);
				
				for (int i = 0; i < vertices.length; i++)
				{
					vertices[i] = Float.parseFloat(verticesString[i]);
				}
				
				// initialize Game3DModel
				Game3DModel m = new Game3DModel(vertices);
				
				// add to collection
				models.put(modelFileName, m);
				
				Log.d(TAG, "Model loaded: " + modelFileName);
			} catch (IOException e) {
	            e.printStackTrace();
			}
		}
		else
		{
			Log.e(TAG, "load3DModels() called before Android Resources binding!");
		}
	}
}
