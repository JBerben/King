package king.org.trans;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import king.org.Updater;
import king.org.trans.Transform;

import javax.swing.*;
import java.util.Collection;
import java.util.List;

public class Player extends Transform {

	public Player(Updater i) {
		super(i);
	}

	@Override
	public ClassNode identify(Collection<ClassNode> classNodes) {
		for (ClassNode node : classNodes) {
			if (node.superName.equals(instance.getClassName("Entity")) && node.fields.size() > 10) {
				
				// Field Detection
				for (FieldNode fn : (List<FieldNode>)node.fields) {
					if (fn.desc.equals("Ljava/lang/String;")) {
						// We found playerName
						fn.name = "playerName";
					}
				}
				
				for (FieldNode fn : (List<FieldNode>)node.fields) {
					//System.out.println(fn.name);
					Updater.processedFields.put(node, fn);
				}
				
				//Updater.processedClasses.put("Player", node);
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
