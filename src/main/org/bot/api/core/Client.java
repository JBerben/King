package src.main.org.bot.api.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static src.main.org.bot.client.Loader.getClassLoader;
import static src.main.org.bot.reflection.Booleans.getBoolean;
import static src.main.org.bot.reflection.Integers.getInt;
import static src.main.org.bot.reflection.Objects.getObject;
import static src.main.org.bot.reflection.Objects.getObjectArray;

import src.main.org.bot.client.Loader;
import src.main.org.bot.client.logger.Logger;
import src.main.org.bot.hooks.XMLParser;
import src.main.org.bot.reflection.Integers;
import src.main.org.bot.reflection.Objects;

public class Client {

	private static XMLParser parser = new XMLParser("Hooks");
    public static Object instance = getObject("com.locopk.client.rs.Client", "instance");
    public static Class<?> client = (Class<?>) Objects.getO(parser.getClass("client"));
    
    public Client() {
        this.instance = getObject("com.locopk.client.rs.Client", "instance");
        System.out.println(instance.toString());
    }

    //not working for some reason :|
    public static void doAction(int i) {
        try {
            Method m = getClassLoader().loadClass("com.locopk.client.rs.Client").getDeclaredMethod("doAction", int.class);
            if (m == null) {
                return;
            }
            m.setAccessible(true);
            m.invoke(instance, i);

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void trackPlayers() {
    	try {
    		Class pTypes[] = {Stream.class, int.class };
    		Client client = new Client();
    		Class<?> c = client.getClass();
    		Method m = c.getDeclaredMethod("method91", pTypes[0], pTypes[1]);
    		if (m == null) {
    			return;
    		}
    		m.setAccessible(true);
    		m.invoke(client);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public static void Set() {
    	Integers.SetAllFields(client);
    }

    public static Object Call(Class<?> className, Object instance, String fieldName) {
    	Field f;
		try {
			f = className.getDeclaredField(fieldName);
			f.setAccessible(true);
			System.out.println(f.get(instance));
			Logger.writeConsole(fieldName.toUpperCase() + ": " + f.get(instance).toString());
			return f.get(instance);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
    
    // THIS SHIT WORKS OMG
    public static int Test() {
    	return Integers.getInt(client, instance, "playerCount");
    }
    
    public boolean isLoggedIn() {
        return getBoolean("com.locopk.client.rs.Client", "loggedIn", instance);
    }

    public static int getOpenInterfaceID() {
        return getInt(client, instance, "openInterfaceID");
    }

    public static int getBaseX() {
        return getInt(client, instance, parser.getField("baseX"));
    }
    
    public static int getPlayerCount() {
    	return getInt(client, instance, parser.getField("playerCount"));
    }

    public static int getBaseY() {
        return getInt(client, instance, "baseY");
    }

    public static int getPlane() {
        return getInt(client, instance, "plane");
    }

    public static int getLoopCycle() {
        return getInt(client, null, "loopCycle");
    }

    public static int getOverlayCurrentHealth() {
        return getInt(client, instance, "drawCurrentHealth");
    }

    public static int getOverlayMaxHealth() {
        return getInt(client, instance, "drawMaxHP");
    }

   // public static int[] getTabInterfaceIds() {
    //    return getIntArray(client, instance, "tabInterfaceIDs");
    //}

    public static int getGameTabID() {
        return getInt(client, instance, "tabID");
    }

    public static int getYaw() {
        return getInt(client, instance, "xCameraCurve");
    }

    public static double getPitch(){
        return getInt(client, instance, "yCameraCurve");
    }

    public static int getCameraX(){
        return getInt(client, instance, "xCameraPos");
    }

    public static int getCameraY(){
        return getInt(client, instance, "yCameraPos");
    }

    public static int getCameraZ(){
        return getInt(client, instance, "zCameraPos");
    }

    public static double getAngleDegree() {
        double yaw = getYaw();
        return (yaw != -1) ? yaw / 2048.0 * 360 : -1;
    }

    public static Object[] getNPCArray() {
        return getObjectArray("com.locopk.client.rs.Client", "npcArray", instance);
    }

    public static Object[] getNPCIndices() {
        return getObjectArray("com.locopk.client.rs.Client", "npcIndex", instance);
    }

    public static void intfields() {
        try {
            // Field f = Loader.getClassLoader().loadClass(className).getDeclaredField(fieldName);
            for (Field f : Loader.getClassLoader().loadClass("com.locopk.client.rs.Client").getDeclaredFields()) {
                try {
                if (f.getType().equals(int.class)) {
                    f.setAccessible(true);

                    System.out.println(f.getName() + ": " + f.get(instance));
                }
                }
                catch (Exception e) {
                        // e.printStackTrace();

                    }
            }
            System.out.println();
        } catch (Exception e) {
           // e.printStackTrace();

        }
    }
}
