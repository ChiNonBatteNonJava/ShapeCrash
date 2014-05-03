package ClientSide;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import android.util.Log;

import com.chinonbattenonjava.saproject.Car;

public class Client extends Thread {
	
    private SocketChannel sc;
    private String  ip;
    private int porta;
    private int id;
    public void run(){
    	try{
    	ip = "10.62.162.84";
    	porta = 4444;
    	
    	//create a random IDENTIFIER    	
        id = (int)(Math.random()*1000);
        Log.i("bnf","aaaaaaaaa"+id);
        
        sc = SocketChannel.open();
        Log.i("bnf","1q");
        sc.connect(new InetSocketAddress(ip, porta));
        Log.i("bnf","2q");
        Receive r = new Receive(sc, id);
        Log.i("bnf","3");
        Thread a = new Thread(r);
        Log.i("bnf","4");
        a.start();
        Send q;
        q = new Send(sc, id);
        Thread b = new Thread(q);
        b.start();
    	}
    	catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String getCarId(){
    	return String.valueOf(id);
    }

}