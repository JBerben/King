package src.main.org.updater.patterns;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import src.main.org.updater.trans.Classes;
import src.main.org.updater.util.search.InstructionSearcher;


public class BytecodeAnalysis {

	private static ArrayList<Integer> cleanInsns = new ArrayList<Integer>();
	private static ArrayList<Integer> dirtyInsns = new ArrayList<Integer>();
	private static boolean specialCase = false;

	/*
	 * Works really well, but finds patterns in a linear pattern.
	 * It needs to find these patterns, store them and then try and match them
	 * up in order to still be able to find methods that look like:
	 * 
	 * method39() {
	 * int f;
	 * int g;
	 * 
	 * method39() {
	 * int g;
	 * int f;
	 */
	public static float analyze(MethodNode cm, MethodNode dm) {

		/*
		 * Try catch counter?
		 * 
		 * for fields - remove any methods that you didnt match
		 * and then base all of your analysis off of those methods
		 */
		
		int anchors = 0;
		int matches = 0;
		float sim = 0f;

		// Anchors we are looking for
		ArrayList<Integer> opcodes = new ArrayList<Integer>();
		opcodes.addAll(Arrays.asList(Opcodes.NEW, Opcodes.ANEWARRAY, Opcodes.GETSTATIC, Opcodes.INVOKESPECIAL, Opcodes.GETFIELD, Opcodes.PUTFIELD,
				Opcodes.INVOKEVIRTUAL, Opcodes.PUTSTATIC, Opcodes.IF_ICMPGE, Opcodes.IF_ICMPLE, Opcodes.IF_ICMPLT, Opcodes.IF_ICMPGT));

		// Finder cf = new Finder(cn, cm);
		InstructionSearcher cs = new InstructionSearcher(cm);
		// Finder df = new Finder(dn, dm);
		InstructionSearcher ds = new InstructionSearcher(dm);

		while (getNextSet(cs, opcodes, true) != null && getNextSet(ds, opcodes, false) != null) {
			anchors++;

			Collections.sort(cleanInsns);
			Collections.sort(dirtyInsns);

			if (dirtyInsns.equals(cleanInsns)) {
				matches++;
			}

			cleanInsns.clear();
			dirtyInsns.clear();
			
		}
		
		if (specialCase) {
			getNextSet(ds, opcodes, false);
			anchors++;
			Collections.sort(cleanInsns);
			Collections.sort(dirtyInsns);
			if (dirtyInsns.equals(cleanInsns)) {
				//System.out.println(dm.name);
				matches++;
			}
			cleanInsns.clear();
			dirtyInsns.clear();
		}

		if (matches > 0) {
			sim = ((float) matches / (float) anchors);
		} else {
			sim = 0f;
		}
		if (sim != 0 && Classes.getDescEquality(cm.desc, dm.desc) == 1) {
			sim += 0.4;
		}
		return sim;
	}

	public static AbstractInsnNode getNextSet(InstructionSearcher is, ArrayList<Integer> opcodes, boolean isClean) {
		if (is.getCurrent() != null) {
			if (opcodes.contains(is.getCurrent().getOpcode())) {
				specialCase = true;
				while (is.getNext() != null && !opcodes.contains(is.getCurrent().getOpcode())) {
					if (isClean) {
						cleanInsns.add(is.getCurrent().getOpcode());
					} else {
						dirtyInsns.add(is.getCurrent().getOpcode());
					}
				}
				return is.getCurrent();
			}
			while (is.getNext() != null) {
				if (opcodes.contains(is.getCurrent().getOpcode())) {
					return is.getCurrent();
				}
				if (isClean) {
					cleanInsns.add(is.getCurrent().getOpcode());
				} else {
					dirtyInsns.add(is.getCurrent().getOpcode());
				}
			}
		}
		return null;
	}

}
