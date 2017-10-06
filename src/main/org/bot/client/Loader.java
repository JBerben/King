package src.main.org.bot.client;

import javax.swing.*;

import src.main.org.bot.client.logger.LogType;
import src.main.org.bot.client.logger.Logger;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class Loader extends JPanel {

	private static final long serialVersionUID = 4245438094800859549L;
	private static final Dimension DEFAULT_SIZE = new Dimension(765, 503);
    private static Applet applet;
    private static URLClassLoader loader;
    private Object loadedInstance;

    public Loader() {
        try {
            this.setLayout(new BorderLayout(0, 0));
            Stub botStub = new Stub();

            if (!botStub.isDownloaded()) {
                Logger.write("Downloading Jar file from " + botStub.getParameter("jar_download"), LogType.CLIENT);
                botStub.downloadJar();
            } else {
                Logger.write("Jar file already downloaded", LogType.CLIENT);
            }

            String homePath = System.getProperty("user.home");
            String dir = (homePath == null ? "./" : new StringBuilder().append(homePath).append("/").toString()) + botStub.getParameter("cache_dir");
            String output = dir + "/" + botStub.getParameter("jar_savepath");

            // Needed to have the ".jar" extension for it to work with eclipse!
            URL[] urls = { new File(output + ".jar").toURI().toURL() };
            loader = new URLClassLoader(urls);
            Class<?> c = loader.loadClass(botStub.getParameter("main_class").replaceAll(".class", ""));
            loadedInstance = c.newInstance();

            JPanel panel = this.getInstanceAs();
            Component[] components = panel.getComponents();

            applet = (Applet)components[0];
            applet.setStub(botStub);
            applet.setMinimumSize(DEFAULT_SIZE);
            applet.setSize(applet.getMinimumSize());
            this.add(applet, BorderLayout.CENTER);           

            applet.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {
                   // System.out.println(Client.getOpenInterfaceID());
                    //System.out.println(Players.getMyPlayer().getAnimation());
                    //System.out.println(Players.getMyPlayer().getAnimId());
                	//System.out.println(Client.Test());
                //    System.out.println(Players.getMyPlayer().getTile());

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> T getInstanceAs() {
        return (T)this.loadedInstance;
    }

    public boolean hijackSuccessful(){
        return (getApplet() instanceof Applet);
    }

    public void start() {
        if (applet != null) {
            applet.init();
            applet.start();

            if (hijackSuccessful()) {
                Logger.write("Applet successfully hijacked", LogType.CLIENT);
            } else {
                Logger.writeWarning("Failed to hijack Applet", LogType.CLIENT);
                this.destruct();
            }

            while(applet.getComponentCount() < 1) {
                try {
                    Thread.sleep(10);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void destruct() {
        if (applet != null) {
            this.remove(applet);
            applet.stop();
            applet.destroy();
            applet = null;
        }
    }

    public Canvas getCanvas() {
        if (applet == null || applet.getComponentCount() == 0 || !(applet.getComponent(0) instanceof Canvas)) {
            return null;
        }
        return (Canvas) applet.getComponent(0);
    }

    public Graphics getGraphics() {
        return applet.getGraphics();
    }

    public Object getLoadedInstance() {
        return loadedInstance;
    }

    public static ClassLoader getClassLoader() {
        return loader;
    }

    public Applet getApplet() {
        return applet;
    }
}
