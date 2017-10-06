package src.main.org.bot.client.logger;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger extends JTextArea {
	private static JTextArea logArea;

	public static void write(final String str) {
		logArea.append("[N/A] " + str + "\n");
	}
	
	public static void writeConsole(final String str) {
		logArea.append("[CONSOLE] " + str + "\n");
	}

	public static void write(final String str, final LogType type) {
		logArea.append("[" + type.getType() + "] " + str + "\n");
	}

	public static void writeException(final String str) {
		logArea.append("[N/A][EXCEPTION] " + str + "\n");
	}

	public static void writeException(final String str, final LogType type) {
		logArea.append("[" + type.getType() + "][EXCEPTION] " + str + "\n");
	}

	public static void writeWarning(final String str) {
		logArea.append("[N/A][WARNING] " + str + "\n");
	}

	public static void writeWarning(final String str, final LogType type) {
		logArea.append("[" + type.getType() + "][WARNING] " + str + "\n");
	}

	public Logger() {
		super(8, 5);
		super.setBackground(Color.WHITE);
		logArea = this;
		setEditable(true);
		setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		setLineWrap(true);
		final DefaultCaret caret = (DefaultCaret) getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	}

    private static String getTime() {
        Date date = new Date();
        SimpleDateFormat d = new SimpleDateFormat("hh:mm a");
        return d.format(date).toString();
    }
}
