package communicationtest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ConnectException;
import java.net.Socket;

import org.json.*;

public class TestAddStudent {
	public static Socket socket = null;
	public static DataInputStream dis = null;
	public static DataOutputStream dos = null;

	public static boolean isSystemRootFolder = false;
	
	public static void main(String[] args) throws Exception {
		try {
			socket = new Socket("127.0.0.1", 5000);//连接服务器
		} catch (ConnectException e) {
			System.out.println("错误: 服务器离线!");
			System.exit(0);
		}
		dis = new DataInputStream(socket.getInputStream());
		dos = new DataOutputStream(socket.getOutputStream());
		
		dos.writeInt(6);
		JSONObject jsout = new JSONObject();
		jsout.put("sID", "00111111");
		jsout.put("sName", "Sandy");
		jsout.put("sClass", 31);
		jsout.put("sGender", false);
		dos.writeUTF(jsout.toString() + "\n");
		dos.writeInt(0);
		dos.flush();
		
		String readString = dis.readUTF();
		JSONObject jsin = new JSONObject(readString);
		System.out.println(jsin.get("return") + " " + jsin.get("reason"));
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
