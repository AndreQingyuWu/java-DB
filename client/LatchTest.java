
//package test;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.net.Socket;
import java.io.*;
import net.sf.json.*;

public class LatchTest {
    // please edit config here
    protected static int ThreadNumber = 200;
    protected static String url = "localhost";
    protected static int port = 2333;

    public static void main(String[] args) throws InterruptedException {
        Runnable taskTemp = new Runnable() {

            @Override
            public void run() {
                JSONObject json = new JSONObject();
                Random random=new Random();
                int choice=random.nextInt(100);
                /*
                if(choice<70)
                {
                    //选课
                    long num=random.nextInt(2000)+1000;
                    json.put("method", 0);
                    json.put("classes", "Select");
                    json.put("sID",num);
                    json.put("cID",1111111111);
                }
                else
                {
                    //退课
                    long num=random.nextInt(2000)+1000;
                    json.put("method", 1);
                    json.put("classes", "Select");
                    json.put("sID",num);
                    json.put("cID",1111111111);
                }
                */
                for(int i=1;i<=100;i++) {
                	long num=i;
                    json.put("method", 1);
                    json.put("classes", "Select");
                    json.put("sID",num);
                    json.put("cID",2222222222L);
                }
                try {
                    // 发起请求
                    Socket socket = new Socket(url, port);
                    socket.setKeepAlive(true);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println(json.toString());
                    System.out.println(Thread.currentThread().getName() + ":"+json.toString());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String response = in.readLine();
                    System.out.println(Thread.currentThread().getName() + ":"+response);
                    socket.close();
                } catch (Exception e) {

                }
            }
        };

        LatchTest latchTest = new LatchTest();
        latchTest.startTaskAllInOnce(ThreadNumber, taskTemp);
    }

    public long startTaskAllInOnce(int threadNums, final Runnable task) throws InterruptedException {
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(threadNums);
        for (int i = 0; i < threadNums; i++) {
            Thread t = new Thread() {
                public void run() {
                    try {
                        startGate.await();
                        try {
                            task.run();
                        } finally {
                            endGate.countDown();
                        }
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            };
            t.start();
        }
        long startTime = System.nanoTime();
        System.out.println("All thread is ready, concurrent going...");
        startGate.countDown();
        endGate.await();
        long endTime = System.nanoTime();
        System.out.println("All thread is completed.");
        return endTime - startTime;
    }
}
