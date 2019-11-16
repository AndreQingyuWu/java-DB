package initializationsandtests;

import java.io.IOException;

import databaseoperations.DataBaseOperations;
import databaseoperations.Flag;
import data.StudentData;

public class TestAddStudent {
	static Flag flag = new Flag();
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		DataBaseOperations dbBase = new DataBaseOperations();
		dbBase.addStudent(111111, 123, "蔡徐坤", false, flag);
		StudentData student = dbBase.getStudent(111111);
		System.out.println(student.sID + " " + student.sName);
		dbBase.deleteStudent(111111, flag);
		dbBase.getStudent(111111);
		dbBase.addStudent(111111, 123, "徐坤菜", false, flag);
		student = dbBase.getStudent(111111);
		System.out.println(student.sID + " " + student.sName);
		dbBase.deleteStudent(111111, flag);
	}
}
