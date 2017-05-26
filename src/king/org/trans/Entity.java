package king.org.trans;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import king.org.Updater;
import king.org.util.search.InstructionSearcher;

public class Entity extends Transform {

	public Entity(Updater i) {
		super(i);
	}

	@Override
	public ClassNode identify(Collection<ClassNode> classNodes) {
		for (ClassNode node : classNodes) {
			if (node.superName.equals(instance.getClassName("Animable"))) {
				if (node.methods.size() <= 7) {
					
					// The number of correct methods we find. Needs to be 2
					short methodCount = 0;
					
					for (MethodNode mn : (List<MethodNode>) node.methods) {

						/*
						 * This method is actually the isVisible() method. Here
						 * we are checking to see if this is actually the
						 * isVisible() method within the Entity class
						 */
						if (mn.desc.equals("()Z")) {
							InstructionSearcher iSearcher = new InstructionSearcher(mn);
							short instnCount = 0;
							int[] opCodes = { Opcodes.ICONST_0, Opcodes.IRETURN };
							for (int i = 0; i < opCodes.length; i++) {
								if (iSearcher.getCurrent().getOpcode() == opCodes[i]) {
									instnCount++;
								}
								iSearcher.getNext();
							}
							if (instnCount == 2) {
								methodCount++;
							}
						}

						/*
						 * Here we are checking for method446() using an opcode
						 * pattern check
						 */
						if (mn.desc.equals("()V") && !mn.name.equals("<init>")) {
							InstructionSearcher iSearcher = new InstructionSearcher(mn);
							short instnCount = 0;
							int[] opCodes = { Opcodes.ALOAD, Opcodes.ICONST_0, Opcodes.PUTFIELD, Opcodes.ALOAD,
									Opcodes.ICONST_0, Opcodes.PUTFIELD, Opcodes.RETURN };
							for (int i = 0; i < opCodes.length; i++) {
								if (iSearcher.getCurrent() != null) {
									if (iSearcher.getCurrent().getOpcode() == opCodes[i]) {
										instnCount++;
									}
									iSearcher.getNext();
								}
							}
							if (instnCount == 7) {
								methodCount++;
							}
						}
					}
					if (methodCount == 2) {
						Updater.rawClasses.put(node.name, node);
						return node;
					}
				}
			}
		}
		return null;
	}

	@Override
	public ClassNode manipulate(ClassNode classNode) {
		
		/*
		 * HOOKING FIELDS
		 */
		
		// Set-up
		HashMap<FieldNode, Integer> candidates = new HashMap<FieldNode, Integer>();
		
		// InteractingEntity
		for (FieldNode fn : (List<FieldNode>) classNode.fields) {
			if (fn.desc.equals("I") && fn.access == Opcodes.ACC_PUBLIC) {
				candidates.put(fn, 0);
			}
			
			/* Lets try and deduct our perfect candidate
			 * for the InteractingEntity field
			 */
			for (ClassNode c : Updater.classes.values()) {
				for (FieldNode f : (List<FieldNode>) c.fields) {
					if (candidates.containsKey(f)) {
						candidates.put(f, candidates.get(f) + 1);
					}
				}
			}
			/* InteractingEntity occurs ~57 times
			 * This is our final check
			 */
			for (FieldNode f : candidates.keySet()) {
				if (candidates.get(f).intValue() == 57) {
					Updater.oldFieldNames.put(f.name, "getInteractingEntity");
					//System.out.println(f.name);
					f.name = "getInteractingEntity";
				}
			}
		}
		
 		
		return classNode;
	}

}
