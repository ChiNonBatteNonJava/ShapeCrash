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

import Physic.PhysicCar;
import Physic.PhysicCarStatus;
import Physic.PhysicsWorld;
import android.util.Log;

class Recive extends Thread {
	private SocketChannel sc;
	private int id;
	private boolean end = false;

	public Recive(SocketChannel sc, int id) {
		this.sc = sc;
		this.id = id;
	}

	public void end() {
		end = true;
		Log.i("bnf", "eneenenenenenen");
	}

	public void run(){
        while(!end) {

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
                for(String s: jsonsss){
                	try{
	                JSONObject json = (JSONObject) new JSONParser().parse(s);
	                Long codel = (Long) json.get("code");
	                int code = codel==null?-2:codel.intValue();
	                switch (code){
	                	case 101:
	                		Long idl = (Long) json.get("player_id");
	                		
	                		PhysicsWorld.instance("MainWorld").delete(""+idl.intValue());
	                		Car c = GameResourceManager.getInstance().getCar(""+idl.intValue());
	                		GameState.getInstance().removeDrawable(c);
	                		GameState.getInstance().removeUpdatable(c);
	            			GameResourceManager.getInstance().deletePlayer(""+idl.intValue());
	            			
	                		break;
	                	case 102:
	                		Long idll = null;
	                		try{
	                			idll = (Long) json.get("player_id");
	                		}catch(Exception e){
	                			Log.i("bnf",e.getMessage());
	                		}
	                		Vector3 pos = new Vector3();
	                		pos.x=0;
	                		pos.y=10;
	                		pos.z=0;
	                		new Car(""+idll.intValue(),pos);
	                		
	                		break;
	                	case 6:
	                		JSONArray jsonArray = (JSONArray) json.get("players");
	                		Iterator i = jsonArray.iterator();
	                		while(i.hasNext()){
	                			JSONObject j = (JSONObject) i.next();
		      					Long pid = (Long) j.get("id");
		      					PhysicCarStatus pcs = new PhysicCarStatus();
		    					pcs.fromJSON(j);
		    					if(id != pid){
			    					if(PhysicsWorld.instance("MainWorld").getVheicle(""+pid.intValue())!= null){
			    						try{
			    							PhysicsWorld.instance("MainWorld").getVheicle(""+pid.intValue()).setStatus(pcs);
			    						}catch(Exception e){
			    							;
			    						}
			       					}
		    					}
	                		}
	                		break;
	                }
                	}catch(Exception e){
                		;
                	}
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}