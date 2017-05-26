package king.org.trans;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import king.org.Updater;
import king.org.util.search.InstructionSearcher;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

public class NPC extends Transform {
	public NPC(Updater i) {
		super(i);
	}

	@Override
	public ClassNode identify(Collection<ClassNode> classNodes) {
		for (ClassNode node : classNodes) {
			int a = 0;
			if (node.superName.equals(instance.getClassName("Entity"))) {
				if (node.fields.size() <= 3) {
					for (MethodNode mn : (List<MethodNode>) node.methods) {
						if (mn.desc.equals("()Z")) {

							short instnCount = 0, totalInstns = 4;
							int[] opCodes = { Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.IFNULL, Opcodes.ICONST_1 };
							InstructionSearcher iSearcher = new InstructionSearcher(mn);

							while (iSearcher.getCurrent() != null && instnCount < totalInstns) {
								if (iSearcher.getCurrent().getOpcode() == opCodes[instnCount]) {
									instnCount++;
								}
								iSearcher.getNext();
							}
							if (instnCount == 4) {
								/*
								 * Before returning this node, we might as well
								 * fetch the Model class from here as well since
								 * it is the one and only field in this class
								 */
								Updater.rawClasses.put(node.name, node);
								return node;
							}
						}
					}
				}
			}
		}
		return null;

	}

	@Override
	public ClassNode manipulate(ClassNode classNode) {

		if (classNode != null) {
			// Go through fields and transform apropiately
			ListIterator<FieldNode> fnIt = classNode.fields.listIterator();
			while (fnIt.hasNext()) {
				FieldNode fn = fnIt.next();

				// desc field within NPC class
				if (fn.access == Opcodes.ACC_PUBLIC && !fn.desc.equals("I")) {
					fn.name = "entityDef";
				}
			}
			return classNode;
		}
		return null;
	}
}