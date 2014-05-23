package ClientSide;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.chinonbattenonjava.saproject.Car;
import com.chinonbattenonjava.saproject.GameResourceManager;
import com.chinonbattenonjava.saproject.GameState;

import Physic.PhysicCar;
import Physic.PhysicCarStatus;
import Physic.PhysicsWorld;
import android.util.Log;

class Recive extends Thread{
    private SocketChannel sc;
    private int id;
    private boolean end = false;

    public Recive(SocketChannel sc, int id) {
        this.sc = sc;
        this.id = id;
    }
    
    public void end(){
    	end = true;
    }
    
    public void run(){
        while(!end) {

            try {
                ByteBuffer buff = ByteBuffer.allocate(1024);
                buff.clear();
                int nbyte = sc.read(buff);
                String str = "";
                if (nbyte != -1) {
                    buff.flip();
                    while (buff.hasRemaining()) {
                        str += (char) buff.get();
                    }
                }
                JSONObject json = (JSONObject) new JSONParser().parse(str);
                Long codel = (Long) json.get("code");
                int code = codel==null?-2:codel.intValue();
                switch (code){
                	case 101:
                		Long idl = (Long) json.get("player_id");
                		
                		PhysicsWorld.instance("MainWorld").delete(""+idl.intValue());
                		GameState.getInstance().getUpdatables().remove(""+idl.intValue());
            			GameState.getInstance().getDrawables().remove(""+idl.intValue());
            			GameResourceManager.getInstance().deletePlayer(""+idl.intValue());
            			
                		break;
                	case 102:
                		Long idll = (Long) json.get("player_id");
                		new Car(""+idll.intValue());
                		break;
                	case 6:
                		;
                		JSONArray plist = new JSONArray(); 
                		plist = (JSONArray) json.get("players");
    					Iterator it = plist.iterator();
    					while(it.hasNext()){
    						JSONObject obj = (JSONObject) it.next();
    						Long pid = (Long) obj.get("id");
    						PhysicCarStatus pcs = new PhysicCarStatus();
    						pcs.fromJSON(obj);
    						Log.i("bnf",""+pid);
    						PhysicCar c = PhysicsWorld.instance("MainWorld").getVheicle(""+pid.intValue());
    						if(c!= null){
    								c.setStatus(pcs);
    								Log.i("bnf","asd");
    						}
    					}
                		break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        }

    }

}