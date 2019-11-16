package databaseoperations;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

import data.StudentData;

/**
 * 操作Student数据文件的工具类
 * @author zhaowenbo
 *
 */

public class StudentFileOperation extends BasicFileOperations {
	
	public StudentFileOperation() throws ClassNotFoundException, FileNotFoundException, IOException {
		super();
		System.out.println("StudentFileOperation 工具类构造完成");
	}

	static File studentData = null;

	public void mapWriteBack(long sID, TreeMap<Long, StudentData> m) throws IOException, ClassNotFoundException {
		TreeMap<Long, StudentData> map = m;
		studentData = new File(findStudentFile(sID));	//找到存储student ID == sID 的学生信息文件
		ObjectOutputStream studentDataOutputStream =
				new ObjectOutputStream(
						new BufferedOutputStream(
								new FileOutputStream(studentData)));
		studentDataOutputStream.writeObject(map);
		studentDataOutputStream.flush();
		studentDataOutputStream.close();
	}

	public TreeMap<Long, StudentData> getTreeMap(long sID) throws IOException, ClassNotFoundException {
		try{
			studentData = new File(findStudentFile(sID));	//找到存储student ID == sID 的学生信息文件
		}catch(NullPointerException e){
			System.out.println("该学号不合法");
			return null;
		}
		if(studentData==null)
			return null;
		ObjectInputStream studentDataInputStream = 	//获得studentMap输入流
				new ObjectInputStream(
						new BufferedInputStream(
								new FileInputStream(studentData)));
		TreeMap<Long, StudentData> map = 
				(TreeMap<Long, StudentData>)studentDataInputStream.readObject(); 
		studentDataInputStream.close();
		return map;
	}
	
	
}
