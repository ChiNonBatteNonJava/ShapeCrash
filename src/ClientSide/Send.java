package ClientSide;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

class Send extends Thread{
    private SocketChannel sc;
    private String msg;

    public Send(SocketChannel sc, String msg, int id) {
        this.sc = sc;
        this.msg = "ID: "+id+"   ->"+msg;

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