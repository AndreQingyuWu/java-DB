package initializationsandtests;

import java.io.*;
import java.util.TreeMap;

import javax.imageio.stream.FileImageInputStream;

public class TestIndex {
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		File file = new File("SelectIndex");
		ObjectInputStream ois = 
				new ObjectInputStream(
						new BufferedInputStream(
								new DataInputStream(
										new FileInputStream(file))));
		TreeMap<Long, String> map = (TreeMap<Long, String>) ois.readObject();
		for (Long key : map.keySet()) {
			String string = map.get(key);
			System.out.println("" + key + ": " + string);
		}
	}
}
