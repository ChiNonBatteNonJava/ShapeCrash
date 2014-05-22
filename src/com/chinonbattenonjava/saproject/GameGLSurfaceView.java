package com.chinonbattenonjava.saproject;


import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.badlogic.gdx.math.Vector2;

public class GameGLSurfaceView extends GLSurfaceView implements
		SurfaceHolder.Callback {
	
	private GameEventListener gm;
	public GameGLSurfaceView(Context context) {
		super(context);
		
		gm=new GameEventListener(new CommandListDeclaration());
	
		GameResourceManager.getInstance().bindAndroidResources(getResources());

		setEGLContextClientVersion(2);
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		setRenderer(new GameRenderer());
		//this.setPreserveEGLContextOnPause(true);
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		
		CarActionBuilder.Create(GameResourceManager.getInstance().getCar(GameResourceManager.getInstance().getPlayerName()),gm);
		
		
		
	}
	public boolean onTouchEvent(final MotionEvent event) {
		return gm.onTouchEvent(event);
	}
	
	public void surfaceDestroyed(SurfaceHolder h){
		Log.i("pippo", "destroy");
		super.surfaceDestroyed(h);
	}
	
	public void onResume(){
		Log.i("pippo", "resume");
		super.onResume();
	}
	
	public void onPause(){
		Log.i("pippo", "pause");
		super.onPause();
	}
}
