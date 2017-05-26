package king.org.trans;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import king.org.Updater;

import java.util.Collection;

public class Character extends Transform {
	public Character(Updater i) {
		super(i);
	}

	@Override
	public ClassNode identify(Collection<ClassNode> classNodes) {
		for (ClassNode node : classNodes) {
			if (node.access == 33 && !node.superName.equals("java/lang/Object") && node.fields.size() > 55
					&& node.fields.size() < 65) {
				Updater.rawClasses.put(node.name, node);
				return node;
			}
		}
		return null;
	}

	@Override
	public ClassNode manipulate(ClassNode classNode) {
		return classNode; // To change body of implemented methods use File |
							// Settings | File Templates.
	}
}
