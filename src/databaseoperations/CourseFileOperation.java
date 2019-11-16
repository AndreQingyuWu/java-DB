package databaseoperations;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

import data.CourseData;

/**
 * 操作Course数据文件的工具类
 * @author zhaowenbo
 *
 */

public class CourseFileOperation extends BasicFileOperations{
	
	public CourseFileOperation() throws ClassNotFoundException, FileNotFoundException, IOException {
		super();
		System.out.println("CourseFileOperation 工具类构造完成");
	}

	static File courseData = null;

	public void mapWriteBack(long cID, TreeMap<Long, CourseData> m) throws IOException {
		TreeMap<Long,CourseData> map = m;
		courseData = new File(findCourseFile(cID));	//找到存储 course ID == cID 的课程信息文件
		ObjectOutputStream courseDataOutputStream =	//更新courseData的输出流
				new ObjectOutputStream(
						new BufferedOutputStream(
								new FileOutputStream(courseData)));
		courseDataOutputStream.writeObject(map);	//写回文件
		courseDataOutputStream.flush();
		courseDataOutputStream.close();
	}

	public TreeMap<Long, CourseData> getTreeMap(long cID) throws IOException, ClassNotFoundException {
		try{
			courseData = new File(findCourseFile(cID));	//找到存储 course ID == cID 的课程信息文件
		}catch (NullPointerException e) {
			System.out.println("该课程号不合法");
			return null;
		}

		ObjectInputStream courseDataInputStream = 	//获得courseData的输入流
				new ObjectInputStream(
						new BufferedInputStream(
								new FileInputStream(courseData)));
		TreeMap<Long, CourseData> map = (TreeMap<Long, CourseData>)courseDataInputStream.readObject();
		courseDataInputStream.close();
		return map;
	}
	
	public TreeMap<Long, CourseData> getTreeMap() throws IOException, ClassNotFoundException {
		ObjectInputStream courseDataInputStream = 	//获得courseData的输入流
				new ObjectInputStream(
						new BufferedInputStream(
								new FileInputStream("CourseData/course")));
		TreeMap<Long, CourseData> map = (TreeMap<Long, CourseData>)courseDataInputStream.readObject();
		courseDataInputStream.close();
		return map;
	}
}
