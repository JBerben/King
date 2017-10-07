package src.main.org.updater.trans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import src.main.org.bot.client.ui.UI;
import src.main.org.updater.Updater;
import src.main.org.updater.UpdaterManager;
import src.main.org.updater.obj.GameField;
import src.main.org.updater.obj.GameMethod;
import src.main.org.updater.util.Maths;
import src.main.org.updater.util.search.InstructionSearcher;

/*
 * This is where all fields are analyzed and matched together.
 * This is the most important part of any analysis since fields
 * are the most used in bot clients.
 */
public class Fields extends Transform {

	public static float fieldProgress;
	private static int numFields;
	
	public Fields(Updater i) {
		super(i);
	}

	@Override
	public HashMap<String, ClassNode> identify(Collection<ClassNode> classNodes) {

		numFields = classNodes.size();
		UpdaterManager.status = "Finding Fields...";
		
		/*
		 * These HashMaps were explained in Updater.class
		 */
		HashMap<String, ClassNode> cleanClasses = Updater.cleanClasses;
		HashMap<String, ClassNode> dirtyClasses = Updater.processedClasses;
		HashMap<GameField, GameField> matchedFields = new HashMap<GameField, GameField>();

		/*
		 * Cycles through all classes and matches fields.
		 */
		for (ClassNode c : cleanClasses.values()) {
			for (ClassNode d : dirtyClasses.values()) {
				if (c.name.equals(d.name)) {
					matchedFields.putAll(matchFields(c, d));
				}
			}
		}

		/*
		 * Updates our HashMaps
		 */
		Updater.matchedFields = matchedFields;
		return Updater.processedClasses;
	}

	// TODO: Add injector functionality
	@Override
	public HashMap<String, ClassNode> manipulate(Collection<ClassNode> classNodes) {
		return Updater.processedClasses;
	}

	/*
	 * Compares two GameFields to find the similarity rating between the two.
	 */
	public static float compareGameFields(GameField c, GameField d) {

		/*
		 * Makes sure both fields are used in the same number of methods,
		 * increasing search speed.
		 */
		if (c.methodsUsedIn.keySet().size() == d.methodsUsedIn.keySet().size()) {

			// Global similarity
			float gSim = 0f;
			Map<String, Integer> clean = c.methodsUsedIn;
			Map<String, Integer> dirty = d.methodsUsedIn;

			for (Map.Entry<String, Integer> cEntry : clean.entrySet()) {

				float sim = 0f;

				for (Map.Entry<String, Integer> dEntry : dirty.entrySet()) {
					if (cEntry.getKey().equals(dEntry.getKey())) {
						sim += Maths.getRatio(cEntry.getValue(), dEntry.getValue());
					}
				}
				gSim += sim;
			}
			return gSim / (float) c.methodsUsedIn.size();
		}
		// Fields aren't similar enough
		return 0f;
	}

	/*
	 * Finds all local fields of a class and how many times they occur in each
	 * of its methods'.
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, GameField> analyseClass(ClassNode cn) {
		HashMap<String, GameField> fields = new HashMap<String, GameField>();
		ArrayList<String> permittedFields = new ArrayList<String>();

		// Makes sure we're only looking at local fields
		for (FieldNode fn : (List<FieldNode>) cn.fields) {
			if (!permittedFields.contains(fn.name)) {
				permittedFields.add(fn.name);
			}
		}

		/*
		 * Look through byte-code instructions and extract the field names from
		 * the Integer bytecode instructions.
		 * 
		 * There are many unused cases, but in the future they will be worked
		 * upon to provide a higher level of accuracy in field analysis.
		 */
		for (MethodNode mn : (List<MethodNode>) cn.methods) {
			GameMethod gm = new GameMethod(mn);
			InstructionSearcher is = new InstructionSearcher(mn);

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
					if (permittedFields.contains(fin.name)) {
						f.setName(fin.name);
						f.setDesc(fin.desc);
						f.setType(fin.getType());
						f.timesUsed++;

						if (fields.containsKey(f.getName())) {
							fields.get(f.getName()).timesUsed++;
							fields.get(f.getName()).methodsUsedIn.put(gm.getName(), fields.get(f.getName()).timesUsed);
							if (!fields.get(f.getName()).methodsUsedIn.containsKey(gm.getName())) {
								fields.get(f.getName()).methodsUsedIn.put(gm.getName(),
										fields.get(f.getName()).timesUsed);
							}
						} else {
							fields.put(f.getName(), f);
							fields.get(f.getName()).methodsUsedIn.put(gm.getName(), f.timesUsed);
						}
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
		}
		return fields;
	}

	/*
	 * Finds the best match for all fields within a dirty and a clean class
	 */
	public static HashMap<GameField, GameField> matchFields(ClassNode c, ClassNode d) {
		GameField match = new GameField();

		HashMap<GameField, GameField> matches = new HashMap<GameField, GameField>();
		HashMap<String, GameField> clean = new HashMap<String, GameField>();
		HashMap<String, GameField> dirty = new HashMap<String, GameField>();

		clean = analyseClass(c);
		dirty = analyseClass(d);

		for (GameField cgf : clean.values()) {
			
			UpdaterManager.status = "Hooking Field: " + cgf.getName();
			
			cgf.setAcessor(c);
			float sim = 0f;
			float comp = 0f;

			for (GameField dgf : dirty.values()) {
				dgf.setAcessor(d);

				if (cgf.getDesc().equals(dgf.getDesc())) {
					if (Maths.getRatio(cgf.methodsUsedIn.size(), dgf.methodsUsedIn.size()) > 0.2) {
						comp = compareGameFields(cgf, dgf);
						if (comp > sim && dgf.methodsUsedIn.keySet().size() == cgf.methodsUsedIn.keySet().size()) {
							sim = comp;
							match = dgf;
						}
					}
				}
			}
			match.setAcessor(d);
			System.out.println("Clean Field: " + cgf.getName() + " |" + cgf.timesUsed + "| " + cgf.methodsUsedIn
					+ " -> " + match.getName() + " |" + match.timesUsed + "|" + match.methodsUsedIn);
			matches.put(cgf, match);
			UpdaterManager.progress += (100 / numFields) * 0.4f;
			UI.sl.UpdateProgress(UpdaterManager.progress, UpdaterManager.status);
		}
		return matches;
	}

	private FieldNode findField(ClassNode c, String name) {
		for (FieldNode f : (List<FieldNode>) c.fields) {
			if (f.name.contains(name)) {
				return f;
			}
		}
		return null;
	}

}
