package king.org.trans;

import java.util.Collection;
import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import king.org.Updater;
import king.org.util.search.InstructionSearcher;

public class EntityDef extends Transform {

	public EntityDef(Updater i) {
		super(i);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ClassNode identify(Collection<ClassNode> classNodes) {

		String nodeName = null;
		FieldNode fn = instance.getField("NPC", "entityDef");

		for (ClassNode node : classNodes) {
			
			// Look for a class that extends lang/Object
			if (node.superName.equals("java/lang/Object") && node.fields.size() > 30) {
				for (MethodNode mn : (List<MethodNode>) node.methods) {
					//System.out.println("METHOD NAME: " + mn.name + " | CLASS NAME: " + node.name);
					if (mn.desc.equals("()V")) {
						short instnCount = 0;
						int[] opCodes = { Opcodes.ACONST_NULL, Opcodes.PUTSTATIC, Opcodes.ACONST_NULL,
								Opcodes.PUTSTATIC, Opcodes.ACONST_NULL, Opcodes.PUTSTATIC, Opcodes.ACONST_NULL,
								Opcodes.PUTSTATIC, Opcodes.RETURN };
						InstructionSearcher iSearcher = new InstructionSearcher(mn);
						while (iSearcher.getCurrent() != null) {
							if (iSearcher.getCurrent().getOpcode() == opCodes[instnCount]) {
								instnCount++;
							}
							iSearcher.getNext();
						}
						//System.out.println("ENTITYDEF METHOD: " + mn.name + " | insns: " + instnCount);
						if (instnCount == opCodes.length) {
							Updater.rawClasses.put(node.name, node);
							return node;
						}
					}
				}
			}

			/*
			// Tries to find Model class quickly
			if (fn != null) {
				nodeName = fn.desc.substring(1, fn.desc.length() - 1);
				System.out.println(nodeName);
				return instance.getUnprocessedNode(nodeName);
			}*/

		}

		return null;
	}

	@Override
	public ClassNode manipulate(ClassNode classNode) {
		return null;
	}

}
