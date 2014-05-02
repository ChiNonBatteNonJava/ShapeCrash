package ClientSide;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import android.util.Log;

class Receive extends Thread{
    private SocketChannel sc;
    private int id;
    public Receive(SocketChannel sc, int id) {
        this.sc = sc;
        this.id = id;
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
                    Log.i("recive:", str);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}