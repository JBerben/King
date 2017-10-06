package src.main.org.updater.util.search;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class ConstantPool {

	public static void RetrieveField(int poolIndex) {
		
	}
	
	public static int findPoolIndex(MethodNode method, AbstractInsnNode iNode) {
		InstructionSearcher iSearch = new InstructionSearcher(method);
		String instruction = null;
		int index = 0;
		
		while (iSearch.getNext() != null) {
			if (iSearch.getNext() == iNode) {
				instruction = iNode.toString();
				instruction = instruction.replaceAll("[^0-9]+", "");
				index = Integer.parseInt(instruction);
				if (index > 0) {
					return index;
				}
			}
		}
		return 0;
	}
 	
}
