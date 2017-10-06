package src.main.org.bot.client.logger;

import javax.swing.*;
import java.awt.*;

public class LoggerPanel extends JPanel {
	private final JScrollPane scrollPane;

	public LoggerPanel(final Logger logger) {
		scrollPane = new JScrollPane(logger, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}
}
