package data;

import java.io.Serializable;

public class CourseData implements Serializable{
	private static final long serialVersionUID = 1L;
	public long cID; //0000000000
	public String cName; //数据结构 操作系统 线性代数
	public int cCapacity; //100
	public int cRemain; //cCapacity-cSelected 048
	public int cSelected; //052
	public CourseData(long cID, int cCapacity, String cName) {
		this.cID = cID;
		this.cName = cName;
		this.cCapacity = cCapacity;
		this.cRemain = cCapacity;
		this.cSelected = 0;
	}
	public void updateInfo() {
		this.cRemain = this.cCapacity - this.cSelected;
	}
	public boolean equals(Object o) {	//重载equals
		if(this == o) 
			return true;
		if(o == null) 
			return false;	
		if(this.getClass() != o.getClass()) 
			return false;
		CourseData c = (CourseData)o;
		if(this.cID==c.cID)
			return true;
		else
			return false;
	}
}
