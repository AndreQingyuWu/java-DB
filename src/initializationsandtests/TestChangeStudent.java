package initializationsandtests;

import java.io.IOException;

import databaseoperations.DataBaseOperations;
import databaseoperations.Flag;
import data.StudentData;

public class TestChangeStudent {
	static Flag flag = new Flag();
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		long t1 = System.currentTimeMillis();
		DataBaseOperations dbBase = new DataBaseOperations();
		StudentData student = dbBase.getStudent(123456);
		System.out.println(student.sID + " " + student.sClass + " " 
				+ student.sName + " " + student.sGender);
		dbBase.setStudentClass(123456, 888, flag);
		dbBase.setStudentGender(123456, false, flag);
		dbBase.setStudentName(123456, "徐坤菜", flag);
		
		student = dbBase.getStudent(123456);
		System.out.println(student.sID + " " + student.sClass + " " 
				+ student.sName + " " + student.sGender);
		long t2 = System.currentTimeMillis();
		System.out.println(t2-t1 + "ms");
	}
}
