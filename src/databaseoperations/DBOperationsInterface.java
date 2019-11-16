package databaseoperations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import data.*;

/**
 * 数据库操作功能接口
 * @author zhaowenbo
 *
 */

public interface DBOperationsInterface {
	
	boolean select(long sID, long cID, Flag flag)	//选课
			throws IOException, ClassNotFoundException;
	boolean drop(long sID, long cID, Flag flag)	//退课
			throws IOException, ClassNotFoundException;
	
	StudentData getStudent(long sID)	//获取学生信息
			throws IOException, ClassNotFoundException;
	public boolean deleteStudent(long sID, Flag flag)	//删除学生信息
			throws ClassNotFoundException, IOException;
	public boolean addStudent(long sID, int sClass, String sName, boolean sGender, Flag flag)	//增加新学生信息
			throws ClassNotFoundException, IOException;
	boolean setStudentClass(long sID,int sClass, Flag flag)	//修改学生班级
			throws IOException, ClassNotFoundException;
	boolean setStudentGender(long sID, boolean sGender, Flag flag)	//修改学生性别 
			throws IOException, ClassNotFoundException;
	boolean setStudentName(long sID, String sName, Flag flag)	//修改学生姓名
			throws IOException, ClassNotFoundException;
	ArrayList<SelectData> getSelectList(long sID)	//获取学生的选课列表
			throws IOException, ClassNotFoundException;
	boolean clearSelectList(long sID)	//清空学生的选课列表
			throws IOException, ClassNotFoundException;
	
	public CourseData getCourse(long cID) 
			throws ClassNotFoundException, IOException;	//获取课程信息
	public boolean deleteCourse(long cID, Flag flag)	//删除课程信息
			throws ClassNotFoundException, IOException;
	public boolean addCourse(long cID, String cName, int cCapacity, Flag flag)	//增加课程信息
			throws ClassNotFoundException, IOException;
	public boolean setCourseName(long cID, String cName, Flag flag)	//修改课程名称
			throws ClassNotFoundException, IOException;
	public boolean setCourseCapacity(long cID, int cpacity, Flag flag)	//修改课程容量
			throws ClassNotFoundException, IOException;
	public TreeMap<Long, CourseData> getCourseMap()	//获得课程列表
			throws ClassNotFoundException, IOException;
}
