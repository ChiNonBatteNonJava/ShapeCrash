package com.chinonbattenonjava.saproject;


import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class GameGLSurfaceView extends GLSurfaceView implements
		SurfaceHolder.Callback {
	
	private GameEventListener gm;
	public GameGLSurfaceView(Context context) {
		super(context);
		
		gm=new GameEventListener(new CommandListDeclaration());
	
		GameResourceManager.getInstance().bindAndroidResources(getResources());
		Log.i("bnf","1234");
		CarActionBuilder.Create(GameResourceManager.getInstance().getCar(GameResourceManager.getInstance().getPlayerName()), gm);
		Log.i("bnf","1244");
		setEGLContextClientVersion(2);
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		setRenderer(new GameRenderer());

		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		
		
	}
	public boolean onTouchEvent(final MotionEvent event) {
		
		
		return gm.onTouchEvent(event);
	}
	
	
	
	

}
