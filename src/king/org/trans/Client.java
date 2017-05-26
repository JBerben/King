package king.org.trans;

import king.org.Updater;
import king.org.util.Stream;
import king.org.util.search.InstructionSearcher;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/* The Client class is the largest class within the Runescape client.
 * It is also the most important class, and therefore contains the most
 * fields and methods out of any of the other classes combined.
 * 
 * Therefore, everything within this transform will be heavily doccumented
 * in order to prevent it from becoming one large chaotic script.
 * 
 * identify(): This is where we find client.class (pretty easy task).
 * manipulate(): This is where we hook all fields and methods
 */

public class Client extends Transform {
    public Client(Updater i) {
        super(i);
    }

    @Override
    public ClassNode identify(Collection<ClassNode> classNodes) {
        for (ClassNode classNode : classNodes) {
        	 /* Client is 6 letters long
        	  * Looks for the class named Client regardless of its package location
        	  */
            if (classNode.name.substring(classNode.name.length() - 6, classNode.name.length()).equalsIgnoreCase("Client")) {
            	Updater.rawClasses.put(classNode.name, classNode);
            	return classNode;
            }
        }
        return null;
    }

    @Override
    public ClassNode manipulate(ClassNode classNode) {
    	
    	/* HOOKING FIELDS
    	 */
    	
    	//loggedIn: Public boolean | Requires processGameLoop();
    	for (MethodNode mn : (List<MethodNode>) classNode.methods) {
    		if (mn.name.equals("processGameLoop")) {
    			
    			InstructionSearcher is = new InstructionSearcher(mn);
    			AbstractInsnNode current = is.getCurrent();
    			int similarity = 0;
    			ArrayList<Integer> searchCriteria = new ArrayList<Integer>(Arrays.asList(Opcodes.PUTSTATIC, Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.IFNE,
    					Opcodes.ALOAD, Opcodes.INVOKESPECIAL, Opcodes.GOTO));
    			
    			//Navigate to block of bytecode we want to analyse
    			while (current != null && current.getOpcode() != Opcodes.IADD) {
    				current = is.getNext();
    			}
    			
    			current = is.getNext();
    			
    			for (int i = 0; i < searchCriteria.size(); i++) {
    				if (current != null) {
    					if (current.getOpcode() == searchCriteria.get(i)) {
    						similarity++;
    					}
    				}
    				current = is.getNext();
    			}
    			if (similarity / searchCriteria.size() > 0.9) {
    				String fName = ((FieldInsnNode)is.getPrevious(Opcodes.GETFIELD)).name;
    				for (FieldNode fn : (List<FieldNode>) classNode.fields) {
    					
    					// We found the loggedIn field!
    					if (fn.name.equals(fName)) {
    						Updater.oldFieldNames.put("isLoggedIn", fn.name);
    						fn.name = "isLoggedIn";
    					}
    				}
    			}
    		}
    	}
    	
    	//openInterfaceID: Public static | requires drawOnBankInterface();
    	for (MethodNode mn : (List<MethodNode>) classNode.methods) {
    		if (mn.name.equals("drawOnBankInterface")) {
    			String fName = ((FieldInsnNode)mn.instructions.getFirst()).name;
    			for (FieldNode fn : (List<FieldNode>) classNode.fields) {
    				if (fn.name.equals(fName)) {
    					Updater.oldFieldNames.put("getOpenInterfaceID", fn.name);
    					fn.name = "getOpenInterfaceID";
    				}
    			}
    		}
    	}
    	
    	//baseX AND baseY: Private int | requires doAction();
    	/* We are going to be looking at the clickId == 234 section
    	 * (SIPUSH 234)
    	 * of the doAction() method. This contains the doWalkTo() method as well
    	 * as plenty of other important fields
    	 */
    	for (MethodNode mn : (List<MethodNode>) classNode.methods) {
    		if (mn.name.equals("doAction")) {
    			InstructionSearcher is = new InstructionSearcher(mn);
    			AbstractInsnNode current = is.getNext(Opcodes.SIPUSH);
    			
    			// Looks for SIPUSH 234
    			while (((IntInsnNode)current).operand != 234) {
    				/*
    				 * 
    				 * HOOK ALL OF THE FILEDS AND METHODS WITH SIPUSH 234
    				 * THE METHOD STRUCTURE IS IDENTICAL IN OBBED AND DEOBBED
    				 * 
    				 * 
    				 */
    			}
    			
    			//System.out.println(((IntInsnNode)current).operand);
    		}
    	}
    	
    	
    	/* HOOKING METHODS
    	 */
    	
    	//doAction(): private void |
    	/* !RECENT DISCOVERY!
    	 * doAction() is a very powerful way of hooking fields since its a method
    	 * composed of a whole bunch of if statements checking for clickID's. These
    	 * ID's don't change through obfuscation, so all we need to do is look up
    	 * the ID that the field sits in and read the bytecode from within that case.
    	 */
    	for (MethodNode mn : (List<MethodNode>) classNode.methods) {
    		if (mn.instructions.size() > 3000) {
    			if (mn.desc.equals("(I)V")) {
    				System.out.println(mn.name);
    				Updater.oldMethodNames.put("doAction", mn.name);
    				mn.name = "doAction";
    			}
    		}
    	}
    	
    	
    	//drawOnBankInterface(): Public void |
    	for (MethodNode mn : (List<MethodNode>) classNode.methods) {
    		if (mn.desc.equals("()V") && mn.access == 0x0001) {
    			
    			int similarity = 0;
    			ArrayList<Integer> search = new ArrayList<Integer>(Arrays.asList(Opcodes.GETSTATIC, Opcodes.SIPUSH, Opcodes.IF_ICMPNE));
    			InstructionSearcher is = new InstructionSearcher(mn);
    			AbstractInsnNode current = is.getCurrent();
    			
    			for (int i = 0; i < search.size(); i++) {
    				if (current != null) {
    					if (search.get(i) == current.getOpcode()) {
    						similarity++;
    					}
    					current = is.getNext();
    				}
    			}
    			
    			if (similarity / search.size() > 0.9) {
    				Updater.oldMethodNames.put("drawOnBankInterface", mn.name);
    				mn.name = "drawOnBankInterface";
    			}
    		}
    	}
    	
    	//processGameLoop(): Public void |
    	for (MethodNode mn : (List<MethodNode>) classNode.methods) {
    		if (mn.desc.equals("()V") && mn.access == 0x0001) {
    			
    			/* We are going to be looking for the 3 condition if-statement,
    			 * which in bytecode looks like a collection of IFNE and IFEQ
    			 */
    			InstructionSearcher is = new InstructionSearcher(mn);
    			AbstractInsnNode current = is.getCurrent();
    			int similarity = 0;
    			ArrayList<Integer> conditionalStatement = new ArrayList<Integer>(Arrays.asList(Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.IFNE,
    																		 Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.IFNE, 
    																		 Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.IFEQ, Opcodes.RETURN));
    			
    			for (int i = 0; i < conditionalStatement.size(); i++) {
    				if (conditionalStatement.get(i) != null && current != null) {
    					if (current.getOpcode() == conditionalStatement.get(i)) {
        					similarity++;
        				}
        				current = is.getNext();
    				}
    			}
    			
    			/* We found the method. Now modify it
    			 */
    			if (similarity / conditionalStatement.size() == 1) {
    				Updater.oldMethodNames.put("processGameLoop", mn.name);
    				mn.name = "processGameLoop";
    			}
    			
    		}
    	}
    	
        return classNode;
    }
}
