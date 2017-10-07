package src.main.org.bot.client;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import src.main.org.bot.api.core.injection.Instance;
import src.main.org.bot.client.logger.Logger;
import src.main.org.bot.client.ui.UI;
import src.main.org.updater.UpdaterManager;

public class Main {
	public static Font customFont;
	
	public static String version = "b003";
	public static String timeStamp = ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);
	public static String hookFileName = "Hooks";
	public static Instance game;
	private static String injectedClient = "file:///C:/Users/JBerben/Documents/Programming/RSPS/ReflectionClient/KingClient/injectedClient.jar";
	private static boolean run = true;
	public static HashMap<String, String> classes = new HashMap<String, String>();
	public static HashMap<String, String> fields = new HashMap<String, String>();
	
    public static void main(String[] args) {
    	setupFont();
    	if (run) {
        	UI Frame = new UI();
            Frame.addBot(new Bot());
            Frame.setVisible(true);
            Frame.pack();
            Frame.revalidate();
        } else {
        	
        }
    }
    
    private static void setupFont() {
    	InputStream fontLocation = Main.class.getClassLoader().getResourceAsStream("Dense-Regular.ttf");
    	GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
			//ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, fontLocation));
			customFont = Font.createFont(Font.TRUETYPE_FONT, fontLocation);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
}
