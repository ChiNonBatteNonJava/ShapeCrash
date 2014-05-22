package com.chinonbattenonjava.saproject;


import android.content.Context;
import android.opengl.GLSurfaceView;
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

		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		
	}
	public boolean onTouchEvent(final MotionEvent event) {
		return gm.onTouchEvent(event);
	}
	

}
