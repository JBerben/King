package src.main.org.bot.client.ui;

import src.main.org.bot.api.core.Client;
import src.main.org.bot.client.Bot;
import src.main.org.bot.client.Console;
import src.main.org.bot.client.Main;
import src.main.org.bot.client.logger.LogType;
import src.main.org.bot.client.logger.Logger;
import src.main.org.bot.client.logger.LoggerPanel;
import src.main.org.bot.reflection.Integers;
import src.main.org.updater.UpdaterManager;

import static src.main.org.bot.utility.Sleep.sleep;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;

public class UI extends JFrame {
	
	final long serialVersionUID = 3437225280501554027L;
	private static final Dimension DEFAULT_SIZE = new Dimension(765, 503);
	private LoggerPanel loggerPanel;
	private JPanel contentPane;
	
	public static SetupLoader sl = new SetupLoader();
	
	URL url = ClassLoader.getSystemResource("src/main/resources/client_icon.png");
	Toolkit kit = Toolkit.getDefaultToolkit();
	Image img = kit.getImage(url);
	
	public UI() {
		this.setTitle("King | Version " + Main.version);
		this.setLayout(new BorderLayout());
		this.setMinimumSize(new Dimension(DEFAULT_SIZE.width + 6, DEFAULT_SIZE.height + 40 + 155));
		this.setSize(this.getMinimumSize());
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.getContentPane().setBackground(Color.BLACK);
		try {
			this.setIconImage(img);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		this.setCustomTheme(UIManager.getSystemLookAndFeelClassName());
		addComponents();
		centerFrame(this);
		this.setResizable(false);
		Logger.write("Initializing Client...", LogType.CLIENT);
		loadHooks();
	}
	
	public void loadHooks() {
		Logger.write("Hook Updater Started.", LogType.CLIENT);
		UI.this.add(sl);
		UI.this.revalidate();
		UI.this.pack();
		UpdaterManager.UpdateHooks(Main.hookFileName);
		UI.this.remove(sl);
	}

	public void addComponents() {
		JMenuBar menubar = new JMenuBar();
		JMenu menuLevel = new JMenu("Debug");

		JCheckBoxMenuItem levelOne = new JCheckBoxMenuItem("|TestCase|");
		JCheckBoxMenuItem levelTwo = new JCheckBoxMenuItem("Player Position");

		menubar.add(menuLevel);
		menuLevel.add(levelOne);
		menuLevel.add(levelTwo);
		setJMenuBar(menubar);

		JToggleButton b = new JToggleButton("Enable Logger");
		b.setSelected(true);
		b.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				switch (e.getStateChange()) {
				case ItemEvent.SELECTED:
					add(loggerPanel, BorderLayout.PAGE_END);
					UI.this.setMinimumSize(new Dimension(DEFAULT_SIZE.width + 6, DEFAULT_SIZE.height + 40 + 155));
					UI.this.setSize(UI.this.getMinimumSize());
					break;
				case ItemEvent.DESELECTED:
					remove(loggerPanel);
					UI.this.setMinimumSize(new Dimension(DEFAULT_SIZE.width + 6, DEFAULT_SIZE.height + 53));
					UI.this.setSize(UI.this.getMinimumSize());
					break;
				default:
					break;
				}
				Logger.write(Integer.toString(Client.getPlayerCount()));
				UI.this.repaint();
			}
		});

		JToggleButton playersOnline = new JToggleButton("Console");
		//playersOnline.setSelected(true);
		playersOnline.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				switch (e.getStateChange()) {
				case ItemEvent.SELECTED:
					try {
						//System.out.println(Players.getMyPlayer().getName() SS);
						Console frame = new Console();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					break;
				case ItemEvent.DESELECTED:
					break;
				default:
					break;
				}
				UI.this.repaint();
			}
		});

		menubar.add(b, BorderLayout.EAST);
		menubar.add(playersOnline, BorderLayout.WEST);
		loggerPanel = new LoggerPanel(new Logger());
		add(loggerPanel, BorderLayout.PAGE_END);
	}

	public void addBot(final Bot bot) {
		bot.loadBot();
		UI.this.add(bot.getApplet());
		UI.this.revalidate();
		UI.this.pack();

		UI.this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				UI.this.setVisible(false);
				bot.getLoader().destruct();
				UI.this.dispose();
				super.windowClosed(e);
			}
		});

		bot.getLoader().start();

		while (true) {
			if (bot != null && bot.getApplet().getComponentCount() > 0 && bot.getApplet() != null) {
				break;
			}
			sleep(50);
		}
	}

	public void setCustomTheme(String Theme) {
		try {
			UIManager.setLookAndFeel(Theme);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException Ex) {
			System.out.println(Ex.getMessage());
		}
	}

	private void centerFrame(JFrame frame) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		int cX = (toolkit.getScreenSize().width / 2) - (frame.getWidth() / 2);
		int cY = (toolkit.getScreenSize().height / 2) - (frame.getHeight() / 2);
		frame.setMinimumSize(frame.getSize());
		frame.setLocation(cX, cY);
		frame.setVisible(true);
	}

	private JMenuItem newItem(String name) {
		JMenuItem item = new JCheckBoxMenuItem(name);
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		return item;
	}

}
