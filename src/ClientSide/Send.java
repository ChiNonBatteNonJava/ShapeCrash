package ClientSide;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.json.simple.JSONObject;

import Physic.PhysicCar;
import Physic.PhysicsWorld;
import android.util.Log;

import com.chinonbattenonjava.saproject.Car;

class Send extends Thread{
    private SocketChannel sc;
    private String msg;
    private JSONObject jsn;
    //private PhysicCar macchina;
    private int id;
    public Send(SocketChannel sc, int id) {
        this.sc = sc;
        //this.macchina = PhysicsWorld.instance("MainWorld").getVheicle(String.valueOf(id));
        this.id = id;
        try {
        	JSONObject jsn = new JSONObject();
            jsn.put("id", id);
            msg = jsn.toJSONString();
            ByteBuffer buff = ByteBuffer.allocate(512);
            buff.clear();
            buff.put(msg.getBytes());
            buff.flip();
            sc.write(buff);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    
    public void run() {
        long taskTime = 0;
        long sleepTime = 1000/10;
        while (true) {
            taskTime = System.currentTimeMillis();
            try {
            	if(PhysicsWorld.instance("MainWorld").getVheicle(String.valueOf(id))!=null){
	            	JSONObject jsn = PhysicsWorld.instance("MainWorld").getVheicle(String.valueOf(id)).getStatus().toJson();
	                jsn.put("id", id);
	                msg = jsn.toJSONString();
	                ByteBuffer buff = ByteBuffer.allocate(512);
	                buff.clear();
	                buff.put(msg.getBytes());
	                buff.flip();
	                sc.write(buff);
            	}
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            taskTime = System.currentTimeMillis()-taskTime;
            if (sleepTime-taskTime > 0 ) {
                try {
                    Thread.sleep(sleepTime-taskTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}