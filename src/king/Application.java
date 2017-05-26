package king;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import king.org.Updater;
import king.org.util.Stream;
import king.org.util.TextWriter;
import king.xml.*;

public class Application {

	public static String timeStamp = ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);
	private static String hookFileName = "Hooks";
	
    public static void main(String...args){
       Stream.log("Initializing updater... | " + timeStamp);
       new Updater();
       
       Stream.log("Creating " + hookFileName + ".xml... | " + timeStamp);
       XMLWriter.CreateFile(hookFileName + ".xml");
       
       //Stream.log("SPECIAL CASE: Creating data file");
       //TextWriter.Write("expectedFields.txt");
    }
}