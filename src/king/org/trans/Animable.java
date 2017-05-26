package king.org.trans;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import king.org.Updater;
import king.org.util.search.Finder;
import king.org.util.search.InstructionSearcher;

import java.util.Collection;
import java.util.List;

/* ANIMABLE
 * Super class: NodeSub
 * Fields: 2
 * Methods: 3
 * Attributes: 1
 * Access flag: Public | 0x00021
 */

public class Animable extends Transform {
    public Animable(Updater i) {
        super(i);
    }
    
    @Override
    public ClassNode identify(Collection<ClassNode> classNodes) {
        for (ClassNode node : classNodes) {
        	// Animable class extends NodeSub
        	if (node.superName.equals(instance.getClassName("NodeSub"))) {
        		for (MethodNode mn : (List<MethodNode>) node.methods) {
        			
        			/* Here, we search for a list of instructions by getting a list
        			 * of all the bytecode instructions within the <init> method,
        			 * and then checking if they match what an ideal Animable class'
        			 * <init> method should contain in terms of instructions.
        			 */
        			if (mn.name.equals("<init>")) {
        				InstructionSearcher iSearcher = new InstructionSearcher(mn);
        				short instnCount = 0;
        				int[] opCodes = { Opcodes.ALOAD, Opcodes.INVOKESPECIAL, Opcodes.ALOAD, Opcodes.SIPUSH, Opcodes.PUTFIELD, Opcodes.RETURN };
        				while (iSearcher.getCurrent() != null) {
        					if (iSearcher.getCurrent().getOpcode() == opCodes[instnCount]) {
        						instnCount++;
        					}
        					iSearcher.getNext();
        				}
        				if (instnCount == 6) {
        					Updater.rawClasses.put(node.name, node);
        					return node;
        				}
        			}
        		}
        	}
        }
        return null;
    }

    @Override
    public ClassNode manipulate(ClassNode classNode) {
        
    	/*
    	 * HOOKING FIELDS
    	 */
    	for (FieldNode fn : (List<FieldNode>) classNode.fields) {
    		if (fn.desc.equals("I")) {
    			Updater.oldFieldNames.put("modelHeight", fn.name);
    			fn.name = "modelHeight";
    		}
    		if (fn.desc.contains("L")) {
    			Updater.oldFieldNames.put("aClass33Array1425", fn.name);
    			fn.name = "aClass33Array1425";
    		}
    	}
    	
    	return classNode;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
