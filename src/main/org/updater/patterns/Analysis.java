package src.main.org.updater.patterns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import src.main.org.updater.obj.GameField;
import src.main.org.updater.obj.GameMethod;
import src.main.org.updater.util.Maths;
import src.main.org.updater.util.search.InstructionSearcher;


public class Analysis {

	public static HashMap<MethodNode, FingerPrint> fingerprints = new HashMap<MethodNode, FingerPrint>();
	private static ArrayList<Integer> ANCHORS = new ArrayList<Integer>(
			Arrays.asList(Opcodes.NEW, Opcodes.GETSTATIC, Opcodes.INVOKESPECIAL, Opcodes.GETFIELD, Opcodes.PUTFIELD,
					Opcodes.INVOKEVIRTUAL, Opcodes.PUTSTATIC, Opcodes.IF_ICMPGE, Opcodes.IF_ICMPLE));
	
	public static HashMap<GameField, GameField> findFields(GameMethod c, GameMethod d) {
		//<String(clean field name), String(dirty field name)>
		HashMap<GameField, GameField> matches = new HashMap<GameField, GameField>();
		GameField match = new GameField();
		for (GameField cf : c.gameFields.values()) {
			float sim = 0f;
			for (GameField df : d.gameFields.values()) {
				if (cf.getDesc().equals(df.getDesc())) {
					if (Maths.getRatio(cf.timesUsed, df.timesUsed) > sim) {
						sim = Maths.getRatio(cf.timesUsed, df.timesUsed);
						match = df;
					} 
				}
			}
			matches.put(cf, match);
		}
		return matches;
	}
	
	public static HashMap<String, GameField> stripInsnList(MethodNode mn) {
		//for (MethodNode mn : (List<MethodNode>)cn.methods) {
		//	
		//}
		InstructionSearcher is = new InstructionSearcher(mn);
		HashMap<String, GameField> fields = new HashMap<String, GameField>();

		while (is.getCurrent() != null) {
			
			GameField f = new GameField();
			
			switch (is.getCurrent().getType()) {
			case AbstractInsnNode.INT_INSN:
				final IntInsnNode iin = (IntInsnNode) is.getCurrent();
				break;

			case AbstractInsnNode.LDC_INSN:
				final LdcInsnNode lin = (LdcInsnNode) is.getCurrent();
				break;

			case AbstractInsnNode.VAR_INSN:
				final VarInsnNode vin = (VarInsnNode) is.getCurrent();
				break;

			case AbstractInsnNode.IINC_INSN:
				final IincInsnNode iiin = (IincInsnNode) is.getCurrent();
				break;

			case AbstractInsnNode.FIELD_INSN:
				final FieldInsnNode fin = (FieldInsnNode) is.getCurrent();
				f.setName(fin.name);
				f.setDesc(fin.desc);
				f.setType(fin.getType());
				
				if (fields.containsKey(f.getName())) {
					fields.get(f.getName()).timesUsed++;
				} else {
					f.timesUsed++;
					fields.put(f.getName(), f);
				}
				break;

			case AbstractInsnNode.METHOD_INSN:
				final MethodInsnNode min = (MethodInsnNode) is.getCurrent();
				break;

			case AbstractInsnNode.TYPE_INSN:
				final TypeInsnNode tin = (TypeInsnNode) is.getCurrent();
				break;

			case AbstractInsnNode.MULTIANEWARRAY_INSN:
				final MultiANewArrayInsnNode manain = (MultiANewArrayInsnNode) is.getCurrent();
				break;
			}
			is.getNext();
		}
		return fields;
	}

	// Finds a fingerprint within a method, and adds it to a list
	public static void findFingerprints(GameMethod gm) {
		InstructionSearcher is = new InstructionSearcher(gm.getMethodNode());
		int start;
		int end;

		while (is.getCurrent() != null) {
			if (ANCHORS.contains(is.getCurrent().getOpcode())) {
				start = is.getIndex();
				end = findNext(gm.getMethodNode(), start, ANCHORS);

				FingerPrint f = new FingerPrint(gm.getMethodNode(), gm.getMethodNode().instructions);
				f.composeList(start, end);

				gm.fPrints.add(f);
			}
			is.getNext();
		}
	}

	/*
	 * Tries to match fingerprints of two methods together. Returns a similarity
	 * value based on how many fingerprints were matched
	 * 
	 * We are comparing the dirty fingerprints against the clean ones
	 * 
	 * Doesnt work :(
	 */
	public static float matchFingerprints(GameMethod clean, GameMethod dirty) {
		InstructionSearcher cleanIs = new InstructionSearcher(clean.getMethodNode());
		InstructionSearcher dirtyIs = new InstructionSearcher(dirty.getMethodNode());
		ArrayList<Integer> checked = new ArrayList<Integer>();
		float fSim = 0f;
		float sim = 0f;
		int fmatches = 0;
		int matches = 0;

		// Check to stop the Updater from doing too much work when it's not
		// needed
		if (Maths.getRatio(clean.fPrints.size(), dirty.fPrints.size()) > 0.4) {
			for (int i = 0; i < clean.fPrints.size(); i++) {
				if (clean.fPrints.get(i) != null) {
					for (int j = 0; j < dirty.fPrints.size(); j++) {

						// Found an anchor
						if (dirty.fPrints.get(j).getAnchor().getOpcode() == clean.fPrints.get(i).getAnchor().getOpcode()
								&& !checked.contains(dirty.fPrints.get(j).index)) {

							// Compare the fingerprints
							AbstractInsnNode cin = clean.fPrints.get(i).getInstructionList().getFirst();
							AbstractInsnNode din = dirty.fPrints.get(j).getInstructionList().getFirst();
							for (int k = 1; k < Math.min(clean.fPrints.get(i).getSize(),
									dirty.fPrints.get(j).getSize()); k++) {
								if (cin.getOpcode() == din.getOpcode()) {
									fmatches++;
								}
								cin = clean.fPrints.get(i).getInstructionList().get(k);
								din = dirty.fPrints.get(j).getInstructionList().get(k);
							}
							if (fSim < (float) matches
									/ Math.min(clean.fPrints.get(i).getSize(), dirty.fPrints.get(j).getSize())) {
								fSim = (float) matches
										/ Math.min(clean.fPrints.get(i).getSize(), dirty.fPrints.get(j).getSize());
							}
						}
						if (fSim > 0.8) {
							matches++;
						}
					}
				}
				checked.add(clean.fPrints.get(i).index);
			}
			sim = (float) matches / (float) clean.fPrints.size();
		}
		return sim;
	}

	/*
	 * Returns the next anchor in a sequence of instructions
	 */
	private static int findNext(MethodNode m, int startPos, ArrayList<Integer> OPCODES) {
		InstructionSearcher is = new InstructionSearcher(m);

		is.setIndex(startPos);

		while (is.getNext() != null) {
			if (OPCODES.contains(is.getCurrent().getOpcode())) {
				return is.getIndex();
			}
		}
		return -1;
	}
	
	public static MethodNode replaceMethodNodeName(MethodNode m, HashMap<String, String> r) {
		if (r.containsKey(m.name)) {
			m.name = r.get(m.name);
		}
		return m;
	}
	
	public static ClassNode renameClassNode(ClassNode c, HashMap<String, String> r) {
		if (r.containsKey(c.name)) {
			c.name = r.get(c.name);
		}
		return c;
	}

}
