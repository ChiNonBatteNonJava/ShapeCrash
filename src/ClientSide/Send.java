package ClientSide;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.json.simple.JSONObject;

import com.chinonbattenonjava.saproject.Car;

class Send extends Thread{
    private SocketChannel sc;
    private String msg;
    private JSONObject jsn;
    private Car macchina;
    private int id;
    public Send(SocketChannel sc, Car macchina, int id) {
        this.sc = sc;
        this.macchina = macchina;
        this.id = id;
    }
    public void run() {
        long taskTime = 0;
        long sleepTime = 1000/10;
        while (true) {
            taskTime = System.currentTimeMillis();
            try {
            	JSONObject jsn = macchina.getCar().getStatus().toJson();
                jsn.put("id", id);
                msg = jsn.toString();
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