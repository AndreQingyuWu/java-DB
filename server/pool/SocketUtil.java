package pool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;

//import com.sun.rmi.rmid.ExecPermission;

import org.apache.commons.pool2.ObjectPool;

//Send msg to db server using socket pool
//Esther

public class SocketUtil {
	private ObjectPool<Socket> pool;

	public SocketUtil(ObjectPool<Socket> pool) {
		this.pool = pool;
	}

	public String sendToServer(String request)  {

		Socket connectionSocket = null;
		PrintWriter out=null;
		BufferedReader in = null;

		String respStr = null;

		try {
			try {

				connectionSocket = getConnection();
				out = new PrintWriter(connectionSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				out.println(request);
				// please make sure the response is always just a line	
				respStr = in.readLine();
			} catch (IOException e) {
				System.out.println(e.getMessage());
				return null;
			} finally {
				connectionSocket.setKeepAlive(true);
			}
			return respStr;
		} catch (NoSuchElementException e) {
			System.out.println("Can't get socket connection");
		} catch (IllegalStateException e) {
			System.out.println("No avalibel connection");
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				releaseConnection(connectionSocket);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		return null;
	}

	// get socket
	private Socket getConnection() throws Exception{
		Socket connectionSocket = pool.borrowObject();
		return connectionSocket;
	}

	// return socket
	private Socket releaseConnection(Socket connectionSocket) throws Exception {	
		pool.returnObject(connectionSocket);
		return connectionSocket;
	}
}
