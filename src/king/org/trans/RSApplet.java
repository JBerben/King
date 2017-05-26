package king.org.trans;

import java.util.Collection;
import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import king.org.Updater;
import king.org.util.search.InstructionSearcher;

public class RSApplet extends Transform {

	public RSApplet(Updater i) {
		super(i);
	}

	@Override
	public ClassNode identify(Collection<ClassNode> classNodes) {
		for (ClassNode node : classNodes) {
			if (node.superName.equals("javax/swing/JApplet") ||
					node.superName.equals("java/applet/Applet")) {
				
				// Looking for the method "start()"
				for (MethodNode mn : (List<MethodNode>) node.methods) {
					if (mn.desc.equals("()V") && mn.access == Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL) {
						short instnCount = 0;
						int[] opCodes = { Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.IFLT, Opcodes.ALOAD,
								Opcodes.ICONST_0, Opcodes.PUTFIELD, Opcodes.RETURN };
						InstructionSearcher iSearcher = new InstructionSearcher(mn);
						while (iSearcher.getCurrent() != null) {
							if (iSearcher.getCurrent().getOpcode() == opCodes[instnCount]) {
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
