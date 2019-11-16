package initializationsandtests;

import java.io.IOException;

import databaseoperations.DataBaseOperations;
import data.StudentData;

public class TestGetStudent {
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		long t1 = System.currentTimeMillis();
		DataBaseOperations dataBase = new DataBaseOperations();
		StudentData student1 = dataBase.getStudent(123123L);
		StudentData student2 = dataBase.getStudent(34355L);
		StudentData student3 = dataBase.getStudent(32075L);
		StudentData student4 = dataBase.getStudent(312355L);
		
		StudentData student = student1;
		System.out.println("" + student.sID + " " + student.sName
				+ " " + student.sClass + " " + student.sGender);
		student = student2;
		System.out.println("" + student.sID + " " + student.sName
				+ " " + student.sClass + " " + student.sGender);
		student = student3;
		System.out.println("" + student.sID + " " + student.sName
				+ " " + student.sClass + " " + student.sGender);
		student = student4;
		System.out.println("" + student.sID + " " + student.sName
				+ " " + student.sClass + " " + student.sGender);
		
		long t2 = System.currentTimeMillis();
		System.out.println(t2-t1 + "ms");
	}
}
