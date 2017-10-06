package src.main.org.updater.patterns;

import java.util.HashMap;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import src.main.org.updater.util.search.InstructionSearcher;

/*
 * Fingerprints are how sets of bytecode instructions are
 * matched.
 */
public class FingerPrint {
	
	private InsnList instructions = new InsnList();
	private ClassNode cNode = null;
	private MethodNode mNode;
	private AbstractInsnNode anchor;
	private int size;
	public int index;
	
	// Standard Constructor
	public FingerPrint(MethodNode mNode, InsnList instructions) {
		this.mNode = mNode;
		this.anchor = instructions.getFirst();
		index = instructions.getFirst().LINE;
		this.size = instructions.size();
	}
	
	// Default Constructor
	public FingerPrint() {
		this.mNode = null;
		this.anchor = null;
		this.index = -1;
		this.size = 0;
	}
	
	/*
	 * GETTERS
	 */
	public ClassNode getClassNode() {
		try {
			return this.cNode;
		} catch (NullPointerException e) {
			System.out.println("Fingerprint has no classnode assigned: ");
			e.printStackTrace();
			return null;
		}
	}
	
	public MethodNode getMethodNode() {
		try {
			return this.mNode;
		} catch (NullPointerException e) {
			System.out.println("Fingerprint has no methodnode assigned: ");
			e.printStackTrace();
			return null;
		}
	}
	
	public AbstractInsnNode getAnchor() {
		try {
			return this.anchor;
		} catch (NullPointerException e) {
			System.out.println("Fingerprint has no anchor. Might possibly be corrupt: ");
			e.printStackTrace();
			return null;
		}
	}
	
	public int getSize() {
		return size;
	}
	
	/*
	 * SETTERS
	 */
	public void setAnchor(AbstractInsnNode ain) {
		this.anchor = ain;
	}
	
	public void setMethodNode(MethodNode mNode) {
		this.mNode = mNode;
	}
	
	/*
	 * GETTERS
	 */
	public InsnList getInstructionList() {
		return instructions;
	}
	
	/*
	 * OTHER FUNCTIONS
	 */
	public boolean composeList(int start, int end) {
		InstructionSearcher is = new InstructionSearcher(this.mNode);
		is.setIndex(start);
		this.anchor = is.getCurrent();
		
		for (int i = start; i < end; i++) {
			is.setIndex(i);
			instructions.add(is.getCurrent());
		}
		
		if (instructions.size() == (end - start) + 1) {
			if (size < 1) {
				size = instructions.size();
			}
			return true;
		} else {
			return false;
		}
	}
	
}
