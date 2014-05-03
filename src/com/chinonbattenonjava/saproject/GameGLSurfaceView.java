package com.chinonbattenonjava.saproject;


import ClientSide.Client;
import Physic.PhysicsWorld;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class GameGLSurfaceView extends GLSurfaceView implements
		SurfaceHolder.Callback {

	private String playerId;
	
	public GameGLSurfaceView(Context context) {
		super(context);

		GameResourceManager.getInstance().bindAndroidResources(getResources());
		
		Client x = new Client();
		x.start();
		while(x.getCarId().equals("0")){}
		playerId = x.getCarId();
		Log.i("bnf","asdasdasdasd"+playerId);
		
		setEGLContextClientVersion(2);
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		setRenderer(new GameRenderer(playerId));

		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}

	public boolean onTouchEvent(final MotionEvent event) {
		int action = event.getAction();
		switch (action) {

		case MotionEvent.ACTION_DOWN: {// MotionEvent class field
			if (event.getX() < 400) {
				PhysicsWorld.instance("MainWorld").getVheicle(playerId)
						.LeftSteering();
				return true;
			} else {
				PhysicsWorld.instance("MainWorld").getVheicle(playerId)
						.RightSteering();
				return true;
			}
		}
		case MotionEvent.ACTION_UP:
			PhysicsWorld.instance("MainWorld").getVheicle(playerId)
					.SetSteering(0);
			return true;

		}
		return true;
	}

}
