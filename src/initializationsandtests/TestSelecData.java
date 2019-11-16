package initializationsandtests;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import data.*;

public class TestSelecData {
	ArrayList<SelectData> arrayList = null;
	static int count = 0;
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		File file = new File("SelectData/100");
		ObjectInputStream ois = 
				new ObjectInputStream(
						new BufferedInputStream(
								new DataInputStream(
										new FileInputStream(file))));
		TreeMap<Long, ArrayList<SelectData>> map = (TreeMap<Long, ArrayList<SelectData>>) ois.readObject();
		for (Long key : map.keySet()) {
			
			ArrayList<SelectData> s = map.get(key);
			Iterator<SelectData> ite = s.iterator();
			while(ite.hasNext()) {
				SelectData sd = ite.next();
				System.out.print(sd.cID + "、");
			}
			System.out.print("\n");
			//System.out.println(++count);
		}
	}
}
