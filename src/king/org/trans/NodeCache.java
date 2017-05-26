package king.org.trans;

import java.util.Collection;
import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import king.org.Updater;

public class NodeCache extends Transform {

	public NodeCache(Updater i) {
		super(i);
	}

	@Override
	public ClassNode identify(Collection<ClassNode> classNodes) {
		
		for (ClassNode cn : classNodes) {
			if (cn.fields.size() == 2 && cn.methods.size() <= 4) {
				int size = 0, cache = 0;
				for (FieldNode fn : (List<FieldNode>) cn.fields) {
					if (fn.desc.equals("I")) {
						size++;
					}
					if (fn.desc.contains("L") && fn.access == 0x0012) {
						cache++;
					}
				}
				if (size == 1 && cache == 1) {
					return cn;
				}
			}
		}
		return null;
	}

	@Override
	public ClassNode manipulate(ClassNode classNode) {
		
		/* Hooking fileds for NodeCache
		 * The "String name" technique is used so that "fn.name" isn't
		 * referenced to within the HashMap, since this is the variable that
		 * we are changing to the new name.
		 * Still need to check if this works as intended or if it's useless >.>
		 * 
		 * oldFieldNames(newName, oldName); <- because I always mess it up
		 */
		for (FieldNode fn : (List<FieldNode>) classNode.fields) {
			if (fn.desc.equals("I")) {
				String name = fn.name;
				Updater.oldFieldNames.put("getSize", fn.name);
				fn.name = "getSize";
			} else {
				String name = fn.name;
				Updater.oldFieldNames.put("getCache", fn.name);
				fn.name = "getCache";
			}
		}
		
		/* Hooking Methods
		 * - Only 2 methods in this class
		 * We use these methods to determine prev and next fields
		 * within the Node class
		 */
		for (MethodNode mn : (List<MethodNode>) classNode.methods) {
			if (!mn.name.contains("init")) {
				if (mn.desc.contains(")V")) {
					Updater.oldMethodNames.put("removeFromCache", mn.name);
					mn.name = "removeFromCache";
				} else {
					Updater.oldMethodNames.put("findNodeByID", mn.name);
					mn.name = "findNodeByID";
				}
			}
		}
		
		return classNode;
	}

}
