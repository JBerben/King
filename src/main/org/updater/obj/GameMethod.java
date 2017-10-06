package src.main.org.updater.obj;

import java.util.ArrayList;
import java.util.HashMap;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import src.main.org.updater.patterns.FingerPrint;
import src.main.org.updater.util.Maths;

public class GameMethod {

	private String name;
	private String desc;
	private ClassNode cNode;
	private MethodNode mNode;
	public InsnList bytecode;
	public HashMap<Integer, String> opcodes;
	public float similarity;
	public int descSimCount;
	public float descSim;
	public float bytecodeSim;
	public ArrayList<FingerPrint> fPrints = new ArrayList<FingerPrint>();
	public HashMap<String, GameField> gameFields = new HashMap<String, GameField>();

	public GameMethod() {
		this.name = null;
		this.cNode = null;
		this.bytecode = null;
		this.opcodes = new HashMap<Integer, String>();
		this.similarity = 0;
		this.descSimCount = 0;
		this.descSim = 0;
	}

	public GameMethod(ClassNode cNode) {
		this.cNode = cNode;
		this.bytecode = null;
		this.opcodes = new HashMap<Integer, String>();
		this.similarity = 0;
	}

	public GameMethod(MethodNode mNode) {
		this.mNode = mNode;
		this.name = mNode.name;
		// this.gameFields = Analysis.stripInsnList(this.mNode);
	}

	/*
	 * GETTERS
	 */
	public InsnList getBytecode() {
		return this.bytecode;
	}

	public ClassNode getClassNode() {
		return this.cNode;
	}

	public MethodNode getMethodNode() {
		return this.mNode;
	}

	public String getName() {
		return this.name;
	}

	public float getDescSimilarity(GameMethod clazz, GameMethod other) {
		if (descSim == 0) {
			descSim = Maths.getRatio(clazz.getDescSimCount(), other.getDescSimCount());
		}
		return Maths.getRatio(clazz.getDescSimCount(), other.getDescSimCount());
	}

	public int getDescSimCount() {
		return this.descSimCount;
	}

	public float getSimilarity() {
		this.similarity = bytecodeSim;
		return this.similarity;
	}

	public String getDesc() {
		return this.desc;
	}

	/*
	 * SETTERS
	 */
	public void setClassNode(ClassNode cNode) {
		this.cNode = cNode;
	}

	public void setMethodNode(MethodNode mNode) {
		this.mNode = mNode;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
