package src.main.org.bot.api.core.injection;

import src.main.org.bot.api.core.Client;
import src.main.org.bot.client.Main;

public class Game {
	
	private static Client client = getClient();
	
	public static Client getClient() {
		try {
			return (Client) Main.game.getClientInstance().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static int getBaseX() {
		return client.getBaseX();
	}
	
}
