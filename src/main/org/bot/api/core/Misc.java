package src.main.org.bot.api.core;

import static src.main.org.bot.reflection.Integers.getInt;
import static src.main.org.bot.reflection.Integers.getIntArray;
import static src.main.org.bot.reflection.Strings.getString;
import static src.main.org.bot.reflection.Booleans.getBoolean;

/* Created on 3/05/17
 * By JBerben
 * Used for future methods and fields that are yet to be
 * implemented into another API category
 */

public class Misc {

	
	private static Object objectInstance;
	
	// Returns the equipment ID's of the local player
	public static int[] Equipment() {
		int[] equipment = getIntArray("com.locopk.client.rs.Player", "equipment", objectInstance);
		return equipment;
	}
	
	// Returns whether the player is visible or not
	public static boolean isSkulled() {
		return getBoolean("com.locopk.client.rs.Player", "visible", objectInstance);
	}
	
}
