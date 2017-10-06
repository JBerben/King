package src.main.org.updater.obj;

public class Key {

	private Object obj;
	protected int hash;
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
