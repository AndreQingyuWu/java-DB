package data;

import java.io.Serializable;

public class StudentData implements Serializable {
	private static final long serialVersionUID = 1L;
	public long sID;// 00000000
	public int sClass; //1701
	public boolean sGender; //0-male 1-female
	public String sName; //
	public StudentData(long sID, int sClass, boolean sGender, String sName) {
		this.sID = sID;
		this.sClass = sClass;
		this.sGender = sGender;
		this.sName = sName;
	}
	public boolean equals(Object o) {	//重载equals
		if(this == o) 
			return true;
		if(o == null) 
			return false;	
		if(this.getClass() != o.getClass()) 
			return false;
		StudentData c = (StudentData)o;
		if(this.sID==c.sID)
			return true;
		else
			return false;
	}
}
