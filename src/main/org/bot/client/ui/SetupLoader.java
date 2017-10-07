package src.main.org.bot.client.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;
import javax.swing.plaf.ProgressBarUI;

import src.main.org.bot.client.Main;
import src.main.org.updater.obj.Key;

public class SetupLoader extends JPanel {
	
	private static SetupLoader current;
	private static String state = "Initiliazing...";

	private static final int STATE_AUTHENTICATION = 0;
	public static final int STATE_LOADING = 1;
	private static final int STATE_SERVER_SELECT = 2;
	private int currentState;

	private ProgressBarUI progressBar;
	private JPanel loginPanel;
	
	float progress = 0;
	String status = "Initializing...";
	
	public void UpdateProgress(float prog, String text) {
		progress = prog;
		status = text;
		this.repaint();
	}

	@Override
	public void paint(Graphics g) {
		AffineTransform at = new AffineTransform();
		super.paint(g);

		this.setBackground(Color.BLACK);
		
		Graphics2D g2 = (Graphics2D) g;
		Graphics2D gg2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		AffineTransform save_ctx = g2.getTransform();
		AffineTransform current_ctx = new AffineTransform();
		current_ctx.concatenate(at);
		current_ctx.translate(getWidth() / 2, getHeight() / 2);
		current_ctx.rotate(Math.toRadians(270));
		g2.transform(current_ctx);
		g2.setColor(Color.MAGENTA);
		g2.setStroke(new BasicStroke(12.0f, BasicStroke.CAP_SQUARE, BasicStroke.CAP_SQUARE));
		g2.drawArc(-(getHeight() - 560 / 2) / 2, -(getHeight() - 560 / 2) / 2, getHeight() - 280, getHeight() - 280, 0,
				-(int) (progress * 3.6));
		
		AffineTransform current_ctx2 = new AffineTransform();
		current_ctx2.rotate(Math.toRadians(90));
		g2.transform(current_ctx2);
		gg2.setFont(Main.customFont.deriveFont(Font.BOLD, 28));
		gg2.setColor(Color.WHITE);
		gg2.drawString("KING", -20, 10);
		FontMetrics fm = g2.getFontMetrics();
		Rectangle2D r = fm.getStringBounds(progress + "%", g2);
		int x = -20 + (0 - (int) r.getWidth()) / 2;
		int y = 150 + (0 - (int) r.getHeight()) / 2 + fm.getAscent();
		g2.setColor(Color.MAGENTA);
		g2.setFont(Main.customFont.deriveFont(Font.TRUETYPE_FONT, 22));
		g2.drawString(status, x, y);
		
		
		g2.transform(save_ctx);
		
	}

}
