package initializationsandtests;

import java.io.*;
import java.util.TreeMap;

/**
 * 初始化选课信息索引表
 * @author zhaowenbo
 *
 */

public class CreateSelectIndex {
	static long count = 0;
	public static void main(String[] args) throws IOException {
		File indexFile = new File("SelectIndex");
		if(indexFile==null)
			indexFile.createNewFile();
		ObjectOutputStream oos =
				new ObjectOutputStream(
						new BufferedOutputStream(
								new DataOutputStream(
										new FileOutputStream(indexFile))));
		TreeMap<Long, String> indexMap = new TreeMap<Long, String>();
		String root = "SelectData/";
		
		while(count<10000) {
			indexMap.put(count, root+count+ "00");
			System.out.println(count+"00");
			count++;
		}
		
		oos.writeObject(indexMap);
		oos.flush();
		oos.close();
	}
}
