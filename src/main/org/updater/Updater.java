package src.main.org.updater;

import org.objectweb.asm.tree.ClassNode;

import src.main.org.updater.obj.GameField;
import src.main.org.updater.obj.GameMethod;
import src.main.org.updater.trans.Classes;
import src.main.org.updater.trans.Fields;
import src.main.org.updater.trans.Methods;
import src.main.org.updater.trans.Transform;
import src.main.org.updater.util.Debug;
import src.main.org.updater.util.JarStream;
import src.main.org.updater.util.Stream;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

public class Updater {

	// Testing
	public static HashMap<String, ClassNode> test = new HashMap<String, ClassNode>();

	/*
	 * HashMaps containing unobfuscated and obfuscated game classes
	 * <unobfuscatedClassName, classNode>
	 */
	public static HashMap<String, ClassNode> cleanClasses = new HashMap<String, ClassNode>();
	public static HashMap<String, ClassNode> dirtyClasses = new HashMap<String, ClassNode>();

	/*
	 * Modified versions of obfuscated classes. Parsed classes have their
	 * descriptors and names modified so that they can be reflected/injected
	 * easier
	 * 
	 * processedClasses: <unobfuscatedClassName, classNode>
	 * processedParsedClasses: <unobfuscatedClassName, obfuscatedClassName>
	 */
	public static HashMap<String, ClassNode> processedClasses = new LinkedHashMap<String, ClassNode>();
	public static LinkedHashMap<String, String> processedParsedClasses = new LinkedHashMap<String, String>();

	/*
	 * Hooked fields matchedFields: <unobfuscatedGameField, obfuscatedGameField>
	 */
	public static HashMap<GameField, GameField> matchedFields = new HashMap<GameField, GameField>();

	/*
	 * GameMethods that have been modified processedGameMethods:
	 * <unobfuscatedMethodName, obfuscatedGameMethod>
	 */
	public static HashMap<String, GameMethod> processedGameMethods = new HashMap<String, GameMethod>();

	/*
	 * A way of linking old method names to new method names methodNameset:
	 * <unobfuscated, obfuscated>
	 */
	public static HashMap<String, String> methodNameset = new HashMap<String, String>();

	/*
	 * Is used to determine how often and where each field is used
	 */
	public static HashMap<String, List<String>> methodUsage = new HashMap<String, List<String>>();

	/*
	 * Transformers. These are used to analyze classes and find
	 * fields/methods/classes
	 */
	private LinkedList<Transform> transforms = new LinkedList<Transform>();

	/*
	 * TODO: Remove these constants. Were used for debugging.
	 */
	public static String dirty = "C:\\Users\\JBerben\\Documents\\Programming\\RSPS\\Tools\\Client.jar";
	public static String dirty2 = "C:\\Users\\JBerben\\Documents\\Programming\\RSPS\\Tools\\testworld-client.jar";
	public static String dirty3 = "C:\\Users\\JBerben\\Documents\\Programming\\RSPS\\Tools\\SoulSplit.jar";
	public static String clean = "C:\\Users\\JBerben\\Documents\\Programming\\RSPS\\Tools\\Galkons.jar";

	/*
	 * File sizes of different classes. Used in class analysis
	 */
	public static HashMap<String, Long> cleanClassSizes = new HashMap<String, Long>();
	public static HashMap<String, Long> dirtyClassSizes = new HashMap<String, Long>();

	// TODO: Remove this test constructor
	public Updater(String url, boolean debug) {

		setup();

		if (debug) {
			Debug d = new Debug();
			d.debugFunctions();
		} else {
			execute();
		}
	}

	public Updater() {
		setup();
		execute();
	}

	/*
	 * Reads classes from client jars and stores them in HashMaps
	 */
	private final void setup() {
		try {
			JarFile cleanJar = new JarFile(clean);
			JarFile dirtyJar = new JarFile(dirty);

			Stream.log("Fetching JarFile.");
			dirtyClasses = new JarStream().getRSPSContents(dirtyJar);
			dirtyClassSizes = cleanClassSizes;
			cleanClasses = new JarStream().getRSPSContents(cleanJar);
			processedClasses = cleanClasses;
			Stream.log("Fetched JarFile and extracted classes.");

			transforms.add(new Classes(this));
			transforms.add(new Methods(this));
			transforms.add(new Fields(this));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Executes transforms in order
	 */
	private final void execute() {

		Stream.log("Deobfuscation started. \n\n");

		for (Transform transform : transforms) {

			// Classes -> Methods -> Fields
			processedClasses = transform.identify(processedClasses.values());
			processedClasses = transform.manipulate(processedClasses.values());
		}

		// Parses classes, making them injectable/reflectable. Also generates
		// console output.
		Iterator<Map.Entry<String, String>> it = processedParsedClasses.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();

			// Class Name
			Stream.log(entry.getKey() + " :: " + entry.getValue());
			Stream.log("----------------------------------------------------------------");

			// Fields
			for (GameField gf : matchedFields.keySet()) {
				if (gf.getAcessor().name.equals(entry.getKey())) {
					Stream.log(gf.getName() + " --> " + matchedFields.get(gf).getName());
				}
			}

			UpdaterManager.status = "Hooking Complete!";
			Stream.log("", "\n\n");

			if (entry.getValue() != null) {
				processedParsedClasses.put(entry.getKey(), entry.getValue().replaceAll("/", "."));
			}
		}
	}
}
