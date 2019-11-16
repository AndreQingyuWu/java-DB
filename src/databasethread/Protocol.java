package databasethread;

import java.io.*;
import java.net.*;
import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import org.json.*;

import data.CourseData;
import data.SelectData;
import data.StudentData;
import databaseoperations.DBOperationsInterface;
import databaseoperations.Flag;

/**
 * 通信协议文件
 * @author zhaowenbo
 *
 */

public class Protocol implements DataBaseStrategy {
	private String order = null;//指令的参数
	JSONObject jsin = null;//输入JSONObject对象
	JSONObject jsout = null;//输出JSONObject对象
	boolean result = false;
	int op = 0;
	static Flag flag = new Flag();//错误种类报告标志
	String reason = " ";
	static String request = null;
	
	@Override
	public void service(Socket socket, DBOperationsInterface dbo) {//实现接口的service函数
		flag.flagByte = -1;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
			
			int command = 0;
			while (true) {//输入流的第一个字节是数字指令
				request=in.readLine();
				//TODO 测试用
				System.out.println(Thread.currentThread().getName()+" request:"+request.toString());
				jsin = new JSONObject(request);
				command = jsin.getInt("method");//读指令
				switch (command) {
				case 0://0代表关闭socket
					System.out.println("close connection.");
					try {
						socket.close();
					} catch (Exception e) {
					}
					return;
				case 1://查询学生信息
					StudentData sd = dbo.getStudent(Long.parseLong((String)jsin.get("sID")));
					if(sd==null) {
						out.println("1");
						break;
					}
					jsout = new JSONObject();//输出JSONObject对象
					jsout.put("sName", sd.sName);
					jsout.put("sClass", sd.sClass);
					jsout.put("sGender", sd.sGender);
					jsout.put("sID",sd.sID);
					out.println(jsout.toString());
					break;	
				case 2://查询学生的选课列表
					ArrayList<SelectData> selectDataList =	//学生的选课列表
							dbo.getSelectList(Long.parseLong((String)jsin.get("sID")));
					int leng = selectDataList.size();//选课列表的长度(所选课程数)
					if(leng == 0 || selectDataList==null) {
						out.println("1");
						break;
					}
					JSONObject[] ja = new JSONObject[leng];//JSONArray
					int i = 0;//计数
					JSONObject j = null;
					SelectData selectData = null;
					Iterator<SelectData> ite = selectDataList.iterator();
					while(i<leng) {
						selectData = ite.next();
						String courseName = dbo.getCourse(selectData.cID).cName;
						j = new JSONObject();
						j.put("cID", ((Long)selectData.cID).toString());
						j.put("cName", courseName);
						j.put("datatime", selectData.datatime);
						j.put("sID",Long.parseLong((String)jsin.get("sID")));
						ja[i] = j;
						i++;
					}
					jsout = new JSONObject();
					jsout.put("selectArrayList", ja);
					out.println(jsout.toString());
					break;
				case 3://选课
					System.out.println(jsin.toString());
					if(dbo.getStudent(Long.parseLong((String) jsin.get("sID")))==null) {
						reason = "该学生不存在";
						jsout = new JSONObject();
						jsout.put("return", false);
						jsout.put("reason", reason);
						out.println(jsout.toString());
						break;
					}
					result = dbo.select(Long.parseLong((String) jsin.get("sID")),
							Long.parseLong((String) jsin.get("cID")),
							flag);
					System.out.println(result);
					jsout = new JSONObject();
					if(result == true)
						jsout.put("return", result);
					else {
						if(flag.flagByte == 0)
							reason = "未找到该课程";
						else if(flag.flagByte == 1)
							reason = "该课程已没有剩余量";
						else if(flag.flagByte == 2)
							reason = "该学生不存在";
						else if(flag.flagByte == 3)
							reason = "该学生已选过这门课程";
						jsout.put("return", result);
						jsout.put("reason", reason);
					}
					out.println(jsout.toString());
					break;
				case 4://退课
					if(dbo.getStudent(Long.parseLong((String) jsin.get("sID")))==null) {
						reason = "该学生不存在";
						jsout = new JSONObject();
						jsout.put("return", false);
						jsout.put("reason", reason);
						out.println(jsout.toString());
						break;
					}
					result = dbo.drop(Long.parseLong((String) jsin.get("sID")),
							Long.parseLong((String) jsin.get("cID")),
							flag);
					jsout = new JSONObject();
					if(dbo.getStudent(Long.parseLong((String) jsin.get("sID")))==null)
						reason = "该学生不存在";
					if(result == true)
						jsout.put("return", result);
					else {
						if(flag.flagByte == 0)
							reason = "该课程不存在";
						else if(flag.flagByte == 1)
							reason = "该学生不存在";
						else if(flag.flagByte == 2)
							reason = "该学生并未选过这门课";
						jsout.put("return", result);
						jsout.put("reason", reason);
					}
					out.println(jsout.toString());
					break;
				case 5://修改学⽣生信息
					op = (int) jsin.get("op");//不同的修改操作
					
					if(op == 1){//修改学生姓名
						result = dbo.setStudentName(Long.parseLong((String) jsin.get("sID")), 
								(String) jsin.get("change"),
								flag);
					}
					else if(op == 2) {//修改学生班级
						result = dbo.setStudentClass(Long.parseLong((String) jsin.get("sID")), 
								(int) jsin.get("change"),
								flag);
					}
					else if(op == 3) {//修改学生性别
						result = dbo.setStudentGender(Long.parseLong((String) jsin.get("sID")),
								(boolean) jsin.get("change"),
								flag);
					}
					else {//修改操作标志符错误
						jsout = new JSONObject();
						jsout.put("return", false);
						jsout.put("reason", "Invalid operation number!");
						out.println(jsout.toString());
						break;
					}
					
					jsout = new JSONObject();
					if(result == true)
						jsout.put("return", true);
					else {
						if(flag.flagByte == 0)
							reason = "该学生不存在";
						jsout.put("return", false);
						jsout.put("reason", reason);
					}
					out.println(jsout.toString());
					break;
				case 6://增加学生
					result = dbo.addStudent(Long.parseLong((String) jsin.get("sID")), 
							(int)jsin.get("sClass"), 
							(String)jsin.get("sName"), 
							(boolean)jsin.get("sGender"),
							flag);
					jsout = new JSONObject();
					if(result == true)
						jsout.put("return", true);
					else {
						if(flag.flagByte == 0)
							reason = "该学生已经存在";
						jsout.put("return", false);
						jsout.put("reason", reason);
					}
					out.println(jsout.toString());
					break;
				case 7://删除学生
					result = dbo.deleteStudent(Long.parseLong((String) jsin.get("sID")), flag);
					dbo.clearSelectList(Long.parseLong((String) jsin.get("sID")));
					jsout = new JSONObject();
					if(result == true)
						jsout.put("return", true);
					else {
						if(flag.flagByte == 0)
							reason = "该学生不存在";
						jsout.put("return", false);
						jsout.put("reason", reason);
					}
					out.println(jsout.toString());
					break;
				case 8://获取课程信息 
					CourseData cd = dbo.getCourse(Long.parseLong((String) jsin.get("cID")));
					if(cd==null) {
						out.println("1");
						break;
					}
					jsout = new JSONObject();
					jsout.put("cName", cd.cName);
					jsout.put("cCapacity", cd.cCapacity);
					jsout.put("cSelected", cd.cSelected);
					jsout.put("cRemain", cd.cRemain);
					out.println(jsout.toString());
					break;
				case 9://增加课程,默认课程选课人数为0
					 
					result = dbo.addCourse(Long.parseLong((String) jsin.get("cID")), 
							(String) jsin.get("cName"), 
							(int) jsin.get("cCapacity"),
							flag);
					System.out.println("Add Course 1 ... ...");
					jsout = new JSONObject();
					if(result == true) {
						jsout.put("return", true);
						System.out.println("Add Course 1 ... ...");
					}
					else {
						System.out.println("Add Course 2 ... ...");
						if(flag.flagByte == 0)
							reason = "该课程已存在";
						jsout.put("return", false);
						jsout.put("reason", reason);
					}
					out.println(jsout.toString());
					break;
				case 10://删除课程, 只有在选课人数为0时才能删除
					 
					result = dbo.deleteCourse(Long.parseLong((String) jsin.get("cID")), flag);
					jsout = new JSONObject();
					if(result == true)
						jsout.put("return", true);
					else {
						if(flag.flagByte == 0)
							reason = "该课程不存在";
						else if(flag.flagByte == 1)
							reason = "该课程选课人数不为0";
						jsout.put("return", false);
						jsout.put("reason", reason);
					}
					out.println(jsout.toString());
					break;
				case 11:
					op = (int) jsin.get("op");
					if(op == 2) {
						result = dbo.setCourseName(Long.parseLong((String) jsin.get("cID")),
								(String) jsin.get("change"),
								flag);
					}
					else if(op == 1) {
						result = dbo.setCourseCapacity(Long.parseLong((String) jsin.get("cID")), 
								(int) jsin.get("change"),
								flag);
					}
					else{
						jsout = new JSONObject();
						jsout.put("return", false);
						jsout.put("reason", "Invalid operation number!");
						out.println(jsout.toString());
						break;
					}
					
					jsout = new JSONObject();
					if(result == true)
						jsout.put("return", true);
					else {
						if(flag.flagByte == 0)
							reason = "该课程不存在";
						else if(flag.flagByte == 1)
							reason = "课程容量低于已报课程学生数";
						jsout.put("return", false);
						jsout.put("reason", reason);
					}
					out.println(jsout.toString());
					break;
				case 12:
					TreeMap<Long, CourseData> courseMap = dbo.getCourseMap();
					if(courseMap == null) {
						out.println("1");
						break;
					}
					int len = courseMap.size();
					JSONObject[] jarray = new JSONObject[len];//JSONArray
					int n = 0;//计数
					JSONObject js = null;
					CourseData courseData = null;
					java.util.Map.Entry<Long, CourseData> entry = null;
					Iterator<java.util.Map.Entry<Long, CourseData>> it = courseMap.entrySet().iterator();
					while(n<len) {
						entry = it.next();
						courseData = entry.getValue();
						js = new JSONObject();
						js.put("cID", ((Long)courseData.cID).toString());
						js.put("cName", courseData.cName);
						js.put("cCapacity", courseData.cCapacity);
						js.put("cSelected", courseData.cSelected);
						js.put("cRemain", courseData.cRemain);
						jarray[n] = js;
						n++;
					}
					jsout = new JSONObject();
					jsout.put("courseList", jarray);
					System.out.println(jsout.toString());
					out.println(jsout.toString());
					break;
				default:
					System.out.println("错误的指令, 指令数字应该在1~11间, 请重新输入!");
					continue;
				}
				//TODO 测试用
				System.out.println(Thread.currentThread().getName()+" done");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
