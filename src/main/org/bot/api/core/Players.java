package src.main.org.bot.api.core;

import java.util.ArrayList;

import src.main.org.bot.hooks.XMLParser;

import static src.main.org.bot.reflection.Objects.getObject;
import static src.main.org.bot.reflection.Objects.getObjectArray;

public class Players {
	
	private static XMLParser parser = new XMLParser("Hooks");

    private static Object getLocalPlayer() {
        return getObject(parser.getClass("Client"), parser.getField("myPlayer"));
    }

    private static Object[] getPlayerArray() {
        return getObjectArray("com.locopk.client.rs.Client", "playerArray", getObject("com.locopk.client.rs.Client", "instance"));
    }

    public static final Actor[] getAllPlayers() {
        final ArrayList<Actor> playerList = new ArrayList<>();
        final Object[] players = getPlayerArray();
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) {
                continue;
            }
            final Actor player = new Actor(players[i]);
            playerList.add(player);
        }
        return playerList.toArray(new Actor[playerList.size()]);
    }

    public static final Actor getPlayer(String PlayerName) {
        final Actor[] players = getAllPlayers();
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null)
                continue;
            if (players[i].getName().equals(PlayerName))
                return players[i];
        }
        return null;
    }

    public static final LocalPlayer getMyPlayer() {
        return new LocalPlayer(getLocalPlayer());
    }

    public static void printAllPlayerOverheadPrayers() {
        for (Actor a : getAllPlayers()) {
        	System.out.println(a.getName() + a.getTile().toString());
        }
    }
}
