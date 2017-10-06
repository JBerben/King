package src.main.org.bot.client;

import java.applet.Applet;

public class Bot {

    private Loader BotLoader = null;

    public void loadBot() {
        try {
            BotLoader = new Loader();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Loader getLoader() {
        return BotLoader;
    }

    public Applet getApplet() {
        return (BotLoader != null) ? BotLoader.getApplet() : null;
    }
}
