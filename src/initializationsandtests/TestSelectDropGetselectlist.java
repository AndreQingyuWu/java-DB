package initializationsandtests;

import java.io.IOException;
import java.util.ArrayList;

import data.SelectData;
import databaseoperations.DataBaseOperations;
import databaseoperations.Flag;
import databaseoperations.DataBaseOperations;
import data.SelectData;

public class TestSelectDropGetselectlist {
	static Flag flag = new Flag();
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		DataBaseOperations dbBase = new DataBaseOperations();
		dbBase.select(111111L, 1111111111L, flag);
		dbBase.select(111111L, 2222222222L, flag);
		dbBase.select(111111L, 3333333333L, flag);
		ArrayList<SelectData> selectList = dbBase.getSelectList(111111L);
		for(SelectData select : selectList) {
			System.out.println(select.sID + " " + select.cID + " " + select.datatime);
		}
		System.out.println("");
		//dbBase.drop(111111, 111111111);
		dbBase.drop(111111, 2222222222L, flag);
		selectList = dbBase.getSelectList(111111L);
		for(SelectData select : selectList) {
			System.out.println(select.sID + " " + select.cID + " " + select.datatime);
		}
	}
}
