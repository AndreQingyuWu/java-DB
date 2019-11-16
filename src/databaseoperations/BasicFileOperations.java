package databaseoperations;

import java.io.*;
import java.util.TreeMap;

/**
 * 按索引查找文件操作的工具类
 * 为了减少索引文件的读取,需要把此类实例化
 * @author zhaowenbo
 *
 */

public class BasicFileOperations {
	
	static TreeMap<Long, String> selectIndexMap = null;
	static TreeMap<Long, String> studentIndexMap = null;	
	
	@SuppressWarnings({ "unchecked", "resource" })
	public BasicFileOperations() 	//实例化该类时就读取索引文件,减少之后的索引文件读取次数
			throws ClassNotFoundException, FileNotFoundException, IOException {
		//索引的Key值指学号介于Key*100~Key*100+99之间的学生信息
		BasicFileOperations.selectIndexMap = (TreeMap<Long, String>) 
				new ObjectInputStream(new BufferedInputStream(new FileInputStream("SelectIndex"))).readObject();
		System.out.println("选课信息索引调入内存");
		BasicFileOperations.studentIndexMap = (TreeMap<Long, String>)
				new ObjectInputStream(new BufferedInputStream(new FileInputStream("StudentIndex"))).readObject();
		System.out.println("学生信息索引调入内存");
	}
	
	public static String findSelectFile(long sID) throws IOException, ClassNotFoundException { //按studentID通过索引方式查找选课记录文件的方法
		if(sID>1000000L || sID<=0) {	//有一千万个学生的信息(学号是8位, 前两位都是0)
			//System.out.println("该学号不合法");
			return null;
		}
		long index = (long)(sID/100);
		String path = selectIndexMap.get(index);
		
		return path;
	}
	
	public static String findStudentFile(long sID) throws IOException, ClassNotFoundException { //按studentID通过索引方式查找学生信息文件的方法
		if(sID>=1000000L || sID<=0) {	//有一千万个学生的信息(学号是6位)
			//System.out.println("该学号不合法");
			return null;
		}
		long index = (long)(sID/100);
		String path = studentIndexMap.get(index);
		
		return path;
	}
	
	public static String findCourseFile(long cID) throws IOException { //按studentID查找课程信息文件的方法
		if(cID>=10000000000L || cID<=0L) {	//课程号是10位
			//System.out.println("课程号不合法");
			return null;
		}
		return "CourseData/course";
	}	
}
