package ClientSide;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import java.nio.channels.SocketChannel;

import org.json.simple.JSONObject;




public class Client extends Thread {
	private static Client instance;
    private SocketChannel sc;
    private String  ip;
    private int porta;
    private int id;
    private Receive r;
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
    public String getMessage(){
    	if (r.isRecived()){
    		return r.getRecived();
    	}
    	return "";
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
    	SingleRequest sq = new SingleRequest(sc, msg);
    	sq.start();
    	while (!sq.isEnded()){
    		;
    	}
    	return sq.getResults();
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
