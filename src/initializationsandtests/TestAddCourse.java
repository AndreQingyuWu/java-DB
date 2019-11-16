package initializationsandtests;

import java.io.IOException;

import databaseoperations.DataBaseOperations;
import databaseoperations.Flag;
import data.CourseData;

public class TestAddCourse {
	static Flag flag = new Flag();
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		DataBaseOperations dbBase = new DataBaseOperations();
		dbBase.addCourse(444444444, "Engineering Mathematics", 100, flag);
		CourseData course = dbBase.getCourse(444444444);
		System.out.println(course.cID + " " + course.cName + " " + course.cRemain);
		dbBase.deleteCourse(444444444, flag);
		course = dbBase.getCourse(444444444);
	}
}
