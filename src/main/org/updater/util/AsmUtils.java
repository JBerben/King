package src.main.org.updater.util;

import java.awt.Rectangle;
import java.lang.reflect.Modifier;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class AsmUtils implements Opcodes {

	private static final Rectangle GAMESCREEN = new Rectangle(4, 4, 512, 334);
	public static final int[] CURVESIN = new int[2048];
	public static final int[] CURVECOS = new int[2048];

	// calculate the curves
	static {
		for (int i = 0; i < 2048; i++) {
			CURVESIN[i] = (int) (65536D * Math.sin((double) i * 0.0030679614999999999D));
			CURVECOS[i] = (int) (65536D * Math.cos((double) i * 0.0030679614999999999D));
		}
	}

	public static boolean isStatic(MethodNode method) {
		return Modifier.isStatic(method.access);
	}

	public static boolean isStatic(FieldNode field) {
		return Modifier.isStatic(field.access);
	}

	public static int[] getFieldCount(ClassNode c, boolean acceptStatic, String... descriptions) {
		int[] resultSet = new int[descriptions.length];
		for (Object f : c.fields) {
			FieldNode field = (FieldNode) f;
			if (acceptStatic != isStatic(field)) {
				continue;
			}
			for (int i = 0; i < descriptions.length; i++) {
				if (field.desc.equals(descriptions[i])) {
					resultSet[i]++;
				}
			}
		}
		return resultSet;
	}

	public static int getReturnOpcode(String desc) {
		desc = desc.substring(desc.indexOf("L") + 1);
		if (desc.length() > 1) {
			return ARETURN;
		}
		final char c = desc.charAt(0);
		switch (c) {
		case 'I':
		case 'Z':
		case 'B':
		case 'S':
		case 'C':
			return IRETURN;
		case 'J':
			return LRETURN;
		case 'F':
			return FRETURN;
		case 'D':
			return DRETURN;
		case 'V': // void, method desc
			return RETURN;
		}
		throw new RuntimeException("Wrong desc type: " + c);
	}

	public static int getOpcode(String d) {
		d = d.substring(d.indexOf("L") + 1);
		if (d.length() > 1) {
			return 25;
		}
		char c = d.charAt(0);
		switch (c) {
		case 'B':
		case 'C':
		case 'I':
		case 'S':
		case 'Z':
			return 21;
		case 'J':
			return 22;
		case 'F':
			return 23;
		case 'D':
			return 24;
		}
		throw new RuntimeException();
	}
	
	public static int getLoadOpcode(String desc) {
		desc = desc.substring(desc.indexOf("L") + 1);
		if (desc.length() > 1) {
			return ALOAD;
		}
		final char c = desc.charAt(0);
		switch (c) {
		case 'I':
		case 'Z':
		case 'B':
		case 'S':
		case 'C':
			return ILOAD;
		case 'J':
			return LLOAD;
		case 'F':
			return FLOAD;
		case 'D':
			return DLOAD;
		}
		throw new RuntimeException("Wrong desc type " + c);
	}
	
	public static void makePublic(FieldNode node) {
		if (!Modifier.isPublic(node.access)) {
			if (Modifier.isPrivate(node.access)) {
				node.access = node.access & (~Opcodes.ACC_PRIVATE);
			}
			if (Modifier.isProtected(node.access)) {
				node.access = node.access & (~Opcodes.ACC_PROTECTED);
			}
			node.access = node.access | Opcodes.ACC_PUBLIC;
		}
	}
	
	public static void makePublic(MethodNode node) {
		if (!Modifier.isPublic(node.access)) {
			if (Modifier.isPrivate(node.access)) {
				node.access = node.access & (~Opcodes.ACC_PRIVATE);
			}
			if (Modifier.isProtected(node.access)) {
				node.access = node.access & (~Opcodes.ACC_PROTECTED);
			}
			node.access = node.access | Opcodes.ACC_PUBLIC;
		}
	}
	
	public static void makePublic(ClassNode node) {
		if (!Modifier.isPublic(node.access)) {
			if (Modifier.isPrivate(node.access)) {
				node.access = node.access & (~Opcodes.ACC_PRIVATE);
			}
			if (Modifier.isProtected(node.access)) {
				node.access = node.access & (~Opcodes.ACC_PROTECTED);
			}
			node.access = node.access | Opcodes.ACC_PUBLIC;
		}
	}
	
}
