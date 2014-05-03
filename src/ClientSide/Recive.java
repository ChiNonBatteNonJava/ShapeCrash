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
                if (nbyte != -1) {
                    buff.flip();
                    while (buff.hasRemaining()) {
                        str += (char) buff.get();
                    }
                    Log.i("bnf",str);
                    JSONObject json = (JSONObject) new JSONParser().parse(str);
                    Long l = (Long)json.get("code");
                    Integer code = (l != null) ? l.intValue() : -1;
                    if (code==0){
                    	l = (Long)json.get("id");
                    	Integer id = (l != null) ? l.intValue() : -1;
                    	if (id!= -1){
                    		new Car(id.toString());
                    	}
                    }else if(code==1){
                    	l = (Long)json.get("id");
                    	Integer id = (l != null) ? l.intValue() : -1;
                    	if (id!= -1){
                    		PhysicCar c = PhysicsWorld.instance("MainWorld").getVheicle(id.toString());
                    		PhysicCarStatus status = new PhysicCarStatus();
                    		status.fromJSON(json);
                    		c.setStatus(status);
                    	}
                    }
                                  
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e){
            	e.printStackTrace();
            }

        }

    }

}