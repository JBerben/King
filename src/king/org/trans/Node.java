package king.org.trans;


import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import king.org.Updater;
import king.org.util.search.InstructionSearcher;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

public class Node extends Transform {
    public Node(Updater i) {
        super(i);
    }

    @Override
    public ClassNode identify(Collection<ClassNode> classNodes) {
    	
    	/*
    	 * Finds the Node class
    	 */
        for (ClassNode classNode : classNodes) {
            short nodeCount = 0, longCount = 0, fieldCount = 0;
            for (FieldNode f : (List<FieldNode>) classNode.fields) {
                if (f.access == Opcodes.ACC_PUBLIC) {
                    if (f.desc.equals("L" + classNode.name + ";")) {
                        nodeCount++;
                    } else if (f.desc.equals("J")) {
                        longCount++;
                    }
                    fieldCount++;
                }
            }
            if (nodeCount == 2 && longCount == 1 && fieldCount == 3) {
            	Updater.rawClasses.put(classNode.name, classNode);
                return classNode;
            }
        }
        return null;
    }

    @Override
    public ClassNode manipulate(ClassNode classNode) {
        
    	/*
    	 * HOOKING FIELDS
    	 */
    	Updater.rawClasses.put(classNode.name, classNode);
    	for (FieldNode fn : (List<FieldNode>) classNode.fields) {
    		
    		/* This exploits the fact that the "prev" field is always stored
    		 * before the "next" field in the constant pool. So this count
    		 * method is just a cheesy way of finding these 2 fields
    		 * 
    		 * The way that this works is so unstable it's not even funny
    		 * - Will work on a better method when I can think of one :)
    		 */
    		if (fn.desc.equals("J")) {
    			Updater.oldFieldNames.put("getId", fn.name);
    			fn.name = "getId";
    		}
    		
    		// Finds the next field
    		String nextName = "";
    		ListIterator<MethodNode> mnIt = classNode.methods.listIterator();
    		methodIterator: while (mnIt.hasNext()) {
    			MethodNode mn = mnIt.next();
    			if ((mn.access & Opcodes.ACC_STATIC) == 0) {
    				if (mn.desc.equals("()V")) {
    					ListIterator <AbstractInsnNode> aInIt = mn.instructions.iterator();
    					while (aInIt.hasNext()) {
    						AbstractInsnNode aIn = aInIt.next();
    						if (aIn instanceof FieldInsnNode) {
    							nextName = ((FieldInsnNode) aIn).name;
    							break methodIterator;
    						}
    					}
    				}
    			}
    		}
    		
    		if (fn.name != nextName && !fn.desc.equals("J")) {
    			Updater.oldFieldNames.put("getPrev", fn.name);
    			fn.name = "getPrev";
    		}
    		
    		if (fn.name.equals(nextName)) {
    			Updater.oldFieldNames.put("getNext", fn.name);
    			fn.name = "getNext";
    		}
    	}
    	
    	/*
    	 * HOOKING METHODS
    	 * W.I.P - Might not continue with as there isn't much point to it
    	 */
    	for (MethodNode mn : (List<MethodNode>) classNode.methods) {
    		int PUBLIC_FINAL = 0x0011;
    		int PUBLIC = 0x0001;
    		
    		//Finds the Unlink() method
    		if (mn.access == PUBLIC_FINAL) {
    			mn.name = "unlink";
    		} else if (mn.access == PUBLIC) {
    			mn.name = "<init>";
    		}
    	}
    	
    	Updater.processedClasses.put("Node", classNode);
    	return classNode;
    }
}
