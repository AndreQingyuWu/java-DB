package databasethread;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import databaseoperations.DBOperationsInterface;
import databaseoperations.DataBaseOperations;

/**
 * 数据库服务器进程
 * 使用容量为300的线程池,为每个请求创建一个线程,执行操作
 * @author zhaowenbo
 *
 */

public class DataBaseServer {
	static DBOperationsInterface dbo = new DataBaseOperations();//实例化数据库操作工具接口
	static final int PORT = 2334;
	 
	public static void main(String[] args) {
		try {
			ServerSocket ss = new ServerSocket(PORT);
			ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(300);//newScheduledThreadPool(210); //300个线程的线程池
			while(true) {
				Socket socket = ss.accept();
				DataBaseWorker worker = new DataBaseWorker(socket, new Protocol(), dbo);	//使用已经实例化的接口
				executor.execute(worker);	//执行线程
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("数据库服务器线程创建错误,服务器即将离线");
			System.exit(0);
		}
	}

}
