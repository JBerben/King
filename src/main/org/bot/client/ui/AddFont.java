package src.main.org.bot.client.ui;

import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

public class AddFont {
	
	private static Font ttfBase = null;
	private static Font custom = null;
	private static InputStream stream = null;
	private static final String FONT_PATH = "src/main/resources/Dense-Regular.ttf";

	public static Font createFont() {
				
		try {
			stream = new BufferedInputStream(new FileInputStream(FONT_PATH));
			ttfBase = Font.createFont(Font.TRUETYPE_FONT, stream);
			custom = ttfBase.deriveFont(Font.PLAIN, 24);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return custom;
	}
	
}
