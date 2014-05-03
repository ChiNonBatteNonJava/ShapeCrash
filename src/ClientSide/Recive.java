package ClientSide;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.chinonbattenonjava.saproject.Car;

import Physic.PhysicCar;
import Physic.PhysicCarStatus;
import Physic.PhysicsWorld;
import android.util.Log;

class Receive extends Thread{
    private SocketChannel sc;
    private int id;
   
    
    public Receive(SocketChannel sc, int id) {
        this.sc = sc;
        this.id = id;
        Log.i("bnf","recive");
    }
    public void run(){
        while(true) {

            try {
                ByteBuffer buff = ByteBuffer.allocate(512);
                buff.clear();
                int nbyte = sc.read(buff);
                String str = "";
                //Log.i("bnf",String.valueOf(nbyte));
                if (nbyte != -1) {
                    buff.flip();
                    while (buff.hasRemaining()) {
                        str += (char) buff.get();
                    }
                    //Log.i("bnf",str);
                    JSONObject json = (JSONObject) new JSONParser().parse(str);
                    Long l = (Long)json.get("code");
                    Integer code = (l != null) ? l.intValue() : -1;
                    if (code==0){
                    	l = (Long)json.get("id");
                    	Integer id = (l != null) ? l.intValue() : -1;
                    	if (id!= -1){
                    		new Car(id.toString());
                    		this.sleep(10);
                    	}
                    }else if(code==1){
                    	//Log.i("bnf", "entrato");
                    	l = (Long)json.get("id");
                    	Integer idd = (l != null) ? l.intValue() : -1;
                    	if (idd!= -1 && idd!=this.id){
                    		Log.i("bnf",idd.toString());             
                    		PhysicCar c = PhysicsWorld.instance("MainWorld").getVheicle(idd.toString());
                    		Log.i("bnf","a");
                    		PhysicCarStatus status = new PhysicCarStatus();
                    		Log.i("bnf","b");
                    		status.fromJSON(json);
                    		Log.i("bnf","ciao"+c.toString());
                    		if(c!=null){
                    			c.setStatus(status);
                    		}
                    	}
                    }
                                  
                }
            } catch (Exception e) {
                Log.i("bnf",e.toString());
            }

        }

    }

}