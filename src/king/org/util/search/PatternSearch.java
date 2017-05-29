package king.org.util.search;

import java.util.List;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

/* A small side class that contains functions to search for
 * particular fields or methods.
 */

public class PatternSearch {
	
	/* Searches for a particular field name within a particular class.
	 * Only useful once you've identified names, this function
	 * is mainly used to reduce code size and improve cleanliness
	 */
	public static FieldNode FindField(String fieldName, ClassNode c) {
		for (FieldNode fn : (List<FieldNode>) c.fields) {
			if (fn.name.equals(fieldName)) {
				return fn;
			}
		}
		return null;
	}
	
	/* Searches for a particular method name within a particular class.
	 * Only useful once you've identified names, this function
	 * is mainly used to reduce code size and improve cleanliness
	 */
	public static MethodNode FindMethod(String fieldName, ClassNode c) {
		for (MethodNode mn : (List<MethodNode>) c.methods) {
			if (mn.name.equals(fieldName)) {
				return mn;
			}
		}
		return null;
	}
	
}
