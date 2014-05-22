package ClientSide;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import android.util.Log;

class Receive extends Thread{
    private SocketChannel sc;
    private int id;
    private String recived;
    public Receive(SocketChannel sc, int id) {
        this.sc = sc;
        this.id = id;
        this.recived = "";
    }
    public boolean isRecived(){
    	return recived==""?false:true;
    }
    public String getRecived(){
    	String r = recived;
    	recived = "";
    	return r;
    }
    public void run(){
        while(true) {

            try {
                ByteBuffer buff = ByteBuffer.allocate(512);
                buff.clear();
                int nbyte = sc.read(buff);
                String str = "";
                if (nbyte != -1) {
                    buff.flip();
                    while (buff.hasRemaining()) {
                        str += (char) buff.get();
                    }
                    this.recived = str;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}