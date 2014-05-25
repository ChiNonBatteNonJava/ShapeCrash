package ClientSide;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.json.simple.JSONObject;

import android.util.Log;
import Physic.PhysicCar;




public class Client extends Thread {
	private static Client instance;
    private SocketChannel sc;
    private String  ip;
    private int porta;
    private int id;
    private Recive rec;
    private Send snd;
    private int connectionStatus;
    private Send sender;
    //Client must keep Car as parameter
    private Client(){
    	connectionStatus = 0;
    }
    public int getConnectionStatus(){
    	return connectionStatus;
    	
    }
    public static Client getInstance(){
    	if (instance == null){
    		instance = new Client();
    	}
    	return instance;
    }
    public void sendMessage(String msg) {
    	 sender.sendMessage(msg);
    }
    
    public String listServer(){
    	JSONObject jsn = new JSONObject();
        jsn.put("code", 0);
    	SingleRequest sq = new SingleRequest(sc, jsn.toString());
    	sq.start();
    	while (!sq.isEnded()){
    		;
    	}
    	return sq.getResults();
    	
    }
    public JSONObject joinServer(int id){
    	
    	return null;
    }
    public String createRoom(String msg){
    	Log.i("bfn","aaaa");
    	SingleRequest sq = new SingleRequest(sc, msg);
    	Log.i("bfn","bbbb");
    	sq.start();
    	Log.i("bfn","cccc");
    	while (!sq.isEnded()){
    		;
    	}
    	Log.i("bfn","dddd");
    	return sq.getResults();
    }
    
    public void send(String msg){
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
    	try{
    	ip = "10.62.162.84";
    	porta = 4444;
    	
    	//create a random IDENTIFIER
    	
        id = (int)(Math.random()*1000);
       
        sc = SocketChannel.open();
        sc.connect(new InetSocketAddress(ip, porta));
        
        //r = new Receive(sc, id);
        //Thread a = new Thread(r);
        //a.start();
        //sender = new Send(sc);
        //sender.start();
        connectionStatus = 1;
    	}
    	catch (IOException e) {
    		connectionStatus = -1;
            e.printStackTrace();
        }
    }
    
    public void setSteering(int dir, int steering){
    	JSONObject json = new JSONObject();
    	json.put("code", 6);
    	json.put("steering", steering);
    	json.put("dir", dir);
    	send(json.toJSONString());
    }
    
    public void startGame(int id){
		rec = new Recive(sc, id);
		rec.start();
		snd = new Send(sc, id);
		snd.start();
		
	}
	
	public void stopGame(){
		rec.end();
		snd.end();
	}
	
	public static void reset(){
		Log.i("bnf","quiquiquiq");
		try {
			instance.sc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		instance = new Client();
	}
    
}

class SingleRequest extends Thread{
	private String msg;
	private String recived;
	private SocketChannel sc;
	private boolean end;
	public SingleRequest(SocketChannel sc, String msg){
		this.sc = sc;
		this.msg = msg;		
		end = false;
	}
	public void run(){
		try{
	    	ByteBuffer buff = ByteBuffer.allocate(512);
	    	buff.clear();
	    	buff.put(msg.getBytes());
	    	buff.flip();
	    	sc.write(buff);
	    	buff.flip();
	    	buff.clear();
            int nbyte = sc.read(buff);
            String str = "";
            Log.i("bnfSingle",""+nbyte);
            if (nbyte != -1) {
                buff.flip();
                while (buff.hasRemaining()) {
                    str += (char) buff.get();
                }
                recived = str;
                end = true;
            }
		}
	    catch (IOException e) {
	        e.printStackTrace();
	    }
		
		
	}
	public String getResults(){
		return recived;
	}
	public boolean isEnded(){
		return end;
	}
	
	
	
}
