package databasethread;

import java.net.Socket;

import databaseoperations.DBOperationsInterface;

/**
 * 数据库线程,继承Thread类,
 * @author zhaowenbo
 *
 */

public class DataBaseWorker extends Thread { 
	private Socket socket; 
	private DataBaseStrategy ios; //自定义的接口
	private DBOperationsInterface dbo = null;
	
	public DataBaseWorker(Socket socket, DataBaseStrategy ios, DBOperationsInterface dbo){	//使用DBOperationsInterface接口
		this.socket = socket;
		this.ios = ios;
		this.dbo = dbo;
	}

	public void run() {
		ios.service(socket, dbo); 
	}
}
