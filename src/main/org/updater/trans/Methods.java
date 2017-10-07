package src.main.org.updater.trans;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import src.main.org.bot.client.ui.UI;
import src.main.org.updater.Updater;
import src.main.org.updater.UpdaterManager;
import src.main.org.updater.obj.GameMethod;
import src.main.org.updater.patterns.BytecodeAnalysis;

/*
 * Is used to compare methods of the clean and dirty classes
 * to each other. This transform finds the most likely method matches,
 * and then maps the dirty methods up with the clean methods.
 */
public class Methods extends Transform {
	
	private static int numMethods;
	
	public Methods(Updater i) {
		super(i);
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, ClassNode> identify(Collection<ClassNode> classNodes) {
		
		UpdaterManager.status = "Finding Methods...";

		/*
		 * Find size
		 */
		for (ClassNode cn : Updater.cleanClasses.values()) {
			numMethods += cn.methods.size();
		}
		
		HashMap<String, GameMethod> processedMethods = new HashMap<String, GameMethod>();
		Collection<ClassNode> cleanClasses = Updater.cleanClasses.values();

		for (ClassNode cleanClass : cleanClasses) {
			for (MethodNode cleanMethod : (List<MethodNode>) cleanClass.methods) {

				UpdaterManager.status = "Hooking Method: " + cleanMethod.name;
				
				/*
				 * gameMethods: A hashmap of GameMethods and their similarity rating.
				 * dirtyClass: The bestmatched dirty class with the current clean class.
				 * match: Our best match.
				 */
				HashMap<GameMethod, Float> gameMethods = new HashMap<GameMethod, Float>();
				ClassNode dirtyClass = Updater.processedClasses.get(cleanClass.name);
				GameMethod match = new GameMethod();
				match.setClassNode(dirtyClass);
				match.setName(cleanMethod.name);

				/*
				 * Now that we have all classes hooked, we are going to modify method
				 * decriptiors to contain proper class names as their objects
				 */
				if (dirtyClass != null) {
					for (MethodNode dirtyMethod : (List<MethodNode>) dirtyClass.methods) {
						
						match = new GameMethod();
						match.setClassNode(dirtyClass);
						match.setName(cleanMethod.name);
						match.setDesc(dirtyMethod.desc);
						match.setMethodNode(dirtyMethod);

						/*
						 * Method Bytecode Analyzer
						 */
						match.bytecodeSim = BytecodeAnalysis.analyze(cleanMethod, dirtyMethod);

						/*
						 * Store all dirty methods for comparison against 1
						 * clean method
						 */
						gameMethods.put(match, match.getSimilarity());
					}

					// Cycle through the HashMap of methods to find the best
					// matched
					for (HashMap.Entry<GameMethod, Float> entry : gameMethods.entrySet()) {
						if (entry.getValue() > match.getSimilarity()) {
							match = entry.getKey();
						}
					}

					// Puts the matched method in our HashMap of matched classes
					if (match.getMethodNode() != null) {
						//System.out.println("Trying to match: Class: " + cleanClass.name + ":" + dirtyClass.name + ": "
						//		+ match.getName() + " --> " + match.getMethodNode().name + " | "
						//		+ match.getSimilarity());
					}
					UpdaterManager.progress += (100 / numMethods) * 0.3f;
					UI.sl.UpdateProgress(UpdaterManager.progress, UpdaterManager.status);
					processedMethods.put(match.getClassNode().name, match);
				}

			}
		}
		Updater.processedGameMethods = processedMethods;
		
		// TEMPORARY
		return Updater.processedClasses;
	}
	
	// TODO: Finish off this method
	private String updateMethodDesc(MethodNode m) {
		String desc = m.desc;
		Pattern pattern = Pattern.compile("[BCDFISZ]L.*?;");
		Matcher matcher = pattern.matcher(desc);

		if (matcher.find()) {
			
		}
		
		if (Updater.processedClasses.keySet().contains(desc)) {
			
		}
		return null;
	}

	@Override
	public HashMap<String, ClassNode> manipulate(Collection<ClassNode> classNodes) {
		// TODO Auto-generated method stub
		//HashMap<String, ClassNode> classes = new HashMap<String, ClassNode>();
		//classes = Updater.processedClasses;
		return Updater.processedClasses;
	}

	/*
	 * Returns all of the class methods :)
	 */

}
