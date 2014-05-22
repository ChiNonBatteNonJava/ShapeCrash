package com.chinonbattenonjava.saproject;

import org.json.simple.JSONObject;

import ClientSide.Client;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class Waiting_room extends Activity {
	private GLSurfaceView mGLView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_waiting_room);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		mGLView = new GameGLSurfaceView(this);
		
		setContentView(mGLView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.waiting_room, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
//      	JSONObject jsn = new JSONObject();
//      	jsn.put("code", 101);
//      	Client c1 = Client.getInstance();
//      	String mess = c1.createRoom(jsn.toString());
	    super.onBackPressed();
		
	    return;
	}
}
