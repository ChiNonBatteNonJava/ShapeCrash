package ClientSide;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class Client {

//    public static void main(String[]args) throws IOException {
//        Client x=new Client("10.62.162.84", 4444);
//    }
    public SocketChannel sc;
    public int randomNum;
    public Client(String ip, int porta) throws IOException {
        randomNum = (int)(Math.random()*1000);
        String msg = "BENFAGAYY";
        sc = SocketChannel.open();
        sc.connect(new InetSocketAddress(ip, porta));
        Receive r;
        r = new Receive(sc);
        Thread a = new Thread(r);
        a.start();
        Send q;
        q = new Send(sc, msg, randomNum);
        Thread b = new Thread(q);
        b.start();
    }

}