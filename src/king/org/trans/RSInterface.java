package king.org.trans;

import java.util.Collection;
import java.util.List;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import king.org.Updater;

public class RSInterface extends Transform {

	public RSInterface(Updater i) {
		super(i);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ClassNode identify(Collection<ClassNode> classNodes) {
		
		for (ClassNode node : classNodes) {
			if (node.fields.size() > 61 && node.fields.size() < 150) {
				for (FieldNode fn : (List<FieldNode>)node.fields) {
					if (fn.desc.equals("[L" + node.name + ";")) {
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
		return classNode;
	}
	
	

}
