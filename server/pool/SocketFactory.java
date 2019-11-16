package pool;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Properties;
import net.sf.json.*;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class SocketFactory extends BasePooledObjectFactory<Socket> {
	protected static final String propertiesName = "db.properties";
	protected static String dbHost;
	protected static int dbPort;

	InetSocketAddress serverInfo = new InetSocketAddress(dbHost, dbPort);
	static
	{
		FileInputStream in = null;
        try {
            Properties properties = new Properties();
            in = new FileInputStream(propertiesName);
            properties.load(in);
            dbHost = properties.getProperty("dbHost");
            dbPort = Integer.parseInt(properties.getProperty("dbPort"));
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	@Override
	public Socket create() throws IOException {
		Socket socket = new Socket();
		socket.setKeepAlive(true);
		try {
			socket.connect(serverInfo);
		} catch (IOException e) {
			System.out.println("Can't connect to DB.");
			throw e;
		}
		return socket;
	}

	@Override
	public PooledObject<Socket> wrap(Socket buffer) {
		return new DefaultPooledObject<Socket>(buffer);
	}

	/*
	 * When an object is returned to the pool, clear the buffer.
	 */
	@Override
	public void passivateObject(PooledObject<Socket> pooledObject) {
	}

	@Override
	public PooledObject<Socket> makeObject() throws Exception {
		return super.makeObject();
	}

	@Override
	public void destroyObject(PooledObject<Socket> p) throws Exception {
		PrintWriter pw = null;
		pw = new PrintWriter(p.getObject().getOutputStream(), true);
		// socket will be closed by socket server after send this msg
		JSONObject requestObj=new JSONObject();
		requestObj.put("method",0);
		pw.println(requestObj.toString());

		super.destroyObject(p);
	}

	@Override
	public boolean validateObject(PooledObject<Socket> p) {
		return super.validateObject(p);
	}

	@Override
	public void activateObject(PooledObject<Socket> p) throws Exception {
		super.activateObject(p);
	}
}
