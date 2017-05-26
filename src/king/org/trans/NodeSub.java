package king.org.trans;

import java.util.Collection;
import java.util.List;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import king.org.Updater;

public class NodeSub extends Transform {

	public NodeSub(Updater i) {
		super(i);
	}
	
	@Override
	public ClassNode identify(Collection<ClassNode> classNodes) {
		for (ClassNode node : classNodes) {
			short fields = 0;
			if (node.superName.equals(instance.getClassName("Node"))) {
				for (FieldNode fn : (List<FieldNode>) node.fields) {
					if (fn.desc.equals("L" + node.name + ";")) {
						fields++;
					}
				}
			}
			if (fields == 2 && node.methods.size() == 2) {
				Updater.rawClasses.put(node.name, node);
				return node;
			}
		}
		return null;
	}
	
	@Override
	public ClassNode manipulate(ClassNode classNode) {
		return classNode;
	}

}
