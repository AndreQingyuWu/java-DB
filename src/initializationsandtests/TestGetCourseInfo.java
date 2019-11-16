package initializationsandtests;

import java.io.IOException;

import data.CourseData;
import databaseoperations.DataBaseOperations;
import databaseoperations.Flag;

public class TestGetCourseInfo {
	static Flag flag = new Flag();
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		DataBaseOperations dbBase = new DataBaseOperations();
		CourseData  course = dbBase.getCourse(2222222222L);
		System.out.println(course.cID);
		System.out.println(course.cName);
		System.out.println(course.cCapacity);
		
		dbBase.setCourseCapacity(2222222222L, 100, flag);
		course = dbBase.getCourse(2222222222L);
		System.out.println(course.cCapacity);
		dbBase.setCourseCapacity(2222222222L, 90, flag);
		
		dbBase.setCourseName(2222222222L, "DataBase", flag);
		course = dbBase.getCourse(2222222222L);
		System.out.println(course.cName);
		dbBase.setCourseName(2222222222L, "Operating System", flag);
	}
}
