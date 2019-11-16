
package communicationtest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Iterator;

import org.json.*;

public class TestGetCourseList {
	public static Socket socket = null;

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
		jsout.put("method", 12);
		out.write(jsout.toString() + "\n");
		out.flush();
		
		jsout = new JSONObject();
		jsout.put("method", 0);
		out.write(jsout.toString() + "\n");
		out.flush();
		
		
		
		String readString = in.readLine();
		if(readString.equals("1"))
		{
			System.out.println("Error");
			System.exit(0);
		}
		JSONObject jsin = new JSONObject(readString);
		JSONArray jsona = (JSONArray) jsin.get("selectArrayList");
		Iterator<Object> ite = jsona.iterator();
		while(ite.hasNext()) {
			JSONObject j = (JSONObject) ite.next();
			System.out.println(j.get("cID") + " " + j.get("cName") + " " + j.get("cCapacity") + " " + j.get("cSelected") + " " + j.get("cRemain"));
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
