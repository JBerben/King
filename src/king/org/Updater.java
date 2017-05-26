package king.org;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import king.Application;
import king.org.trans.*;
import king.org.trans.Character;
import king.org.util.JarStream;
import king.org.util.Stream;
import king.org.util.search.Finder;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarFile;

public class Updater {
	// oldFieldNames(newName, oldName);
	public static HashMap<String, String> oldFieldNames = new HashMap<String, String>();
	public static HashMap<String, String> oldMethodNames = new HashMap<String, String>();
	
	public static HashMap<String, ClassNode> classes = new HashMap<String, ClassNode>();
	public static LinkedHashMap<String, ClassNode> processedClasses = new LinkedHashMap<String, ClassNode>();
	public static LinkedHashMap<String, ClassNode> rawClasses = new LinkedHashMap<String, ClassNode>();
	public static HashMap<ClassNode, FieldNode> processedFields = new HashMap<ClassNode, FieldNode>();
	public static HashMap<ClassNode, MethodNode> processedMethods = new HashMap<ClassNode, MethodNode>();
	private LinkedList<Transform> transforms = new LinkedList<Transform>();
	
	
	private String gamePackURL = "C:\\Users\\JBerben\\Documents\\Programming\\RSPS\\ReflectionClient\\KingUpdater\\src\\client.jar";
	private String testURL = "C:\\Users\\JBerben\\Documents\\Programming\\RSPS\\Tools\\ObbedClient - Nr.jar";
	
	public Updater(String url) {
		gamePackURL = url;
		setup();
		execute();
	}

	public Updater() {
		setup();
		execute();
	}

	
	private final void setup() {
		try {
			JarFile jar = new JarFile(gamePackURL);
			classes = new JarStream().getRSPSContents(jar);
			transforms.add(new Node(this));
			transforms.add(new NodeCache(this));
			transforms.add(new NodeSub(this));
			transforms.add(new Animable(this));
			transforms.add(new Entity(this));
			transforms.add(new Player(this));
			// Entity and Character are same class
			// transforms.add(new Character(this));
			transforms.add(new NPC(this));
			transforms.add(new EntityDef(this));
			transforms.add(new Model(this));
			transforms.add(new RSApplet(this));
			transforms.add(new Item(this));
			transforms.add(new RSInterface(this));
			transforms.add(new Client(this));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Executes transforms in order
	 */
	private final void execute() {
		for (Transform transform : transforms) {
			// Runs all the classes through the identify functions
			
			ClassNode classNode = transform.identify(classes.values());
			
			transform.manipulate(classNode);
			if (classNode != null) {
				
				processedClasses.put(transform.getClass().getSimpleName(), classNode);
				
				//Stream.log(transform.getClass().getSimpleName() + ": " + classNode.name);
				
			} else {
				//Stream.log(transform.getClass().getSimpleName() + ": " + "NULL");
			}
		}
		Stream.log("Finished! | " + Application.timeStamp);
	}

	public String getClassName(String input) {
		if (processedClasses.containsKey(input)) {
			String pro = processedClasses.get(input).name;
			return pro;
		}
		return "null";
	}

	public ClassNode getClassNode(String input) {
		if (processedClasses.containsKey(input)) {
			ClassNode node = processedClasses.get(input);
			return node;
		}
		return null;
	}

	public ClassNode getUnprocessedNode(String input) {
		if (classes.containsKey(input)) {
			ClassNode node = classes.get(input);
				return node;
		}
		return null;
	}

	public FieldNode getField(String nodeName, String fieldName) {
		FieldNode fn = null;
		if (processedClasses.get(nodeName) != null) {
			for (int i = 0; i < processedClasses.get(nodeName).fields.size(); i++) {
				fn = (FieldNode) processedClasses.get(nodeName).fields.get(i);
				if (fn.name.equals(fieldName)) {
					return fn;
				}
			}
		}
		return null;
	}
}
