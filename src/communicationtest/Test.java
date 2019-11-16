package communicationtest;

import org.json.JSONObject;

public class Test {
	public static void main(String[] args) {
		JSONObject j = new JSONObject();
		JSONObject x = new JSONObject();
		x.put("C", "CCC");
		JSONObject[] ja = new JSONObject[3];
		ja[0] = x;
		ja[1] = x;
		ja[2] = x;
		j.put("array", ja);
		System.out.println(j.toString());
	}
}
