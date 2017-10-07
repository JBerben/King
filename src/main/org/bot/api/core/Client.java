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
    private static Object instance = getObject("com.locopk.client.rs.Client", "instance");
	
    private static String client;
    //public static Class<?> client = (Class<?>) Objects.getO(parser.getClass("client"));
    
    /*
     * Default constructor
     * Sets Client instance
     */
    public Client() {
        client = parser.getClass("client");
        instance = getObject(client, "instance");
        System.out.println(instance.toString());
    }

    /*
     * GETTERS
     */
    public boolean isLoggedIn() {
        return getBoolean(client, parser.getField("loggedIn"), instance);
    }

    public static int getOpenInterfaceID() {
        return getInt(client, parser.getField("currentOpenInterface"), instance);
    }

    public static int getBaseX() {
        return getInt(client, parser.getField("baseX"), instance);
    }
    
    public static int getBaseY() {
        return getInt(client, parser.getField("baseY"), instance);
    }
    
    public static int getPlayerCount() {
    	return getInt(client, parser.getField("playerCount"), instance);
    }

    public static int getPlane() {
        return getInt(client, parser.getField("plane"), instance);
    }
    
    public static int getOpenBackDialogId() {
    	return getInt(client, parser.getField("backDialogID"), instance);
    }

    public static int getLoopCycle() {
        return getInt(client, null, "loopCycle");
    }
    
    public static int[][] getCollisionFlags() {
    	return null;
    	//return get2dIntArray(client, parser.getField(""))
    }

    public static int getOverlayCurrentHealth() {
        return getInt(client, instance, "drawCurrentHealth");
    }

    public static int getOverlayMaxHealth() {
        return getInt(client, instance, "drawMaxHP");
    }

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
    
}
