package databaseoperations;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import databaseoperations.CourseFileOperation;
import databaseoperations.SelectFileOperation;
import databaseoperations.StudentFileOperation;
import data.*;

/**
 * 实现了DBOperation接口的方法以及按索引来查找保存了学生信息的Map文件的方法
 * @author zhaowenbo
 * 实例化DBOperations类让索引文件在服务器进程启动的时候就调入主存, 进程结束的时候才清除, 减少索引文件的读取
 */


public class DataBaseOperations implements DBOperationsInterface{

	private static CourseFileOperation coursefo = null;
	private static StudentFileOperation studentfo = null;
	private static SelectFileOperation selectfo = null;
	
	public DataBaseOperations() {	//创建工具类的同时创建各种不同的专门工具类
		try {
			DataBaseOperations.coursefo = new CourseFileOperation();
			DataBaseOperations.studentfo = new StudentFileOperation();
			DataBaseOperations.selectfo = new SelectFileOperation();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.print("数据库工具类创建错误!");
			System.exit(0);
		}
		System.out.println("DataBaseOperations 工具类构造完成");
	}
	
	@Override
	public StudentData getStudent(long sID) throws IOException, ClassNotFoundException{	//获得学生信息
		TreeMap<Long,StudentData> studentDataMap = studentfo.getTreeMap(sID);	//获得一个文件里的Map
		if(studentDataMap==null || studentDataMap.get(sID) == null) {
			System.out.println("该学生不存在");
			return null;
		}
		else {
			StudentData tempStudent = studentDataMap.get(sID);
			return tempStudent;
		}
	}

	@Override
	public ArrayList<SelectData> getSelectList(long sID) throws IOException, ClassNotFoundException {	//获得某个学生的选课信息
		TreeMap<Long,ArrayList<SelectData>> selectDataMap = selectfo.getTreeMap(sID); //获得一个文件里的Map
		if(selectDataMap == null || selectDataMap.get(sID) == null) {
			System.out.println("没有这位同学的选课信息");
			return null;
		}
		else {
			ArrayList<SelectData> tempSelectList = selectDataMap.get(sID);
			return tempSelectList;
		}
	}

	synchronized public CourseData getCourse(long cID) 	//获取课程信息
			throws ClassNotFoundException, IOException {
		TreeMap<Long, CourseData> courseDataMap = coursefo.getTreeMap(cID);
		if(courseDataMap==null || !courseDataMap.containsKey(cID)) {
			System.out.println("该课程不存在");
			return null;
		}
		return courseDataMap.get(cID);
	}
	
