package ClientSide;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

class Receive extends Thread{
    SocketChannel sc;
    public Receive(SocketChannel sc) {
        this.sc = sc;
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
                    System.out.println(str);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }



}