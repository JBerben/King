package src.main.org.bot.client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.Method;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import src.main.org.bot.api.core.Client;

public class Console extends JFrame {

	private static final long serialVersionUID = -3779056108392508019L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Console frame = new Console();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @throws ClassNotFoundException
	 */
	public Console() throws ClassNotFoundException {
		Client c = new Client();
		Class<?> clientAPI = c.getClass();
		// Object obj = Objects.getO(clientAPI.getName());
		String enter = "enter";

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(Console.class.getResource("/resources/client_icon.png")));
		setTitle("Console");
		setResizable(false);
		setBounds(100, 100, 625, 590);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));

		JTextPane textPane = new JTextPane();
		textPane.setBackground(Color.BLACK);
		textPane.setForeground(Color.GREEN);
		textPane.setFont(new Font("DialogInput", Font.PLAIN, 22));
		textPane.setToolTipText("");
		contentPane.add(textPane);
		setVisible(true);

		int condition = JComponent.WHEN_FOCUSED;
		InputMap iMap = textPane.getInputMap(condition);
		ActionMap aMap = textPane.getActionMap();
		iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK), enter);
		aMap.put(enter, new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Class pTypes[] = { Class.class, Object.class, String.class };
				String text = textPane.getText();
				int numArgs = text.substring(text.indexOf("("), text.indexOf(")")).split(",").length;
				String methodName;
				String[] methodArgs = new String[numArgs];
				Method m;
				
				/*
				 * Manipulates the string into a proper function call
				 */
				methodName = text.split("\\(")[0];
				for (int i = 0; i < numArgs; i++) {
					methodArgs[i] = text.substring(text.indexOf("(") + 1, text.indexOf(")")).split(",")[i];
					//System.out.println(methodArgs[i]);
				}
				
				try {
					//System.out.println(methodArgs[0]);
					m = clientAPI.getMethod(methodName, pTypes[0], pTypes[1], pTypes[2]);
					m.invoke(c, Client.client, Client.instance, methodArgs[0]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				//Logger.write("Script successfully executed");
				// Clears text, ready for next command
				textPane.setText("");
			}
		});

	}

}
