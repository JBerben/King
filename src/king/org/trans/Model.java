package king.org.trans;

import java.util.Collection;
import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import king.org.Updater;
import king.org.util.search.InstructionSearcher;

public class Model extends Transform {

	public Model(Updater i) {
		super(i);
	}

	@Override
	public ClassNode identify(Collection<ClassNode> classNodes) {
		for (ClassNode node : classNodes) {
			if (node.superName.equals(instance.getClassName("Animable"))) {
				
				// Searches for the nullLoader() method in Model.class
				for (MethodNode mn : (List<MethodNode>) node.methods) {
					// if the method returns void and is public static
					if (mn.desc.equals("()V") && mn.access == Opcodes.ACC_STATIC + Opcodes.ACC_PUBLIC) {
						short instnCount = 0;
						int[] opCodes = { Opcodes.ACONST_NULL, Opcodes.PUTSTATIC, Opcodes.ACONST_NULL,
								Opcodes.PUTSTATIC, Opcodes.ACONST_NULL, Opcodes.PUTSTATIC, Opcodes.ACONST_NULL,
								Opcodes.PUTSTATIC };
						InstructionSearcher iSearcher = new InstructionSearcher(mn);
						for (int i = 0; i < opCodes.length; i++) {
							//System.out.println("OPCODE: " + iSearcher.getCurrent().getOpcode() + " | CHECK: " + opCodes[instnCount]);
							if (iSearcher.getCurrent().getOpcode() == opCodes[i]) {
								instnCount++;
							}
							iSearcher.getNext();
						}
						if (instnCount == opCodes.length) {
							Updater.rawClasses.put(node.name, node);
							return node;
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public ClassNode manipulate(ClassNode classNode) {
		return classNode;
	}

}
