package data;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class SelectData implements Serializable{
	private static final long serialVersionUID = 1L;
	public long sID;// 00000000 8位
	public long cID; //0000000000 10位
	public String datatime; //yyyy-mm-dd HH:MM:SS  2019-09-16 23:59:11
	public SelectData(long sID, long cID, String datatime) {
		this.sID = sID;
		this.cID = cID;
		this.datatime = datatime;
	}
	public boolean equals(Object o) {	//重载equals
		if(this == o) 
			return true;
		if(o == null) 
			return false;	
		if(this.getClass() != o.getClass()) 
			return false;
		SelectData c = (SelectData)o;
		if(this.sID==c.sID && this.cID==c.cID)
			return true;
		else
			return false;
	}
}
