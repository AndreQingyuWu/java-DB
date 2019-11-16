package test;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

//server入口
//Esther
public class DBServer {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(2334);
            ExecutorService execServer = Executors.newFixedThreadPool(200);
            System.out.println("OK");
            while (true) {
                Socket socket=serverSocket.accept();
                socket.setKeepAlive(true);
                execServer.execute(new DBRunnable(socket));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}