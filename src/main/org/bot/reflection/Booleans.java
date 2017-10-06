package src.main.org.bot.reflection;

import java.lang.reflect.Field;

import src.main.org.bot.client.Loader;

public class Booleans {

    public static Boolean getBoolean(String className, String fieldName, Object obj){
        try {
            Field f = Loader.getClassLoader().loadClass(className).getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.getBoolean(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
