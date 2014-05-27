package com.chinonbattenonjava.saproject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import ClientSide.Client;
import Physic.PhysicsWorld;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;




public class Waiting_room extends Activity {
	private String msg;
	private Handler handler;
	private ArrayList<Integer> players; 
	public class PlayerOnLine {
		String Player_id;
	}

	Adapter ServerListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		players = new ArrayList<Integer>();
		handler = new Handler();
		Intent intent = getIntent();
		String mess = intent.getStringExtra("jsn");
		this.msg = mess;
		super.onCreate(savedInstanceState);
		JSONObject jsn = new JSONObject();
		try {
			jsn = (JSONObject) new JSONParser().parse(msg);
		} catch (ParseException e) {

			e.printStackTrace();
		}
		
		Long errors = (Long) jsn.get("code");
		Long pid = (Long) jsn.get("player_id");
		Client.getInstance().waitRoom(pid.intValue(), this);
		setContentView(R.layout.activity_list_view_with_simple_adapter);
		ServerListAdapter = new Adapter();

		ListView Lista = (ListView) findViewById(R.id.listView1);
		Lista.setAdapter(ServerListAdapter);
		
	}
	public void enablePlay(){
		ImageButton bt1 = (ImageButton) findViewById(R.id.button1);
		bt1.setVisibility(View.VISIBLE);
	}
	public void playClicked(View v){
		JSONObject jsn = new JSONObject();
		jsn.put("code", 5);
		Client.getInstance().sendNoRecive(jsn.toString());
	}
	
	public Handler getHandler(){
		return handler;
	}
	
	public class Adapter extends BaseAdapter {

		List<PlayerOnLine> serverList = getDataForListView();

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
				LayoutInflater inflater = (LayoutInflater) Waiting_room.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				arg1 = inflater.inflate(R.layout.activity_waiting_room, arg2, false);
			}

			TextView chapterName = (TextView) arg1.findViewById(R.id.textView1);

			PlayerOnLine servers = serverList.get(arg0);

			chapterName.setText(servers.Player_id);

			return arg1;
		}

		public PlayerOnLine getPosition(int position) {

			return serverList.get(position);
		}
	}


	public List<PlayerOnLine> getDataForListView() {
		List<PlayerOnLine> PlayerList = new ArrayList<PlayerOnLine>();
		
		JSONObject jsn = new JSONObject();
		try {
			jsn = (JSONObject) new JSONParser().parse(msg);
		} catch (ParseException e) {

			e.printStackTrace();
		}
		
		Long errors = (Long) jsn.get("code");
		if (errors == 1) {
			Long pid = (Long) jsn.get("player_id");
			PlayerOnLine serv1 = new PlayerOnLine();
			serv1.Player_id = "You are player number:" +pid;
			PlayerList.add(serv1);
			JSONArray arr = (JSONArray) jsn.get("players");
			Iterator it = arr.iterator();
			while (it.hasNext()) {
				
				JSONObject rum = new JSONObject();
				rum = (JSONObject) it.next();
				PlayerOnLine serv = new PlayerOnLine();
				serv.Player_id = "Player:" +(Long) rum.get("id");
				players.add(new Integer(((Long) rum.get("id")).intValue()));
				Log.i("pierz", serv.Player_id);
				PlayerList.add(serv);
			}
		}
		else if(errors == 2){
			ImageButton bt1 = (ImageButton) findViewById(R.id.button1);
			bt1.setVisibility(View.VISIBLE);
			Long pid = (Long) jsn.get("player_id");
			PlayerOnLine serv1 = new PlayerOnLine();
			serv1.Player_id = "You are player number:" +pid;
			PlayerList.add(serv1);
		}
		return PlayerList;

	}
	
	public void addPlayer(int id){
		PlayerOnLine serv = new PlayerOnLine();
		serv.Player_id = "Player:" +id;
		ServerListAdapter.serverList.add(serv);
		ListView Lista = (ListView) findViewById(R.id.listView1);
		players.add(id);
		Lista.setAdapter(ServerListAdapter);
		
		Log.i("azz", "add");
	}
	public void removePlayer(int id){
		List<PlayerOnLine> PlayerList = ServerListAdapter.serverList;
		Iterator it = PlayerList.listIterator();
		int count = 0;
		while (it.hasNext()){
			PlayerOnLine p = new PlayerOnLine();
			p = (PlayerOnLine) it.next();
			String iden1 = p.Player_id;
			String iden2 = "Player:"+id;
			if (iden1.equals(iden2)){
				PlayerList.remove(count);
				
			}
			count += 1;
		}
		Log.i("azz", "remove");
		ListView Lista = (ListView) findViewById(R.id.listView1);
		
		Lista.setAdapter(ServerListAdapter);
		
	}	
	public void startGame(){
		JSONObject jsn = new JSONObject();
		try {
			jsn = (JSONObject) new JSONParser().parse(msg);
		} catch (ParseException e) {

			e.printStackTrace();
		}
		
		Long errors = (Long) jsn.get("code");
		Long pid = (Long) jsn.get("player_id");
		Client.getInstance().startGame(pid.intValue());
		GameResourceManager.getInstance().addPlayer(""+pid.intValue());		
		GameResourceManager.getInstance().setPlayerName("" + pid.intValue());
		String[] ple = new String[players.size()];
		
		for(int i=0; i<players.size(); i++){
			ple[i] = ""+players.get(i);
		}
		GameResourceManager.getInstance().addPlayer(ple);
		
		/*
		Iterator it = ServerListAdapter.serverList.iterator();
		String[] pp = new String[(ServerListAdapter.serverList.size()-1)];
		int count = 0;
		while(it.hasNext()){
			PlayerOnLine p = (PlayerOnLine)it.next();
			if(!p.Player_id.equals(String.valueOf(pid))){
				pp[count] = p.Player_id;
				count++;
			}
		}
		GameResourceManager.getInstance().addPlayer(pp);		*/
		/*
		if (errors == 1) {

			
			JSONArray plist = new JSONArray();

			plist = (JSONArray) jsn.get("players");

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
		}
			/*
			JSONArray plist = new JSONArray();
	
			plist = (JSONArray) jsn.get("players");
	
			
			Iterator it = plist.iterator();
			//Iterator it = ServerListAdapter.serverList.iterator();
			String listaPlayer[] = new String[ServerListAdapter.serverList.size()];
			int count = 0;
			while (it.hasNext()) {
				JSONObject rum = new JSONObject();
				rum = (JSONObject) it.next();
				Long player1 = (Long) rum.get("id");
				listaPlayer[count] = ""+player1.intValue();
				//String i = ((PlayerOnLine) it.next()).Player_id;
				//if(!i.equals(""+pid)){
					//listaPlayer[count] = "" + ((PlayerOnLine) it.next()).Player_id;
					count++;
				//}
			}
			GameResourceManager.getInstance().addPlayer(listaPlayer);
		}
		*/
		
		
		Intent intent = new Intent(Waiting_room.this, Game.class);
		startActivity(intent);
		Log.i("start", "parte ora");
		
	}
	
	public void onDestroy() {
		/*
		JSONObject json = new JSONObject();
		json.put("code", 101);
		Client.getInstance().request(json.toJSONString());
		Client.getInstance().stopGame();
		Client.reset();
		GameState.getInstance().reset();
		GameResourceManager.getInstance().reset();
		PhysicsWorld.reset();
		
		
		Log.i("bnf","close");
		*/
		super.onDestroy();
	}
}