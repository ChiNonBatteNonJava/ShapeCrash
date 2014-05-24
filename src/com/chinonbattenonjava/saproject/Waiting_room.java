package com.chinonbattenonjava.saproject;

import org.json.simple.JSONObject;

import ClientSide.Client;
import Physic.PhysicsWorld;
import android.app.Activity;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;


public class Waiting_room extends Activity {
	private GLSurfaceView mGLView;
	private MediaPlayer mp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_waiting_room);
		Log.i("bnf","1");
		mp = MediaPlayer.create(Waiting_room.this, R.raw.song);
		Log.i("bnf","2");
		mp.setLooping(true);
		Log.i("bnf","3");
		mp.start();
		Log.i("bnf","4");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Log.i("bnf","5");
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		Log.i("bnf","6");
		mGLView = new GameGLSurfaceView(this);
		Log.i("bnf","7");
		setContentView(mGLView);
		Log.i("bnf","8");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.waiting_room, menu);
		return true;
	}

	public void onDestroy() {

		GameState.getInstance().reset();
		GameResourceManager.getInstance().reset();
		PhysicsWorld.reset();
		JSONObject jsn = new JSONObject();
		jsn.put("code", 101);
		Client c1 = Client.getInstance();
		String mess = c1.createRoom(jsn.toString());
		super.onDestroy();

	}

}
