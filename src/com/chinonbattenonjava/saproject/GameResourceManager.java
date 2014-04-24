package com.chinonbattenonjava.saproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

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
