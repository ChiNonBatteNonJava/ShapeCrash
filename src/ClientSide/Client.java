package ClientSide;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.json.simple.JSONObject;

import com.chinonbattenonjava.saproject.Waiting_room;

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
    private Waiting_room wr;
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
    	SingleRequest sq = new SingleRequest(sc, jsn.toString(), true);
    	sq.start();
    	while (!sq.isEnded()){
    		;
    	}
    	return sq.getResults();
    	
    }

    public JSONObject joinServer(int id){
    	
    	return null;
    }
    public String request(String msg){
    	Log.i("bfn","aaaa");
    	SingleRequest sq = new SingleRequest(sc, msg, true);
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
        	Log.i("bana", "6");
        	if (sc!=null){
        		
        		Log.i("bana", "not null");
        	}
    		//sc.write(buff);
        	Log.i("bana", "7");
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
    
    public void waitRoom(int id, Waiting_room wr){
    	rec = new Recive(sc, id, wr);
    	this.wr = wr;
		rec.start();
    }
    
    public void startGame(int id){
    	rec.gameStart();
		snd = new Send(sc, id);
		snd.start();
		
	}
	public void sendNoRecive(String msg){
		SingleRequest sq = new SingleRequest(sc, msg, false);
		sq.start();
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
	private boolean ricevi;
	public SingleRequest(SocketChannel sc, String msg, boolean ricevi){
		this.sc = sc;
		this.ricevi = ricevi;
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
	    	if (ricevi){
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
