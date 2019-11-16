

import net.sf.json.JSONObject;

public class Course {
	long cID;
	String cName;
	int cCapacity;
	int cRemain;
	int cSelected;

	public Course(long ID,String Name,int Capacity,int Remain,int Selected) {
		cID=ID;
		cName=Name;
		cCapacity=Capacity;
		cRemain=Remain;
		cSelected=Selected;
	}
	public Course(JSONObject json)
	{
		cID=json.getLong("cID");
		cName=json.getString("cName");
		cCapacity=json.getInt("cCapacity");
		cRemain=json.getInt("cRemain");
		cSelected=json.getInt("cSelected");

	}
	public void print()
	{
		System.out.println("Course ID:"+String.format("%010d",cID)+"\t"
						  +"Course Name:"+cName+"\t"
						  +"Course Capacity:"+cCapacity+"\t"
						  +"Course Remain:"+cRemain+"\n");
	}

}
