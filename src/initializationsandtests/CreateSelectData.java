package initializationsandtests;

import java.io.*;
import java.util.ArrayList;
import java.util.TreeMap;

import data.*;

/**
 * 产生选课信息
 * @author zhaowenbo
 *
 */

public class CreateSelectData {
	public static void main(String[] args) throws IOException {
		String rootString = "./SelectData/";
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
		TreeMap<Long, ArrayList<SelectData>> map = new TreeMap<Long, ArrayList<SelectData>>();
		for(int j=0; j<100; j++) {
			map.put(count, new ArrayList<SelectData>());
			count++;
			System.out.println(count);
		}
		oos.writeObject(map);
		oos.flush();
		oos.close();
		
		for(long i=1; i< 10000; i++) {
			file =  new File(rootString + i*100);
			file.createNewFile();
			oos = new ObjectOutputStream(
							new BufferedOutputStream(
									new DataOutputStream( 
											new FileOutputStream(file))));
			TreeMap<Long, ArrayList<SelectData>> map2 = new TreeMap<Long, ArrayList<SelectData>>();
			for(int j=0; j<100; j++) {
				map2.put(count, new ArrayList<SelectData>());
				count++;
				System.out.println(count);
			}
			oos.writeObject(map2);
			oos.flush();
			oos.close();
		}
	}
}
