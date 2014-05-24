package com.chinonbattenonjava.saproject;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import ClientSide.Client;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class Settings extends Activity {
	private SeekBar seekBar;
	private TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		seekBar = (SeekBar) findViewById(R.id.seekBar1);
		textView = (TextView) findViewById(R.id.textView1);
		int max;
		max = seekBar.getMax();
		max += 2;
		textView.setText(seekBar.getProgress() + 2 + "/" + max);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int progress = 0;

			@Override
			public void onProgressChanged(SeekBar seekBar, int progresValue,
					boolean fromUser) {
				progress = progresValue;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// Do something here,
				// if you want to do anything at the start of
				// touching the seekbar
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// Display the value in textview
				int max = seekBar.getMax();
				max = max + 2;
				textView.setText(progress + 2 + "/" + max);
			}
		});
		Button bt1 = (Button) findViewById(R.id.button1);
		bt1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i("bnfcroom", "button pressed");
				seekBar = (SeekBar) findViewById(R.id.seekBar1);
				seekBar.getProgress();
				int pl1 = seekBar.getProgress();
				pl1 = pl1 + 2;
				EditText et = (EditText) findViewById(R.id.editText1);
				String name = et.getText().toString();
				JSONObject jsn = new JSONObject();
				jsn.put("code", 2);
				jsn.put("room_name", name);
				jsn.put("room_password", "");
				jsn.put("password_request", 0);
				JSONObject set = new JSONObject();
				set.put("max_player", pl1);
				jsn.put("settings", set);

				Client c1 = Client.getInstance();
				String mess = c1.createRoom(jsn.toString());
				Log.i("bnfcroom", mess);
				try {
					JSONObject json = (JSONObject) new JSONParser().parse(mess);
					Log.i("bnf","1111");
					if ((Long) json.get("code") == 2) {
						Display display = getWindowManager().getDefaultDisplay();
						Point size = new Point();
						display.getSize(size);
						GameResourceManager.getInstance().setSreenSize(new Point(size.x,size.y));
						Long pid = (Long) json.get("player_id");
						Log.i("bnf",pid.toString());
						GameResourceManager.getInstance().addPlayer("" + pid);
						GameResourceManager.getInstance().setPlayerName("" + pid);
						
						Client.getInstance().startGame(pid.intValue());
						/*
						 * JSONArray plist = new JSONArray(); plist =
						 * (JSONArray) risp.get("players"); Iterator it =
						 * plist.iterator(); String listaPlayer[] = new
						 * String[plist.size()]; int count = 0; while
						 * (it.hasNext()) { JSONObject rum = new JSONObject();
						 * rum = (JSONObject) it.next(); Long player1 = (Long)
						 * rum.get("id"); listaPlayer[count] = "" + player1;
						 * count++; }
						 * 
						 * GameResourceManager.getInstance().addPlayer(listaPlayer
						 * );
						 */
						Intent intent = new Intent(Settings.this, Waiting_room.class);
						startActivity(intent);
						Log.i("bnf","777");
					} else {
						Toast.makeText(Settings.this, "42", Toast.LENGTH_SHORT)
								.show();

					}
				} catch (Exception e) {
					Log.i("bnf", e.getMessage());
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

}
