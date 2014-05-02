package ClientSide;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import com.chinonbattenonjava.saproject.Car;

public class Client {

//    public static void main(String[]args) throws IOException {
//        Client x=new Client("10.62.162.84", 4444);
//    }
	
    private SocketChannel sc;
    private int randomNum;
    private String  ip;
    private int porta;
    public Client(Car macchina){
    	try{
    	ip = "10.62.162.84";
    	porta = 4444;
        randomNum = (int)(Math.random()*1000);
        String msg = "BENFAGAYY";
        sc = SocketChannel.open();
        sc.connect(new InetSocketAddress(ip, porta));
        Receive r;
        r = new Receive(sc);
        Thread a = new Thread(r);
        a.start();
        Send q;
        q = new Send(sc, macchina, randomNum);
        Thread b = new Thread(q);
        b.start();
    	}
    	catch (IOException e) {
            e.printStackTrace();
        }
    }

}