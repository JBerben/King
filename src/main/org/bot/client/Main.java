package src.main.org.bot.client;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import src.main.org.bot.api.core.injection.Instance;
import src.main.org.updater.UpdaterManager;

public class Main {
	public static String timeStamp = ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);
	private static String hookFileName = "Hooks";
	public static Instance game;
	private static String injectedClient = "file:///C:/Users/JBerben/Documents/Programming/RSPS/ReflectionClient/KingClient/injectedClient.jar";
	private static boolean run = true;
	public static HashMap<String, String> classes = new HashMap<String, String>();
	public static HashMap<String, String> fields = new HashMap<String, String>();
	
    public static void main(String[] args) {
        if (run) {
        	UpdaterManager.UpdateHooks(hookFileName);
        	UI Frame = new UI();
            Frame.addBot(new Bot());
            Frame.setVisible(true);
            Frame.pack();
            Frame.revalidate();
        } else {
        	
        }
    }
}
