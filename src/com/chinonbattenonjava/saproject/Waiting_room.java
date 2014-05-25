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
		mp = MediaPlayer.create(Waiting_room.this, R.raw.song);
		mp.setLooping(true);
		mp.start();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mGLView = new GameGLSurfaceView(this);
		setContentView(mGLView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.waiting_room, menu);
		return true;
	}

	public void onDestroy() {
		JSONObject json = new JSONObject();
		json.put("code", 101);
		Client.getInstance().createRoom(json.toJSONString());
		Client.getInstance().stopGame();
		Client.reset();
		GameState.getInstance().reset();
		GameResourceManager.getInstance().reset();
		PhysicsWorld.reset();
		
		mp.stop();
		super.onDestroy();
		Log.i("bnf","close");
	}

}
