package communicationtest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;

import org.json.*;

public class TestSelect {
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
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		
		JSONObject jsout = new JSONObject();
		jsout.put("method", "3");
		jsout.put("sID", "00111111");
		jsout.put("cID", "3333333333");
		
		out.write(jsout.toString() + "\n");
		out.flush();
		jsout = new JSONObject();
		jsout.put("method", "0");
		out.write(jsout.toString() + "\n");
		out.flush();
		
		String readString = in.readLine();
		JSONObject jsin = new JSONObject(readString);
		System.out.println(jsin.get("return"));// + " " + jsin.get("reason"));
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
