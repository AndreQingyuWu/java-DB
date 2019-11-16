
import net.sf.json.JSONObject;

public class Student {
    long sID;
    int sClass;
    boolean sGender;
    String sName;

    public Student(long ID, int Class, boolean Gender,String Name) {
    	sID=ID;
    	sClass=Class;
    	sGender=Gender;
    	sName=Name;
    }

    public Student(JSONObject json)
    {
        sID=json.getLong("sID");
    	sClass=json.getInt("sClass");
    	sGender=json.getBoolean("sGender");
    	sName=json.getString("sName");
    }

    public void print()
    {
        System.out.print("Student ID:"+String.format("%08d",sID)+"\t"
						  +"Student Name:"+sName+"\t"
						  +"Student Class:"+sClass+"\t");
        if(sGender)
        {
            System.out.println("Student gender:female");
        }
        else
        {
            System.out.println("Student gender:male");
        }
    }
    
}
