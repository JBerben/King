package src.main.org.bot.reflection;

import java.lang.reflect.Field;

import src.main.org.bot.client.Loader;

public class Objects {

    public static Object getObject(String className, String fieldName) {
        try {
            Class<?> c = Loader.getClassLoader().loadClass(className);
            Field f = c.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getObject(String className, String fieldName, Object obj) {
        try {
            Field f = Loader.getClassLoader().loadClass(className).getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static Object getO(String className) {
		try {
			Class<?> c = Loader.getClassLoader().loadClass(className);
			System.out.println(c.getName());
			return (Object) c;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    	return null;
    }

    public static Object[] getObjectArray(String className, String fieldName) {
        try {
            Class<?> c = Loader.getClassLoader().loadClass(className);
            Field f = c.getDeclaredField(fieldName);
            f.setAccessible(true);
            return (Object[]) f.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object[] getObjectArray(String className, String fieldName, Object obj) {
        try {
            Field f = Loader.getClassLoader().loadClass(className).getDeclaredField(fieldName);
            f.setAccessible(true);
            return (Object[]) f.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
