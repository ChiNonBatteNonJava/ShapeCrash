package com.chinonbattenonjava.saproject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import ClientSide.Client;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Play extends Activity {
	private String msg;

	public class ServerOnLine {
		String Name;
		String Description;
		Long room_id;
	}

	Adapter ServerListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		GameResourceManager.getInstance().setSreenSize(new Point(size.x,size.y));
		setContentView(R.layout.activity_list_view_with_simple_adapter);
		Client c1 = Client.getInstance();
		String msg = c1.listServer();
		this.msg = msg;
		Log.i("received", msg);
		ServerListAdapter = new Adapter();

		ListView codeLearnLessons = (ListView) findViewById(R.id.listView1);
		codeLearnLessons.setAdapter(ServerListAdapter);

		codeLearnLessons.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				ServerOnLine server1 = ServerListAdapter.getPosition(arg2);

				JSONObject jsn = new JSONObject();
				jsn.put("code", 1);	
				// TODO controllare se va bene server1.room_id
				jsn.put("room_id", server1.room_id);
				Log.i("id", server1.room_id.toString());
				// TODO code 1, player_id int boh, players:[id int boh, id int
				// boh]
				Client c1 = Client.getInstance();
				String mess = c1.createRoom(jsn.toString());
				Log.i("messaggio", mess);
				// GameResourceManager.getInstance().reset();
				// GameState.getInstance().reset();
				JSONObject risp = new JSONObject();
				try {
					risp = (JSONObject) new JSONParser().parse(mess);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if ((Long) risp.get("code") == 1) {
					Long pid = (Long) risp.get("player_id");
					Client.getInstance().startGame(pid.intValue());
					GameResourceManager.getInstance().addPlayer(""+pid);
					GameResourceManager.getInstance().setPlayerName("" + pid);
					
					JSONArray plist = new JSONArray();
					plist = (JSONArray) risp.get("players");
					Iterator it = plist.iterator();
					String listaPlayer[] = new String[plist.size()];
					int count = 0;
					while (it.hasNext()) {
						JSONObject rum = new JSONObject();
						rum = (JSONObject) it.next();
						Long player1 = (Long) rum.get("id");
						listaPlayer[count] = "" + player1;
						count++;
					}
					
					GameResourceManager.getInstance().addPlayer(listaPlayer);
					Intent intent = new Intent(Play.this, Waiting_room.class);
					startActivity(intent);
				} else {
					Toast.makeText(Play.this, "42", Toast.LENGTH_SHORT).show();

				}
			}
		});
	}

	public class Adapter extends BaseAdapter {

		List<ServerOnLine> serverList = getDataForListView();

		@Override
		public int getCount() {
			return serverList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return serverList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {

			if (arg1 == null) {
				LayoutInflater inflater = (LayoutInflater) Play.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				arg1 = inflater.inflate(R.layout.activity_play, arg2, false);
			}

			TextView chapterName = (TextView) arg1.findViewById(R.id.textView1);
			TextView chapterDesc = (TextView) arg1.findViewById(R.id.textView2);

			ServerOnLine servers = serverList.get(arg0);

			chapterName.setText(servers.Name);
			chapterDesc.setText(servers.Description);

			return arg1;
		}

		public ServerOnLine getPosition(int position) {

			return serverList.get(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_view_with_simple_adapter, menu);
		return true;
	}

	public List<ServerOnLine> getDataForListView() {
		List<ServerOnLine> ServerList = new ArrayList<ServerOnLine>();

		JSONObject jsn = new JSONObject();
		try {
			jsn = (JSONObject) new JSONParser().parse(msg);
		} catch (ParseException e) {

			e.printStackTrace();
		}
		Long errors = (Long) jsn.get("code");
		if (errors == 0) {
			JSONArray arr = (JSONArray) jsn.get("rooms");
			Iterator it = arr.iterator();
			while (it.hasNext()) {
				JSONObject rum = new JSONObject();
				rum = (JSONObject) it.next();
				ServerOnLine serv = new ServerOnLine();
				serv.Name = (String) rum.get("name");
				serv.room_id = (Long) rum.get("id");
				serv.Description = "numero max player: "
						+ (Long) rum.get("max_player");
				ServerList.add(serv);
			}
		} else {
			Toast.makeText(Play.this,
					"errore per colpa del server di benfa... pa pa paaaaaa",
					Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(Play.this, MainActivity.class);
			startActivity(intent);
		}
		return ServerList;

	}
}
