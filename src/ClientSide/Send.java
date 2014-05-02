package ClientSide;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.chinonbattenonjava.saproject.Car;

class Send extends Thread{
    private SocketChannel sc;
    private String msg;
    private JSONObject jsn;
    public Send(SocketChannel sc, Car macchina, int id) {
        this.sc = sc;

        JSONObject jsn = macchina.getCar().getStatus() ;
    }
    public void run() {
        long taskTime = 0;
        long sleepTime = 1000/10;
        while (true) {
            taskTime = System.currentTimeMillis();
            try {
                ByteBuffer buff = ByteBuffer.allocate(512);
                buff.clear();
                buff.put(msg.getBytes());
                buff.flip();
                sc.write(buff);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            taskTime = System.currentTimeMillis()-taskTime;
            if (sleepTime-taskTime > 0 ) {
                try {
                    Thread.sleep(sleepTime-taskTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}