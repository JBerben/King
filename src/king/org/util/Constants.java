package king.org.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarFile;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import king.org.Updater;

public class Constants {

	/* Holds values for how many individual classes each field/method appears in
	 * Integer: frequency
	 * String:  field/method name
	 */
	public static HashMap<String, Integer> expectedFFreqs = new HashMap<String, Integer>();
	public static HashMap<String, Integer> fFreqs = new HashMap<String, Integer>();
	
	public static HashMap<String, Integer> expectedMFreqs = new HashMap<String, Integer>();
	public static HashMap<String, Integer> mFreqs = new HashMap<String, Integer>();	

	public static HashMap<String, ClassNode> sampleClasses = new HashMap<String, ClassNode>();
	
	public static String cleanJar = "C:\\Users\\JBerben\\Documents\\Programming\\RSPS\\ReflectionClient\\KingUpdater\\src\\client.jar";
	
	
	/* FindFrequencies uses a clean de-obbed/refactored client in order to establish
	 * the expected frequencies for fields and methods.
	 */
	public static void FindFrequencies(String url) {
		try {
			JarFile jar = new JarFile(url);
			
			// Unpack classes
			sampleClasses = new JarStream().getRSPSContents(jar);
			PackFreqs();
			
			for (ClassNode cn : sampleClasses.values()) {
				for (ClassNode c : Updater.classes.values()) {
					for (FieldNode f : (List<FieldNode>) c.fields) {
						if (expectedFFreqs.containsKey(f.name)) {
							expectedFFreqs.put(f.name, expectedFFreqs.get(f.name) + 1);
							//System.out.println(expectedFFreqs.get(f.name));
						}
					}
				}
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/* Packages all of the field names into the Hashmap
	 */
	private static void PackFreqs() {
		for (ClassNode cn : Updater.classes.values()) {
			for (FieldNode fn : (List<FieldNode>) cn.fields) {
				if (!expectedFFreqs.containsKey(fn.name)) {
					expectedFFreqs.put(fn.name, 0);
				}
			}
		}
	}
	
}
