package src.main.org.updater.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import src.main.org.updater.Updater;
import src.main.org.updater.obj.GameClass;
import src.main.org.updater.obj.GameField;
import src.main.org.updater.obj.GameMethod;
import src.main.org.updater.patterns.Analysis;
import src.main.org.updater.patterns.BytecodeAnalysis;
import src.main.org.updater.trans.Fields;

/*
 * Class/Object that is used to test analyzing
 * methods, which is needed due to the scale of the project
 * and how difficult it is to test singularities amongst
 * 10+ other analyzing strategies
 */
public class Debug {

	private HashMap<String, ClassNode> cClasses;
	private HashMap<String, ClassNode> dClasses;

	public Debug() {
		cClasses = Updater.cleanClasses;
		dClasses = Updater.dirtyClasses;
	}

	/*
	 * FUNCTIONS
	 */

	// Looking at the Model class
	public void debugFunctions() {
		boolean testBC = true;
		boolean testFP = false;
		boolean testF = true;

		// Gets the Model classes
		ClassNode cn = findClass(cClasses, "client");
		ClassNode dn = findClass(dClasses, "rs/Client");
		
		System.out.println(cn.name + " " + dn.name);
		
		HashMap<String, String> nameTable = new HashMap<String, String>();
		GameMethod dMatch = new GameMethod();

		if (testBC) {

			GameClass c = new GameClass(cn);
			GameClass d = new GameClass(dn);
			GameMethod match = new GameMethod();
			for (MethodNode cm : (List<MethodNode>) cn.methods) {
				GameMethod clean = new GameMethod(cm);
				c.gameMethods.put(clean.getName(), clean);
				match.similarity = 0;
				for (MethodNode dm : (List<MethodNode>) dn.methods) {
					float sim = BytecodeAnalysis.analyze(cm, dm);
					if (sim > match.similarity) {
						match.setMethodNode(dm);
						match.gameFields = Analysis.stripInsnList(match.getMethodNode());
						match.similarity = sim;
						d.gameMethods.put(match.getName(), match);
					}
				}

				nameTable.put(match.getMethodNode().name, cm.name);
				// match.getMethodNode().name = cm.name;
				System.out.println(
						"MATCHING: " + cm.name + " --> " + match.getMethodNode().name + " | " + match.similarity);
				match.getMethodNode().name = cm.name;
				//System.out.println("MATCHED: " + cm.name + " " + match.getMethodNode().name);
			}
			for (MethodNode m : (List<MethodNode>) dn.methods) {
				m = Analysis.replaceMethodNodeName(m, nameTable);
			}
		}

		if (testF) {
			HashMap<GameField, GameField> matches = new HashMap<GameField, GameField>();
			matches = Fields.matchFields(cn, dn);

			// Print out matched fields
			Iterator itr = matches.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry pair = (Map.Entry) itr.next();
				GameField cleanField = (GameField) pair.getKey();
				GameField dirtyField = (GameField) pair.getValue();
				System.out.println("Clean Field: " + cleanField.getName() + " |" + cleanField.timesUsed + "| " + cleanField.methodsUsedIn + " -> "
						+ dirtyField.getName() + " |" + dirtyField.timesUsed + "| " + dirtyField.methodsUsedIn);
				itr.remove();
			}
		}

		if (testFP) {
			int count = 0;
			for (MethodNode cm : (List<MethodNode>) cn.methods) {
				GameMethod cMatch = new GameMethod();
				cMatch.setMethodNode(cm);
				Analysis.findFingerprints(cMatch);
				for (MethodNode dm : (List<MethodNode>) dn.methods) {
					dMatch = new GameMethod();
					dMatch.setMethodNode(dm);
					Analysis.findFingerprints(dMatch);
					Analysis.matchFingerprints(cMatch, dMatch);
				}
				System.out.println("MATCHING: " + cMatch.fPrints.size() + " --> " + dMatch.fPrints.size());
			}
		}

	}

	// Find class by name
	public ClassNode findClass(HashMap<String, ClassNode> map, String name) {
		for (ClassNode cn : map.values()) {
			if (cn.name.contains(name) && !cn.name.contains("$")) {
				return cn;
			}
		}
		System.out.println("Couldn't find specified class! " + "(" + name + ")");
		return null;
	}

}