	@Override
	synchronized public boolean select(long sID, long cID, Flag flag) throws IOException, ClassNotFoundException {	//选课操作
		TreeMap<Long,CourseData> courseDataMap = coursefo.getTreeMap(cID);	//获得一个文件里的Map
		TreeMap<Long,ArrayList<SelectData>> selectDataMap = selectfo.getTreeMap(sID);	//获得一个文件里的Map

		CourseData course = courseDataMap.get(cID);
		//System.out.println(course);
		if(course==null) {	//未找到该课程
			System.out.println("未找到该课程");
			flag.flagByte = 0;
			return false;
		}
		
		if(course.cRemain==0) {	//该课程没有课程剩余量了
			System.out.println("该课程没有剩余量了");
			flag.flagByte = 1;
			return false;
		}
		
		ArrayList<SelectData> selectList = selectDataMap.get(sID);	//获得学生的选课信息
		if(selectList == null) {	//未找到该学生的选课信息
			System.out.println("该学生不存在");
			flag.flagByte = 2;
			return false;
		}
		if(selectList.contains(new SelectData(sID, cID, null))) {	//该学生已经选过这门课程了
			System.out.println("该学生已选过这门课程");
			flag.flagByte = 3;
			return false;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //获得当前时间
		String datatime = sdf.format(System.currentTimeMillis());
		selectList.add(new SelectData(sID, cID, datatime));	//选课信息写入
		course.cSelected = course.cSelected + 1;	//更新课程信息
		course.cRemain = course.cRemain -1;
		
		coursefo.mapWriteBack(cID, courseDataMap);
		selectfo.mapWriteBack(sID, selectDataMap);
		return true;
	}

	@Override
	synchronized public boolean drop(long sID, long cID, Flag flag) throws IOException, ClassNotFoundException {	//退课操作
		TreeMap<Long,CourseData> courseDataMap = coursefo.getTreeMap(cID);	//获得一个文件里的Map
		TreeMap<Long,ArrayList<SelectData>> selectDataMap = selectfo.getTreeMap(sID);	//获得一个文件里的Map
		
		CourseData course = courseDataMap.get(cID);
		if(course==null) {	//未找到该课程
			System.out.println("该课程不存在");
			flag.flagByte = 0;
			return false;
		}
		
		ArrayList<SelectData> selectList = selectDataMap.get(sID);	//获得学生的选课信息
		if(selectList == null) {	//未找到该学生的选课信息
			System.out.println("该学生不存在");
			flag.flagByte = 1;
			return false;
		}
		if(!selectList.contains(new SelectData(sID, cID, null))) {	//该同学没有选过这门课
			System.out.println("并未选过这门课");
			flag.flagByte = 2;
			return false;
		}
		selectList.remove(new SelectData(sID, cID, null));	//删除这条选课记录
		course.cSelected--;	//更新课程信息
		course.updateInfo();
		
		coursefo.mapWriteBack(cID, courseDataMap);
		selectfo.mapWriteBack(sID, selectDataMap);

		return true;
	}

	@Override
	synchronized public boolean setStudentClass(long sID, int sClass, Flag flag) throws IOException, ClassNotFoundException {	//修改学生班级信息
		TreeMap<Long,StudentData> studentDataMap = studentfo.getTreeMap(sID);	//获得一个文件里的Map
		if(studentDataMap.get(sID)==null) {	//未找到该学生
			System.out.println("该学生不存在");
			flag.flagByte = 0;
			return false;
		}
		StudentData tempStudent = studentDataMap.get(sID);	//找到该学生
		tempStudent.sClass = sClass;
		
		studentfo.mapWriteBack(sID, studentDataMap);
		return true;
	}

	@Override
	synchronized public boolean setStudentGender(long sID, boolean sGender, Flag flag) throws IOException, ClassNotFoundException {	//修改学生性别信息
		TreeMap<Long,StudentData> studentDataMap = studentfo.getTreeMap(sID);	//获得一个文件里的Map
		if(studentDataMap==null || studentDataMap.get(sID)==null) {	//未找到该学生
			System.out.println("该学生不存在");
			flag.flagByte = 0;
			return false;
		}
		StudentData tempStudent = studentDataMap.get(sID);	//找到该学生
		tempStudent.sGender = sGender;
		
		studentfo.mapWriteBack(sID, studentDataMap);
		return true;
	}

	@Override
	synchronized public boolean setStudentName(long sID, String sName, Flag flag) throws IOException, ClassNotFoundException {	//修改学生姓名信息
		TreeMap<Long,StudentData> studentDataMap = studentfo.getTreeMap(sID);	//获得一个文件里的Map
		if(studentDataMap.get(sID)==null) {	//未找到该学生
			System.out.println("该学生不存在");
			flag.flagByte = 0;
			return false;
		}
		StudentData tempStudent = studentDataMap.get(sID);	//找到该学生
		tempStudent.sName = sName;

		studentfo.mapWriteBack(sID, studentDataMap);
		return true;
	}

	synchronized public boolean addStudent(long sID, int sClass, String sName, boolean sGender, Flag flag)	//增加新学生
			throws ClassNotFoundException, IOException {
		TreeMap<Long,StudentData> studentDataMap = studentfo.getTreeMap(sID);
		if(studentDataMap.containsKey(sID)) {
			System.out.println("该学生已经存在");
			flag.flagByte = 0;
			return false;
		}
		StudentData student = new StudentData(sID, sClass, sGender, sName);
		studentDataMap.put(sID, student);
		
		studentfo.mapWriteBack(sID, studentDataMap);
		return true;
	}
	
	synchronized public boolean deleteStudent(long sID, Flag flag)	//删除学生信息
			throws ClassNotFoundException, IOException {
		TreeMap<Long,StudentData> studentDataMap = studentfo.getTreeMap(sID);
		if(!studentDataMap.containsKey(sID)) {
			System.out.println("该学生不存在");
			flag.flagByte = 0;
			return false;
		}
		studentDataMap.remove(sID);
		
		studentfo.mapWriteBack(sID, studentDataMap);
		return true;
	}
	
	synchronized public boolean addCourse(long cID, String cName, int cCapacity, Flag flag)	//增加新课程
			throws ClassNotFoundException, IOException {
		TreeMap<Long, CourseData> courseDataMap = coursefo.getTreeMap(cID);
		if(courseDataMap.containsKey(cID)) {
			System.out.println("该课程已存在");
			flag.flagByte = 0;
			return false;
		}
		CourseData course = new CourseData(cID, cCapacity, cName);
		courseDataMap.put(cID, course);
		
		coursefo.mapWriteBack(cID, courseDataMap);
		return true;
	}
	
	synchronized public boolean deleteCourse(long cID, Flag flag)	//删除课程 
			throws ClassNotFoundException, IOException {
		TreeMap<Long, CourseData> courseDataMap = coursefo.getTreeMap(cID);
		if( courseDataMap==null || !courseDataMap.containsKey(cID)) {
			System.out.println("该课程不存在");
			flag.flagByte = 0;
			return false;
		}
		else {
			CourseData course = courseDataMap.get(cID);
			if(course.cSelected>0) {
				flag.flagByte = 1;
				return false;
			}
			else {
				courseDataMap.remove(cID);
				coursefo.mapWriteBack(cID, courseDataMap);
				return true;
			}
		}
	}

	@Override
	public boolean setCourseName(long cID, String cName, Flag flag)	//修改课程名称
			throws ClassNotFoundException, IOException {
		TreeMap<Long, CourseData> courseDataMap = coursefo.getTreeMap(cID);
		if(!courseDataMap.containsKey(cID)) {
			System.out.println("该课程不存在");
			flag.flagByte = 0;
			return false;
		}
		else {
			CourseData course = courseDataMap.get(cID);
			course.cName = cName;
			coursefo.mapWriteBack(cID, courseDataMap);
			return true;
		}
	}

	@Override
	public boolean setCourseCapacity(long cID, int cpacity, Flag flag)		//修改课程容量
			throws ClassNotFoundException, IOException {
		TreeMap<Long, CourseData> courseDataMap = coursefo.getTreeMap(cID);
		if(!courseDataMap.containsKey(cID)) {
			System.out.println("该课程不存在");
			flag.flagByte = 0;
			return false;
		}
		else {
			CourseData course = courseDataMap.get(cID);
			if(cpacity < course.cSelected) {
				System.out.println("课程容量低于已报课程学生数");
				flag.flagByte = 1;
				return false;
			}
			else {
				course.cCapacity = cpacity;
				course.cRemain = course.cCapacity - course.cSelected;
				coursefo.mapWriteBack(cID, courseDataMap);
				return true;
			}
		}
	}

	@Override
	public TreeMap<Long, CourseData> getCourseMap()	//获得所有课程信息
			throws ClassNotFoundException, IOException {
		return coursefo.getTreeMap();
	}

	@Override
	public boolean clearSelectList(long sID) throws IOException, ClassNotFoundException {	//清空某个学生的选课列表
		TreeMap<Long,ArrayList<SelectData>> selectDataMap = selectfo.getTreeMap(sID);
		if(selectDataMap == null || selectDataMap.get(sID) == null) {
			return true;
		}
		else {
			if(selectDataMap.containsKey(sID)) {
				ArrayList<SelectData> arrayList = selectDataMap.get(sID);
				Iterator<SelectData> it = arrayList.iterator();
				while(it.hasNext()) {
					SelectData sd = it.next();
					drop(sID, sd.cID, null);
				}
				arrayList.clear();
				selectfo.mapWriteBack(sID, selectDataMap);
				return true;
			}
			else
				return true;
		}
	}
}
