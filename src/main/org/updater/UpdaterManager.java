package src.main.org.updater;

import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.objectweb.asm.tree.ClassNode;

import src.main.org.updater.util.JarStream;
import src.main.org.updater.util.Stream;
import src.main.org.updater.xml.XMLWriter;

/*
 * In other updater files, anything obfuscated will be mentioned as "dirty",
 * and anything unobfuscated will be referred to as "clean".
 */

public class UpdaterManager {

	public static String timeStamp = ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);
	
	public static void UpdateHooks(String hookFileName) {
		Stream.log("Initializing updater... | " + timeStamp);
		new Updater(Updater.clean, false);

		Stream.log("Creating " + hookFileName + ".xml... | " + timeStamp);
		XMLWriter.CreateFile(hookFileName + ".xml");
		
		Stream.log("Finished updating hooks! | " + UpdaterManager.timeStamp);
	}
	
}