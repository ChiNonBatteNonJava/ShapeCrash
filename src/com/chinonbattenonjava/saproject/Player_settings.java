package com.chinonbattenonjava.saproject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class Player_settings extends Activity {
	private boolean visuale;
	private String name;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_settings);
	}

	public void done(View v){
		Switch s = (Switch) findViewById(R.id.switch1);
		visuale = s.isClickable();	
		EditText etlextraterrestre = (EditText) findViewById(R.id.editText1);
		name = etlextraterrestre.getText().toString();
		GameState.getInstance().changeCamState(visuale);
		GameState.getInstance().setName(name);
		Intent i = new Intent(Player_settings.this, MainActivity.class);
		startActivity(i);
		}

}
