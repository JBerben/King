package src.main.org.bot.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import src.main.org.bot.client.Loader;

public class Integers {
	
	//Temp
	public static void SetAllFields(Class<?> c) {
		Field[] fields;
		fields = c.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			System.out.println(fields[i].getName());
		}
	}

	public static int getInt(Class<?> c, Object obj, String fieldName) {
		try {
			Field f = c.getDeclaredField(fieldName);
			f.setAccessible(true);
			return f.getInt(obj);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1337;
	}
	
	// Filler method for the moment to stop compiler throwing
	public static int getInt(String c, String f, Object o) {
		return 0;
	}
	
	/* OLD METHOD
	public static int getInt(String className, String fieldName, Object obj) {
		try {
			Field f = Loader.getClassLoader().loadClass(className).getDeclaredField(fieldName);
			System.out.println(f.getName());
			
			if (Modifier.isStatic(f.getModifiers())) {
				System.out.println("IS STATIC");
				f.setAccessible(true);
				System.out.println("::::::::::::::: " + f.getName());
				return f.getInt(Class.forName(className));
			} else if (!Modifier.isStatic(f.getModifiers())) {
				System.out.println("IS NONSTATIC");
				f.setAccessible(true);
				f = obj.getClass().getDeclaredField(fieldName);
				System.out.println(obj.getClass().getFields());
				System.out.println("::::::::::::::: " + f.getName());
				return f.getInt(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1337;
	}
	*/

	public static int[] getIntArray(String className, String fieldName, Object obj) {
		try {
			Field f = Loader.getClassLoader().loadClass(className).getDeclaredField(fieldName);
			f.setAccessible(true);
			return (int[]) f.get(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new int[] {};
	}

	/*
	 * public static int getInt(final String className, final String fieldName,
	 * final Object obj) throws SecurityException, NoSuchFieldException,
	 * ClassNotFoundException, IllegalArgumentException, IllegalAccessException
	 * { try { final Field f =
	 * Loader.getClassLoader().loadClass(className).getDeclaredField(fieldName);
	 * 
	 * if (Modifier.isStatic(f.getModifiers())) { f.setAccessible(true); return
	 * f.getInt(obj); } else { f = obj.getClass().getDeclaredField(fieldName);
	 * f.setAccessible(true); return f.getInt(f); } } catch (Exception e) {
	 * e.printStackTrace(); } return -1337; }
	 */
}
