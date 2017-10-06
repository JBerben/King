package src.main.org.bot.reflection;

import java.lang.reflect.Field;

import src.main.org.bot.client.Loader;

public class Strings {

    public static String getString(String className, String fieldName, Object obj){
        try {
            Field f = Loader.getClassLoader().loadClass(className).getDeclaredField(fieldName);
            f.setAccessible(true);
            return (String) f.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static String getString(String className, String fieldName){
        try {
            Field f = Loader.getClassLoader().loadClass(className).getDeclaredField(fieldName);
            f.setAccessible(true);
            return (String) f.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
