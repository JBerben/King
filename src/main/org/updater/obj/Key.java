package src.main.org.updater.obj;

public class Key {

	public enum Rights {
		FREE(1),
		USER(10),
		PAID(-1),
		DEV(-1),
		CREATOR(-1);
		
		protected int uses;
		
		Rights(int uses) {
			this.uses = uses;
		}
	}
	
	private Object obj;
	protected int hash;
	protected int uses;
	protected boolean inUse;
	
	public Key(Object obj) {
		this.obj = obj;
	}
	
	/*
	 * GETTERS
	 */
	public Object getObject() {
		return this.obj;
	}
	
	public int getHash() {
		return this.hash;
	}
	
	public boolean getStatus() {
		return this.inUse;
	}
	
	/*
	 * SETTERS
	 */
	public void activate() {
		this.hash = obj.hashCode();
		this.inUse = true;
	}
	
	/*
	 * FUNCTIONS
	 */
	public void deactivate() {
		this.obj = null;
	}
}
