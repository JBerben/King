package src.main.org.updater.obj;

import java.util.HashMap;

import org.objectweb.asm.tree.ClassNode;

import src.main.org.updater.util.Maths;

public class GameClass {

	String name;
	ClassNode node;
	public float fieldSim;
	public float methodSim;
	public boolean hasSuperMatch;
	private float similarity;
	public int descSimCount;
	public float descSim;
	public float sizeSim;
	public float fieldDescSim;
	public short interfaceMatch;
	public float bytecodeSim;
	public float attSim;
	public float superSim;
	public HashMap<String, GameMethod> gameMethods = new HashMap<String, GameMethod>();

	// Default Constructor
	public GameClass() {
		name = null;
		node = null;
		fieldSim = 0;
		methodSim = 0;
		hasSuperMatch = false;
		similarity = 0;
		descSimCount = 0;
		descSim = 0;
		sizeSim = 0;
		fieldDescSim = 0;
		interfaceMatch = 0;
		bytecodeSim = 0;
		attSim = 0;
		superSim = 0;
	}
	
	public GameClass(ClassNode cn) {
		name = cn.name;
		node = cn;
		fieldSim = 0;
		methodSim = 0;
		hasSuperMatch = false;
		similarity = 0;
		descSimCount = 0;
		descSim = 0;
		sizeSim = 0;
		fieldDescSim = 0;
		interfaceMatch = 0;
		bytecodeSim = 0;
		attSim = 0;
		superSim = 0;
	}

	// Custom Constructor
	public GameClass(String name, ClassNode node, int fieldSim, int methodSim) {
		this.name = name;
		this.node = node;
		this.fieldSim = fieldSim;
		this.methodSim = methodSim;
		this.hasSuperMatch = false;
		this.similarity = 0;
		this.descSimCount = 0;
		this.descSim = 0;
		this.sizeSim = 0;
		this.fieldDescSim = 0;
		this.interfaceMatch = 0;
		this.bytecodeSim = 0;
		this.attSim = 0;
		this.superSim = 0;
	}

	/*
	 * GETTERS
	 */
	public String getName() {
		return name;
	}

	public ClassNode getNode() {
		return node;
	}

	public float getSimilarity() {
		if (this.similarity <= 0) {
			if (this.hasSuperMatch) {
				this.similarity = (float) (((this.methodSim) + (this.fieldSim) + 0.1f + (this.descSim)
						+ (this.sizeSim) + (this.fieldDescSim * 3) + (interfaceMatch * 3)
						+ (this.bytecodeSim * 3) + (this.attSim) + (this.superSim * 6)) / 7);
			} else {
				this.similarity = (float) (((this.methodSim) + (this.fieldSim) + (this.descSim)
						+ (this.sizeSim) + (this.fieldDescSim * 3) + (interfaceMatch * 3)
						+ (this.bytecodeSim * 3) + (this.attSim) + (this.superSim * 6)) / 7);
			}
			return this.similarity;
		} else {
			return this.similarity;
		}
	}

	public String getSuperName() {
		if (hasSuperMatch) {
			return node.superName;
		} else {
			return null;
		}
	}

	public float getDescSimilarity() {
		if (!Maths.isNan(descSim)) {
			descSim = Maths.getRatio(this.getDescSimCount(), this.getNode().methods.size());
		} else {
			descSim = Maths.getRatio(this.getDescSimCount(), this.getNode().methods.size());
		}
		return descSim;
	}

	public int getDescSimCount() {
		return this.descSimCount;
	}

	/*
	 * SETTERS
	 */
	public void setName(String newName) {
		this.name = newName;
	}

	public void setNode(ClassNode newNode) {
		this.node = newNode;
	}
	
	public void setSimilarity(float sim) {
		this.similarity = sim;
	}
}
