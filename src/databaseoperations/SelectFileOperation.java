package databaseoperations;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import data.SelectData;

/**
 * 操作Select数据文件的工具类
 * @author zhaowenbo
 *
 */

public class SelectFileOperation extends BasicFileOperations{
	
	public SelectFileOperation() throws ClassNotFoundException, FileNotFoundException, IOException {
		super();
		System.out.println("SelectFileOperation 工具类构造完成");
	}

	static File selectData = null;

	public void mapWriteBack(long sID, TreeMap<Long, ArrayList<SelectData>> m) 
			throws IOException, ClassNotFoundException {
		TreeMap<Long, ArrayList<SelectData>> map = m;
			selectData = new File(findSelectFile(sID));	//找到存储 student ID == sID 的学生选课信息的文件
			ObjectOutputStream selectDataOutputStream =	//更新selectData的输出流
					new ObjectOutputStream(
							new BufferedOutputStream(
									new FileOutputStream(selectData)));
			selectDataOutputStream.writeObject(map);
			selectDataOutputStream.flush();
			selectDataOutputStream.close();
	}

	public TreeMap<Long, ArrayList<SelectData>> getTreeMap(long sID) throws IOException, ClassNotFoundException {
		try{
			selectData = new File(findSelectFile(sID));	//找到存储 student ID == sID 的学生选课信息的文件
		}catch(NullPointerException e) {
			System.out.println("该学号不合法");
			return null;
		}
		if(selectData==null) {
			return null;
		}
		ObjectInputStream selectDataInputStream = 	//获得selectDataMap的输入流
				new ObjectInputStream(
						new BufferedInputStream(
								new FileInputStream(selectData)));
		TreeMap<Long, ArrayList<SelectData>> map = 
				(TreeMap<Long, ArrayList<SelectData>>)selectDataInputStream.readObject();
		selectDataInputStream.close();
		return map;
	}
}
