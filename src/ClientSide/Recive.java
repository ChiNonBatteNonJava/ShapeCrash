package ClientSide;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.badlogic.gdx.math.Vector3;
import com.chinonbattenonjava.saproject.Car;
import com.chinonbattenonjava.saproject.GameResourceManager;
import com.chinonbattenonjava.saproject.GameState;
import com.chinonbattenonjava.saproject.Waiting_room;

import Physic.PhysicCar;
import Physic.PhysicCarStatus;
import Physic.PhysicsWorld;
import android.os.Handler;
import android.util.Log;

class Recive extends Thread {
	private SocketChannel sc;
	private int id;
	private boolean end = false;
	private boolean game = false;
	private Waiting_room wr;

	public Recive(SocketChannel sc, int id, Waiting_room wr) {
		this.sc = sc;
		this.id = id;
		this.wr = wr;
	}

	public void end() {
		end = true;
		Log.i("bnf", "eneenenenenenen");
	}

	public void gameStart() {
		game = true;
	}

	public void run() {
		while (!end) {

			try {
				ByteBuffer buff = ByteBuffer.allocate(2048);
				buff.clear();
				int nbyte = sc.read(buff);
				String str = "";
				if (nbyte != -1) {
					buff.flip();
					while (buff.hasRemaining()) {
						str += (char) buff.get();
					}
				}
				String[] jsonsss = str.split("\n");
				for (String s : jsonsss) {
					try {
						JSONObject json = (JSONObject) new JSONParser()
								.parse(s);
						Long codel = (Long) json.get("code");
						int code = codel == null ? -2 : codel.intValue();
						switch (code) {
						case 101:
							final Long idl = (Long) json.get("player_id");
							if (game) {
								PhysicsWorld.instance("MainWorld").delete(
										"" + idl.intValue());
								Car c = GameResourceManager.getInstance()
										.getCar("" + idl.intValue());
								GameState.getInstance().removeDrawable(c);
								GameState.getInstance().removeUpdatable(c);
								GameResourceManager.getInstance().deletePlayer(
										"" + idl.intValue());
							} else {
								Handler handler = wr.getHandler();
								handler.post(new Runnable() {
									public void run() {
										wr.removePlayer(idl.intValue());
									}
								});
							}
							break;
						case 102:
							final Long idll;
							try {
								idll = (Long) json.get("player_id");

								if (game) {
									Vector3 pos = new Vector3();
									pos.x = 0;
									pos.y = 10;
									pos.z = 0;
									new Car("" + idll.intValue(), pos);
								} else {

									Handler handler = wr.getHandler();
									handler.post(new Runnable() {
										public void run() {
											wr.addPlayer(idll.intValue());
										}
									});
								}
							} catch (Exception e) {
								Log.i("bnf", e.getMessage());
							}
							break;
						case 6:
							JSONArray jsonArray = (JSONArray) json
									.get("players");
							Iterator i = jsonArray.iterator();
							while (i.hasNext()) {
								JSONObject j = (JSONObject) i.next();
								Long pid = (Long) j.get("id");
								PhysicCarStatus pcs = new PhysicCarStatus();
								pcs.fromJSON(j);
								if (id != pid) {
									if (PhysicsWorld.instance("MainWorld")
											.getVheicle("" + pid.intValue()) != null) {
										try {
											PhysicsWorld
													.instance("MainWorld")
													.getVheicle(
															"" + pid.intValue())
													.setStatus(pcs);
										} catch (Exception e) {
											;
										}
									}
								}
							}
							break;
						case 5:
							wr.startGame();
							Log.i("start", "banana");
							break;
						case 9:
							if(!game){
								wr.getHandler().post(new Runnable(){public void run(){wr.enablePlay();}});
							}
							break;
						}
					} catch (Exception e) {
						;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}
}