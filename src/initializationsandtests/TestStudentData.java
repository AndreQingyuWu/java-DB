package initializationsandtests;

import java.io.*;
import java.security.KeyStore.Entry;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import data.*;

public class TestStudentData {
	static StudentData student = null;
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		File file = new File("StudentData/300");
		ObjectInputStream ois = 
				new ObjectInputStream(
						new BufferedInputStream(
								new DataInputStream(
										new FileInputStream(file))));
		TreeMap<Long, StudentData> map = (TreeMap<Long, StudentData>) ois.readObject();
		for (Long key : map.keySet()) {
			student = map.get(key);
			System.out.println("" + student.sID + " " + student.sName + " " + student.sGender 
					+ " " + student.sClass);
		}
	}
}
