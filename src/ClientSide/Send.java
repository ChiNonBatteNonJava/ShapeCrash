package ClientSide;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.json.simple.JSONObject;


class Send extends Thread{
    private SocketChannel sc;
    private String msg;
    private JSONObject jsn;
    private boolean sent;
    //private Car macchina;
    private int id;
    public Send(SocketChannel sc) {
        this.sc = sc;
        sent = true;
    }
    public void sendMessage(String msg){
    	this.msg = msg;
    	sent = false;
    	
    	
    }
    
    public void run() {
    	while (true) {
    		if (!sent) {
    			try{
			    	ByteBuffer buff = ByteBuffer.allocate(512);
			    	buff.clear();
			    	buff.put(msg.getBytes());
			    	buff.flip();
			    	sc.write(buff);
			    	
			    }
			    catch (IOException e) {
			        e.printStackTrace();
			    }
    		sent = true;
    		}
    	}
    }
}