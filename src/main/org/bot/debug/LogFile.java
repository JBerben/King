package src.main.org.bot.debug;

import java.io.IOException;
import java.io.PrintWriter;

public class LogFile {

	public static void WriteLog(String fileName) {
		try{
		    PrintWriter writer = new PrintWriter(fileName + ".txt", "UTF-8");
		    writer.println("The first line");
		    writer.close();
		} catch (IOException e) {
		   // do something
		}
	}
	
}
