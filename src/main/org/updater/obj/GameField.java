package src.main.org.updater.obj;

import java.util.HashMap;

import org.objectweb.asm.tree.ClassNode;

public class GameField {

	private ClassNode acessor;
	private String name;
	private String desc;
	private int type;
	private int flag;
	public int timesUsed;
	
	// Name, TimesUsed
	public HashMap<String, Integer> methodsUsedIn = new HashMap<String, Integer>();
	
	public GameField() {
		this.name = null;
		this.desc = null;
		this.type = 0;
	}
	
	/*
	 * GETTERS
	 */
	public String getName() {
		return this.name;
	}
	
	public String getDesc() {
		return this.desc;
	}
	
	public int getType() {
		return this.type;
	}
	
	public ClassNode getAcessor() {
		return this.acessor;
	}
	
	public int getFlag() {
		return this.flag;
	}
	
	/*
	 * SETTERS
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public void setAcessor(ClassNode c) {
		this.acessor = c;
	}
	
}
