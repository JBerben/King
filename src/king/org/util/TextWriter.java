package king.org.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

public class TextWriter {

	/*
	 * Creates a text file and writes data to it. Function is under heavy work
	 * and is subject to change very frequently
	 */
	public static void Write(String fileName) {

		Constants.FindFrequencies(Constants.cleanJar);
		
		BufferedWriter writer = null;
		try {
			File file = new File(fileName);
			java.util.Iterator<Entry<String, ClassNode>> classIterator = Constants.sampleClasses.entrySet().iterator();
			java.util.Iterator<Entry<String, Integer>> fieldIterator = Constants.expectedFFreqs.entrySet().iterator();
			
			writer = new BufferedWriter(new FileWriter(file));
			
			while (classIterator.hasNext()) {
				ClassNode currentClass = classIterator.next().getValue();
				writer.write(currentClass.name + "\n");
				for (FieldNode fn : (List<FieldNode>) currentClass.fields) {
					System.out.println(fn.name);
					//System.out.println(Constants.expectedFFreqs.get(fn.name));
					writer.write(fn.name + ": " + Constants.expectedFFreqs.get(fn.name) + "\n");
				}
				writer.write("\n");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		finally {
			
			try {
				writer.close();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
