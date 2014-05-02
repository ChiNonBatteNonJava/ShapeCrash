package ClientSide;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import com.chinonbattenonjava.saproject.Car;

public class Client {
	
    private SocketChannel sc;
    private String  ip;
    private int porta;
    private int id;
    public Client(Car macchina){
    	try{
    	ip = "10.62.162.84";
    	porta = 4444;
    	
    	//create a random IDENTIFIER
    	
        id = (int)(Math.random()*1000);
        
        
        String msg = "BENFAGAYY";
        sc = SocketChannel.open();
        sc.connect(new InetSocketAddress(ip, porta));
        Receive r;
        r = new Receive(sc, id);
        Thread a = new Thread(r);
        a.start();
        Send q;
        q = new Send(sc, macchina, id);
        Thread b = new Thread(q);
        b.start();
    	}
    	catch (IOException e) {
            e.printStackTrace();
        }
    }

}