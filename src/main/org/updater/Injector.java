package src.main.org.updater;

import java.util.HashMap;
import java.util.List;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import src.main.org.updater.obj.GameField;
import src.main.org.updater.util.AsmUtils;
import src.main.org.updater.util.Stream;

public class Injector implements Opcodes {

	private ClassNode node;
	private String name;

	public Injector(ClassNode node) {
		this.node = node;
		this.name = node.name;
	}

	public void modify() {
		for (FieldNode f : (List<FieldNode>) this.node.fields) {

			String getterName = "get" + f.name;
			MethodNode method = new MethodNode(Opcodes.ACC_PUBLIC, getterName, "()" + f.desc, null, null);
			method.visitFieldInsn(AsmUtils.isStatic(f) ? Opcodes.GETSTATIC : Opcodes.GETFIELD, this.node.name, f.name,
					f.desc);
			// method.visitInsn(Type.getType(this.node));
			// method.visitInsn(Type.getType(node.getClass()).getOpcode(Opcodes.IRETURN));
			method.visitInsn(AsmUtils.getReturnOpcode(f.desc));

			if (this.node.methods.add(method)) {
				Stream.log("Injected: " + getterName + " into " + this.node.name);
			}
		}
	}

	public void injectTest() {
		for (FieldNode f : (List<FieldNode>) this.node.fields) {
			String methodName = "get" + f.name.substring(0, 1).toUpperCase() + f.name.substring(1);
			
			MethodNode method = new MethodNode(AsmUtils.isStatic(f) ? Opcodes.GETSTATIC : Opcodes.GETFIELD,
					methodName, "()" + f.desc, null, null);
			if (!AsmUtils.isStatic(f)) {
				method.visitVarInsn(Opcodes.ALOAD, 0);
			}
			if (AsmUtils.isStatic(f)) {
				AsmUtils.makePublic(f);
			}
			method.visitFieldInsn(AsmUtils.isStatic(f) ? GETSTATIC : GETFIELD, node.name, f.name, f.desc);

			if (f.desc.contains("L")) {
				if (!f.desc.contains("[")) {
					method.visitTypeInsn(CHECKCAST, f.desc.replaceFirst("L", "").replaceAll(";", ""));
				} else {
					method.visitTypeInsn(CHECKCAST, f.desc);
				}
			}
			method.visitInsn(AsmUtils.getReturnOpcode(f.desc));
			method.visitMaxs(1, 1);
			this.node.methods.add(method);
			//Stream.log("Injected: " + methodName + " into " + this.node.name);
		}
	}

	// Needs a lot of work :(
	public static void injectGetters(ClassNode c) {
		HashMap<GameField, GameField> fields = Updater.matchedFields;

		for (GameField gf : fields.values()) {
			String methodName = "get" + gf.getName();
			MethodVisitor mv = c.visitMethod(gf.getFlag(), methodName, "()" + gf.getDesc(), null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitFieldInsn(Opcodes.GETFIELD, gf.getAcessor().name, gf.getAcessor().name, gf.getDesc());
			mv.visitInsn(Type.getType(c.getClass()).getOpcode(Opcodes.IRETURN));
			mv.visitMaxs(0, 0);
		}

	}

}
