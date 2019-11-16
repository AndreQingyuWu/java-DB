package initializationsandtests;

import java.lang.Math;
import java.util.Random;
import java.util.TreeMap;

import data.StudentData;
import java.io.*;

/**
 * 产生100万条学生信息
 * @author zhaowenbo
 *
 */

public class CreateStudentData {
	static boolean gender = false;
	static Random random = new Random();
	
	public static String createName() {
		String[] firstNameArray = { "李", "王", "张", "刘", "陈", "杨", "赵", "黄", "周", "吴", "徐", "孙", "胡", "朱", "高", "欧阳",
	                "太史", "端木", "上官", "司马" };// 20个姓，其中5个复姓
	        String[] lastNameArray = { "伟", "勇", "军", "磊", "涛", "斌", "强", "鹏", "杰", "峰", "超", "波", "辉", "刚", "健", "明", "亮",
	                "俊", "飞", "凯", "浩", "华", "平", "鑫", "毅", "林", "洋", "宇", "敏", "宁", "建", "兵", "旭", "雷", "锋", "彬", "龙", "翔",
	                "阳", "剑", "静", "敏", "燕", "艳", "丽", "娟", "莉", "芳", "萍", "玲", "娜", "丹", "洁", "红", "颖", "琳", "霞", "婷", "慧",
	                "莹", "晶", "华", "倩", "英", "佳", "梅", "雪", "蕾", "琴", "璐", "伟", "云", "蓉", "青", "薇", "欣", "琼", "宁", "平",
	                "媛" };// 80个常用于名字的单字
	        int firstPos = (int) (20 * Math.random());
	        StringBuilder name = new StringBuilder(firstNameArray[firstPos]);
	        int lastLen = (int) (2 * Math.random()) + 1;
	        for (int i = 0; i < lastLen; i++) {
	            int lastPos = (int) (80 * Math.random());
	            name.append(lastNameArray[lastPos]);
	        }
	        return name.toString();
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		String rootString = "./StudentData/";
		File file = null;
		long count = 0;
		
		file =  new File(rootString + "000");
		if(file==null)
			file.createNewFile();
		ObjectOutputStream oos = 
				new ObjectOutputStream(
						new BufferedOutputStream(
								new DataOutputStream( 
										new FileOutputStream(file))));
		TreeMap<Long, StudentData> map = new TreeMap<Long, StudentData>();
		for(int j=0; j<100; j++) {
			map.put(count, new StudentData(count, (int)random.nextInt(100) + 1, gender, createName()));
			gender = random.nextBoolean();
			count++;
			System.out.println(count);
		}
		oos.writeObject(map);
		oos.flush();
		oos.close();
		
		for(long i=1; i< 10000; i++) {
			file =  new File(rootString + i*100);
			if(file==null)
				file.createNewFile();
			oos = new ObjectOutputStream(
							new BufferedOutputStream(
									new DataOutputStream( 
											new FileOutputStream(file))));
			TreeMap<Long, StudentData> map2 = new TreeMap<Long, StudentData>();
			for(int j=0; j<100; j++) {
				map2.put(count, new StudentData(count, (int)random.nextInt(100) + 1, gender, createName()));
				gender = random.nextBoolean();
				count++;
				System.out.println(count);
			}
			oos.writeObject(map2);
			oos.flush();
			oos.close();
		}
	}
}
