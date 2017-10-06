package src.main.org.updater.util.search;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InstructionSearcher {
	
	public ArrayList<Integer> pos = new ArrayList<Integer>();
	private InsnList list;
	private AbstractInsnNode current;
	private MethodNode method;

	public InstructionSearcher(MethodNode m) {
		this.method = m;
		this.list = m.instructions;
		this.current = list.getFirst();
	}

	public AbstractInsnNode getCurrent() {
		return current;
	}

	public void setCurrent(AbstractInsnNode in) {
		current = in;
	}

	public AbstractInsnNode getNext(int opcode) {
		while (current != null) {
			if (current.getOpcode() == opcode) {
				AbstractInsnNode old = current;
				current = current.getNext();
				return old;
			}
			current = current.getNext();
		}
		return null;
	}

	public AbstractInsnNode getNext() {
		if (current != null) {
			current = current.getNext();
			while (current != null && current.getOpcode() == -1) {
				current = current.getNext();
			}
		}
		return current;
	}

	public int getLastSet(ArrayList<Integer> opcodes) {
		int size = this.list.size();
		int initial = this.getIndex();
		int furtherest = 0;
		for (int i = 0; i < opcodes.size(); i++) {
			while (getNext(opcodes.get(i)) != null) {
				if (furtherest < this.getIndex()) {
					furtherest = this.getIndex();
				}
			}
		}
		this.setIndex(furtherest);
		if (current != null) {
			this.setIndex(initial);
			return this.getIndex();
		}
		this.setIndex(initial);
		return -1;
	}

	public AbstractInsnNode getPrevious(int opcode) {
		while (current != null) {
			if (current.getOpcode() == opcode) {
				AbstractInsnNode old = current;
				current = current.getPrevious();
				return old;
			}
			current = current.getPrevious();
		}
		return null;
	}

	public AbstractInsnNode getPrevious() {
		current = current.getPrevious();
		while (current.getOpcode() == -1)
			current = current.getPrevious();
		return current;
	}

	public LdcInsnNode getNextLDC(Object cst) {
		AbstractInsnNode in;
		while ((in = getNext(Opcodes.LDC)) != null) {
			LdcInsnNode ln = (LdcInsnNode) in;
			if (ln.cst.equals(cst))
				return ln;
		}
		return null;
	}

	public LdcInsnNode getPreviousLDC(Object cst) {
		AbstractInsnNode in;
		while ((in = getPrevious(Opcodes.LDC)) != null) {
			LdcInsnNode ln = (LdcInsnNode) in;
			if (ln.cst.equals(cst))
				return ln;
		}
		return null;
	}

	public IntInsnNode getNextPush(int opcode, int value) {
		AbstractInsnNode in;
		while ((in = getNext(opcode)) != null) {
			IntInsnNode iin = (IntInsnNode) in;
			if (iin.operand == value)
				return iin;
		}
		return null;
	}
	
	public InsnList getList() {
		return list;
	}

	public List<AbstractInsnNode> analyze(int opcode) {
		reset();
		List<AbstractInsnNode> list = new ArrayList<AbstractInsnNode>();
		AbstractInsnNode in;
		while ((in = getNext(opcode)) != null) {
			list.add(in);
		}
		return list;
	}

	// Analyzes a section of instructions
	public List<Integer> analyzeSection(int lines) {
		reset();
		List<Integer> list = new ArrayList<Integer>();
		AbstractInsnNode in;
		int opcode;
		for (int i = 0; i < lines; i++) {
			opcode = getCurrent().getOpcode();
			list.add(opcode);
			in = getNext();
		}
		return list;
	}

	public int findPairIndex(int first, int next) {
		reset();
		AbstractInsnNode in;
		int index = 0;
		while ((in = getNext(next)) != null) {
			index = getIndex();
			setIndex(index - 1);
			getPrevious();
			if (getCurrent().getOpcode() == first) {
				return index - 2;
			}
		}
		return 0;
	}

	public int getIndex() {
		return list.indexOf(current);
	}

	public void setIndex(int index) {
		current = list.get(index);
	}

	public void reset() {
		current = list.getFirst();
	}
	
	public MethodNode getMethodNode() {
		return this.method;
	}

}