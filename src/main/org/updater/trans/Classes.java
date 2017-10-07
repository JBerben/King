package src.main.org.updater.trans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import src.main.org.bot.client.ui.UI;
import src.main.org.updater.Updater;
import src.main.org.updater.UpdaterManager;
import src.main.org.updater.obj.GameClass;
import src.main.org.updater.obj.GameMethod;
import src.main.org.updater.patterns.BytecodeAnalysis;
import src.main.org.updater.util.Maths;

/*
 * Looks at all clean and dirty classes and compares certain aspects of 
 * them to each other to solve which dirty class is most likely to be a match
 * with a clean class.
 */
public class Classes extends Transform {
	
	public Classes(Updater i) {
		super(i);
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, ClassNode> identify(Collection<ClassNode> cleanNodes) {
		
		UpdaterManager.status = "Finding classes...";
		
		// To rename dirty classes
		HashMap<String, String> classNameTable = new HashMap<String, String>();
		
		Collection<ClassNode> dirtyNodes = Updater.dirtyClasses.values();
		HashMap<String, ClassNode> matchedClasses = new HashMap<String, ClassNode>();

		// Cycles through clean classes whilst comparing with dirty
		for (ClassNode clean : cleanNodes) {
			
			UpdaterManager.status = "Hooking Class: " + clean.name;

			GameClass match = new GameClass();
			GameMethod cleanMethod = new GameMethod(clean);

			if (clean != null) {
				HashMap<GameClass, Float> gameClasses = new HashMap<GameClass, Float>();

				for (ClassNode dirty : dirtyNodes) {
					
					match = new GameClass();
					GameMethod dirtyMethod = new GameMethod(clean);
					float currentFS = 0.0f;
					float currentMS = 0.0f;

					/*
					 * File size analyzer Reads from file size hashmaps in
					 * Updater class which are updated in jarstream class
					 */
					long cSize = Updater.cleanClassSizes.get(clean.name);
					long dSize = Updater.dirtyClassSizes.get(dirty.name);
					float sim = Maths.getRatio(cSize, dSize);

					if (dirty != null && sim > 0.2) {

						/*
						 * Field and method count analysis
						 */
						if (Math.min(clean.fields.size(), dirty.fields.size()) > 0) {
							currentFS = (float) Math.min(clean.fields.size(), dirty.fields.size())
									/ (float) Math.max(clean.fields.size(), dirty.fields.size());
						} else {
							currentFS = 0.0f;
						}

						if (Math.min(clean.methods.size(), dirty.methods.size()) > 0) {
							currentMS = (float) Math.min(clean.methods.size(), dirty.methods.size())
									/ (float) Math.max(clean.methods.size(), dirty.methods.size());
						} else {
							currentMS = 0.0f;
						}

						match.methodSim = currentMS;
						match.fieldSim = currentFS;
						match.setNode(dirty);
						match.setName(clean.name);

						/*
						 * Superclass analysis Just a quick check to see if a
						 * class has a super. (Just for a tiny bit of added
						 * accuracy). If it doesn't, subtract 0.5 from
						 * similarity
						 * 
						 * NOTE: added a count checker
						 */
						if (clean.superName.length() > 0 && dirty.superName.length() > 0) {

							int cleanSupers = 1;
							int dirtySupers = 1;

							String cleanSuperName = clean.superName.replace("/", ".");
							String dirtySuperName = dirty.superName;

							ClassNode cleanSuper = Updater.cleanClasses.get(cleanSuperName);
							ClassNode dirtySuper = Updater.dirtyClasses.get(dirty.superName);
							while (cleanSuper != null && cleanSuper.superName != null
									&& !cleanSuper.superName.contains("java")) {
								cleanSuper = Updater.cleanClasses.get(cleanSuperName);
								cleanSuperName = cleanSuper.superName.replace("/", ".");
								cleanSupers++;
							}
							while (dirtySuper != null && dirtySuper.superName != null
									&& !dirtySuper.superName.contains("java")) {
								dirtySuper = Updater.dirtyClasses.get(dirtySuperName);
								dirtySuperName = dirtySuper.superName;
								dirtySupers++;
							}

							if (cleanSupers == dirtySupers) {
								match.superSim = 1;
							}

							// Blatant check
							if (clean.superName.contains("java") && dirty.superName.contains("java")) {
								// Helps finding RSFrame for clients that
								// use swing and some that use fx
								if (clean.superName.contains("Frame") && dirty.superName.contains("Frame")) {
									match.superSim = 1;
								} else if (clean.superName.equals(dirty.superName)) {
									match.superSim = 1;
								} else {
									// DEFINITELY NOT THE RIGHT CLASS
									match.superSim = -7;
								}
							}
						}

						if (!dirty.superName.equals("java/lang/Object")
								&& !clean.superName.equals("java/lang/Object")) {
							match.hasSuperMatch = true;
						} else {
							match.hasSuperMatch = false;
						}

						/*
						 * Interface/Implements analyzer
						 */
						if (dirty.interfaces.size() > 0 && clean.interfaces.size() > 0) {
							if (dirty.interfaces.get(0).equals(clean.interfaces.get(0))) {
								match.interfaceMatch = 1;
							}
						}
						if (dirty.interfaces.size() != clean.interfaces.size()) {
							match.interfaceMatch = -1;
						}

						/*
						 * Field Analyzer
						 */
						ArrayList<String> cleanFD = new ArrayList<String>();
						ArrayList<String> dirtyFD = new ArrayList<String>();

						for (FieldNode cfn : (List<FieldNode>) clean.fields) {
							if (cfn.desc.startsWith("L")) {
								// Testing something new
								//cfn.desc = "L";
							}
							cleanFD.add("L");
						}

						for (FieldNode dfn : (List<FieldNode>) dirty.fields) {
							if (dfn.desc.startsWith("L")) {
								// Testing something new
								//dfn.desc = "L";
							}
							dirtyFD.add("L");
						}

						Collections.sort(cleanFD);
						Collections.sort(dirtyFD);

						if (dirtyFD.equals(cleanFD)) {
							match.fieldDescSim = 1.0f;
						} else {
							// needs some work
							match.fieldDescSim = 0.0f;
						}

						/*
						 * Method description analyzer AND bytecode analyzer
						 */
						float totalBytecodeSim = 0f;
						float bytecodeSim = 0f;
						cleanMethod.setClassNode(clean);
						dirtyMethod.setClassNode(dirty);

						for (MethodNode cmn : (List<MethodNode>) clean.methods) {
							
							// Using these values later on in method analysis
							List<String> vals = new ArrayList<String>();
							vals.add(clean.name);
							Updater.methodUsage.put(clean.name + "/" + cmn.name, vals);
							
							for (MethodNode dmn : (List<MethodNode>) dirty.methods) {

								// Quick check. Will be done in more depth in
								// the methods transform
								if (BytecodeAnalysis.analyze(cmn, dmn) > bytecodeSim) {
									bytecodeSim = BytecodeAnalysis.analyze(cmn, dmn);
								}

								if (Maths.getRatio(clean.methods.size(), dirty.methods.size()) > 0.6f) {
									//Updater.test.put(dmn.name, value)
									if (getDescEquality(dmn.desc, cmn.desc) == 1.0f) {
										match.descSimCount++;
									}
								}
							}
							totalBytecodeSim += bytecodeSim;
						}
						match.bytecodeSim = totalBytecodeSim / clean.methods.size();
						match.descSim = Maths.getRatio(match.descSimCount, clean.methods.size());

						/*
						 * Getting a ratio of the two similarities and adding it
						 * into a hashmap to parse later.
						 * 
						 * These ratios have been made up and are subject to
						 * change. Methodsimilarity is more indicative than
						 * fields as far as I know though.
						 */
						gameClasses.put(match, match.getSimilarity());
					}
				}

				// Cycle through the HashMap of classes to find the best matched
				for (HashMap.Entry<GameClass, Float> entry : gameClasses.entrySet()) {
					if (entry.getValue() > match.getSimilarity()) {
						match = entry.getKey();
					}
				}

				// Puts the matched class in our HashMap of matched classes
				if (match.getNode() != null) {
					classNameTable.put(match.getNode().name, match.getName());
					if (!match.getName().contains(match.getNode().name)) {
					//	System.out.println("Trying to match: " + match.getName() + " --> " + match.getNode().name
					//			+ " | " + match.getSimilarity());
					}
					UpdaterManager.progress += (100 / cleanNodes.size()) * 0.3f;
					UI.sl.UpdateProgress(UpdaterManager.progress, UpdaterManager.status);
					matchedClasses.put(match.getName(), match.getNode());	
					Updater.methodNameset.put(match.getNode().name, match.getName());
				}
				

			}

		}
		
		// Returns out HashMap of matched classes
		return matchedClasses;

	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, ClassNode> manipulate(Collection<ClassNode> classNodes) {
		/*
		 * Modify ClassNode method descriptors for next part of analyzing
		 * 
		 * Usage for nameset hashmap: <OldClassName, NewClassName>
		 */
		HashMap<String, ClassNode> classes = new HashMap<String, ClassNode>();
		for (ClassNode cn : classNodes) {
			for (MethodNode mn : (List<MethodNode>) cn.methods) {
				for (String desc : Updater.methodNameset.keySet()) {
					if (mn.desc.contains(desc)) {
						mn.desc = mn.desc.replaceAll(desc, Updater.methodNameset.get(desc));
					}
				}
			}
			//System.out.println("Put " + Updater.methodNameset.get(cn.name) + " as key and " + cn.name + " as node");
			classes.put(Updater.methodNameset.get(cn.name), cn);
			Updater.processedParsedClasses.put(Updater.methodNameset.get(cn.name), cn.name);
			cn.name = Updater.methodNameset.get(cn.name);
		}
		
		/*
		 * Console Output
		 */
		
		
		return classes;
	}

	// Changes any obfuscated object name into java/lang/Object for easy
	// comparison
	public static String parseObjectDesc(String desc) {
		Pattern pattern = Pattern.compile("[BCDFISZ]L.*?;");
		Matcher matcher = pattern.matcher(desc);

		if (matcher.find()) {
			desc = desc.replaceAll(pattern.toString(), desc.charAt(matcher.start() - 1) + "L;");
		}
		return desc;
	}
	
	// Returns a percentage value of how similar two method descriptors are
	public static float getDescEquality(String desc, String other) {
		desc = Classes.parseObjectDesc(desc);
		other = Classes.parseObjectDesc(other);
		ArrayList<String> descriptors = new ArrayList<String>();
		ArrayList<String> otherDescriptors = new ArrayList<String>();
		Pattern pattern = Pattern.compile("\\[+[BCDFISZL]|[BCDFISZL]|\\)l|V");
		Matcher descMatcher = pattern.matcher(desc);
		Matcher otherMatcher = pattern.matcher(other);

		while (descMatcher.find()) {
			try {
				descriptors.add(desc.substring(descMatcher.start(), descMatcher.end()));
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}

		while (otherMatcher.find()) {
			try {
				otherDescriptors.add(other.substring(otherMatcher.start(), otherMatcher.end()));
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}

		Collections.sort(descriptors);
		Collections.sort(otherDescriptors);

		if (otherDescriptors.equals(descriptors) && otherDescriptors.size() == descriptors.size()) {
			return 1.0f;
		} else {
			return 0.0f;
		}
	}
}
