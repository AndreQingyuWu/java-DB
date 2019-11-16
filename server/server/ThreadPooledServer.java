package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import pool.SocketPreconnectPool;
import pool.SocketFactory;

//server主线程
//实现了线程池
//Esther
public class ThreadPooledServer implements Runnable {

    protected static final String propertiesName = "db.properties";
    protected static int serverPort;
    protected static int poolSize;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;
    protected Thread runningThread = null;
    protected ExecutorService execServer = Executors.newFixedThreadPool(poolSize);
    public static ObjectPool<Socket> pool = null;
    public static GenericObjectPoolConfig config = new GenericObjectPoolConfig();

    static {
        FileInputStream in = null;
        try {
            Properties properties = new Properties();
            in = new FileInputStream(propertiesName);
            properties.load(in);
            serverPort = Integer.parseInt(properties.getProperty("ServerPort"));
            poolSize = Integer.parseInt(properties.getProperty("ThreadPoolSize"));
            config.setMaxTotal(Integer.parseInt(properties.getProperty("SocketPoolSize")));
            System.out.println("Read properties success!");
        } catch (Exception e) {
            System.out.println("Falied to read properties!");
        }
        pool = new SocketPreconnectPool<Socket>(new SocketFactory(), config);
    }

    public void run() {
        synchronized (this) {
            this.runningThread = Thread.currentThread();
        }

        isStopped = false;
        openServerSocket();

        while (!isStopped()) {
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
                clientSocket.setKeepAlive(true);
            } catch (IOException e) {
                if (isStopped()) {
                    break;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
            this.execServer.execute(new WorkerRunnable(clientSocket, pool));
        }
        execServer.shutdown();
        try {
            pool.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Server Stopped.");
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(serverPort);
            System.out.println("Server is ready. Listening on " + serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + serverPort, e);
        }
    }
}