package src.main.org.bot.api.core.injection;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class Instance {
	
	private Class<?> clientInstance;
	private URL[] gameLocation;
	private URLClassLoader ucl;
	private String server;
	private int session;
	
	public Instance(String url) {
		try {
			this.gameLocation = new URL[] {new URL(url)};
			ucl = new URLClassLoader(gameLocation);
			this.clientInstance = loadGameClass("client");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public Instance() {
		this.gameLocation = null;
		this.ucl = null;
		this.server = null;
		this.session = -1;
	}
	
	/*
	 * GETTERS
	 */
	public Class<?> getClientInstance() {
		return this.clientInstance;
	}
	
	
	/*
	 * SETTERS
	 */
	
	
	/*
	 * METHODS
	 */
	public Class<?> loadGameClass(String name) {
		try {
			return ucl.loadClass(name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
