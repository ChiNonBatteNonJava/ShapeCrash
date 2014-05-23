package ClientSide;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.json.simple.JSONObject;

import android.util.Log;
import Physic.PhysicCarStatus;
import Physic.PhysicsWorld;


class Send extends Thread{
    private static final long MIN_DELTA = 50;
	private SocketChannel sc;
    private String msg;
    private JSONObject jsn;
    private boolean sent;
    private boolean end = false;
    //private Car macchina;
    private int id;
    public Send(SocketChannel sc, int id) {
        this.sc = sc;
        sent = true;
        this.id = id;
    }
    
    public void end(){
    	end = true;
    }
    
    public void sendMessage(String msg){
    	ByteBuffer buff = ByteBuffer.allocate(512);
    	buff.clear();
    	buff.put(msg.getBytes());
    	buff.flip();
    	try{
    		sc.write(buff);
    	}catch(Exception e ){
    		Log.i("err", e.getMessage());
    	}
    }
    
    public void run() {
    	long firstTime = System.currentTimeMillis();
        long lastTime = System.currentTimeMillis();
        long delta = firstTime - lastTime;
    	while (!end) {
    		lastTime = System.currentTimeMillis();
            delta = lastTime - firstTime;
            firstTime = lastTime;
    		if(PhysicsWorld.instance("MainWorld").getVheicle(""+id)!= null){
    			PhysicCarStatus pcs = PhysicsWorld.instance("MainWorld").getVheicle(""+id).getStatus();
    			JSONObject obj = pcs.toJson();
    			obj.put("id", id);
    			obj.put("code", 6);
    			sendMessage(obj.toJSONString());	
    		}
    		if(lastTime-firstTime<MIN_DELTA){
                try {
                    Thread.sleep(MIN_DELTA-(lastTime-firstTime));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    	}
    }
}