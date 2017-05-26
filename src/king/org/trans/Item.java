package king.org.trans;

import java.util.Collection;

import javax.xml.transform.Transformer;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import king.org.Updater;

public class Item extends Transform {

	public Item(Updater i) {
		super(i);
		
	}

	@Override
	public ClassNode identify(Collection<ClassNode> classNodes) {
		for (ClassNode node : classNodes) {
			if (node.superName.equals(instance.getClassName("Animable")) && node.fields.size() < 6
					&& node.methods.size() <= 2) {
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
