package communicationtest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.*;

import data.SelectData;

public class TestGetSelectList {
	public static Socket socket = null;
	public static DataInputStream dis = null;
	public static DataOutputStream dos = null;

	public static boolean isSystemRootFolder = false;
	
	public static void main(String[] args) throws Exception {
		try {
			socket = new Socket("127.0.0.1", 2334);//连接服务器
		} catch (ConnectException e) {
			System.out.println("错误: 服务器离线!");
			System.exit(0);
		}
		dis = new DataInputStream(socket.getInputStream());
		dos = new DataOutputStream(socket.getOutputStream());
		
		
		//dos.writeInt(2);
		JSONObject jsout = new JSONObject();
		jsout.put("method", 2);
		jsout.put("sID", "00111111");
		dos.writeUTF(jsout.toString() + "\n");
		dos.writeInt(0);
		dos.flush();
		
		jsout = new JSONObject();
		jsout.put("method", 0);
		dos.writeUTF(jsout.toString() + "\n");
		dos.writeInt(0);
		dos.flush();
		
		String readString = dis.readUTF();
		if(readString.equals("1")) {
			System.out.println("Error");
			System.exit(0);
		}
		JSONObject jsin = new JSONObject(readString);
		JSONArray ja = (JSONArray) jsin.get("selectArrayList");
		Iterator<Object> ite = ja.iterator();
		while(ite.hasNext()) {
			JSONObject jo = (JSONObject) ite.next();
			System.out.println(jo.get("cID") + " " + jo.get("cName") + " " + jo.get("datatime"));
		}
		try {
			Thread.sleep(50);//等待50毫秒
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		try {
			socket.close();//关闭socket
		} catch (Exception e) {
		}
		System.exit(0);
	}
}
