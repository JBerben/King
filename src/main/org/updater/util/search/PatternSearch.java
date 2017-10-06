package src.main.org.updater.util.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import src.main.org.updater.trans.Classes;
import src.main.org.updater.util.Maths;


/* A small side class that contains functions to search for
 * particular fields or methods.
 */

public class PatternSearch {

	/*
	 * Searches for a particular field name within a particular class. Only
	 * useful once you've identified names, this function is mainly used to
	 * reduce code size and improve cleanliness
	 */
	public static FieldNode FindField(String fieldName, ClassNode c) {
		for (FieldNode fn : (List<FieldNode>) c.fields) {
			if (fn.name.equals(fieldName)) {
				return fn;
			}
		}
		return null;
	}

	/*
	 * Searches for a particular method name within a particular class. Only
	 * useful once you've identified names, this function is mainly used to
	 * reduce code size and improve cleanliness
	 */
	public static MethodNode FindMethod(String fieldName, ClassNode c) {
		for (MethodNode mn : (List<MethodNode>) c.methods) {
			if (mn.name.equals(fieldName)) {
				return mn;
			}
		}
		return null;
	}

	/*
	 * We are looking at the bytecode of a method. We need to find out whether
	 * they are the same method even though they most likely don't have
	 * identical opcode instructions
	 * 
	 * Analysis techniques so far: Looks at first n instructions of both
	 * methods, since this is usually where the bytecode of equal methods looks
	 * the same
	 * 
	 * TODO: Add more strategies
	 */
	public static float analyzeBytecode(int start, int end, MethodNode cm, MethodNode dm) {

		// Analyze first 15 instructions
		int length = 15;
		float similarity = 0f;
		InsnList cList = cm.instructions;
		InsnList dList = dm.instructions;
		if (Math.min(cList.size(), dList.size()) < length)
			length = Math.min(cList.size(), dList.size());
		for (int i = 0; i < length; i++) {
			if (cList.get(i).getOpcode() == dList.get(i).getOpcode()) {
				similarity++;
			} else if (i + 1 < dList.size()) {
				if (cList.get(i).getOpcode() == dList.get(i + 1).getOpcode()) {
					similarity++;
				}
			}
		}
		return similarity = (float) (similarity / length);
	}
	
	// Goes hand in hand with analyzeByteCode function
	public static float analyzeSections(MethodNode cm, MethodNode dm) {
		float allSim = 0f;
		InsnList cList = cm.instructions;
		InsnList dList = dm.instructions;
		int minLength = Math.min(cList.size(), dList.size());
		int skipLength = (int) (minLength * .1);
		int analyzeLength = (int) (minLength * .05) + 1;
		int iterations = minLength / (skipLength + analyzeLength);
		
		for (int i = 0; i < iterations; i++) {
			allSim += analyzeBytecode(skipLength * i, analyzeLength * (i + 1), cm, dm);
		}
		
		return allSim / iterations;
	}
	
	// A fail-safe when above functions don't guess correctly
	
	public static float analyzeMethodFields(MethodNode cm, MethodNode dm) {
		float sim = 0f;
		InsnList cList = cm.instructions;
		InsnList dList = dm.instructions;
		
		ArrayList<FieldInsnNode> c = new ArrayList<FieldInsnNode>();
		ArrayList<FieldInsnNode> d = new ArrayList<FieldInsnNode>();
		
		Iterator cIt = cList.iterator();
		Iterator dIt = dList.iterator();
		
		while (cIt.hasNext()) {
			Object n = cIt.next();
			if (n.getClass().equals(FieldInsnNode.class)) {
				
				c.add((FieldInsnNode)n);
			}
		}
		
		while (dIt.hasNext()) {
			Object n = dIt.next();
			if (n.getClass().equals(FieldInsnNode.class)) {
				d.add((FieldInsnNode)n);
			}
		}
		
		//System.out.println("MODEL: " + cm.name + " --> " + dm.name + " | " + c.get(0).desc + " " + d.get(0).desc);
		
		int minSize = Math.min(c.size(), d.size());
		if (Maths.getRatio(c.size(), d.size()) > 0.5) {

			for (int i = 0; i < minSize; i++) {
				if (c.size() >= d.size()) {
					if (fieldlistContains(c, d.get(i).desc)) {
						sim++;
					}
				}
				
				if (c.size() < d.size()) {
					if (fieldlistContains(d, c.get(i).desc)) {
						sim++;
					}
				}
			}
		} 
		if (sim > 0) {
			return sim / Math.max(c.size(), d.size());
		} else {
			return 0;
		}
	}
	
	public static float methodUsageSimilarity(MethodNode cm, MethodNode dm) {
		
		
		return 0f;
	}
	
	public static float analyzeMethodBytecode(MethodNode cm, MethodNode dm) {
		float sim = analyzeMethodFields(cm, dm) + analyzeSections(cm, dm);
		if (sim > 0) {
			return sim / 2.0f;
		} else {
			return 0;
		}
	}
	
	private static boolean fieldlistContains(ArrayList<FieldInsnNode> list, String desc) {
		for (int i = 0; i < list.size(); i++) {
			if (Classes.parseObjectDesc(list.get(i).desc).equals(Classes.parseObjectDesc(desc))) {
				return true;
			}
		}
		return false;
	}

}
