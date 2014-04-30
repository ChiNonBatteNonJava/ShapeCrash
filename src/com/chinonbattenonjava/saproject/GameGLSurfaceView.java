package com.chinonbattenonjava.saproject;


import Physic.PhysicsWorld;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class GameGLSurfaceView extends GLSurfaceView implements
		SurfaceHolder.Callback {

	public GameGLSurfaceView(Context context) {
		super(context);

		GameResourceManager.getInstance().bindAndroidResources(getResources());

		setEGLContextClientVersion(2);
		//setEGLConfigChoos er(8, 8, 8, 8, 16, 0);
		setRenderer(new GameRenderer());

		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}

	public boolean onTouchEvent(final MotionEvent event) {
		int action = event.getAction();
		switch (action) {

		case MotionEvent.ACTION_DOWN: {// MotionEvent class field
			if (event.getX() < 400) {
				PhysicsWorld.instance("MainWorld").getVheicle("car0")
						.LeftSteering();
				return true;
			} else {
				PhysicsWorld.instance("MainWorld").getVheicle("car0")
						.RightSteering();
				return true;
			}
		}
		case MotionEvent.ACTION_UP:
			PhysicsWorld.instance("MainWorld").getVheicle("car0")
					.SetSteering(0);
			return true;

		}
		return true;
	}

}
