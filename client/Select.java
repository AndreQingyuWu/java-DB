
import java.text.SimpleDateFormat;


import net.sf.json.JSONObject;

public class Select {
	long sID;
	long cID;
	String datatime;
	String cName;

	public Select(long studentID,long classID) {
		sID=studentID;
		cID=classID;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
		datatime=(String)df.format(System.currentTimeMillis()); 
	}

	public Select(JSONObject json)
	{
		sID=json.getLong("sID");
		cID=json.getLong("cID");
		datatime=json.getString("datatime");
		cName=json.getString("cName");
	}

	public String getCourse()
	{
		return String.format("%010d",cID)+" "+cName;
	}
	
}


