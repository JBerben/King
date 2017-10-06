package src.main.org.bot.api.core;

import src.main.org.bot.hooks.XMLParser;
import static src.main.org.bot.reflection.Integers.getInt;
import static src.main.org.bot.reflection.Strings.getString;
import static src.main.org.bot.reflection.Integers.getIntArray;


public class Actor extends Entity {

    private XMLParser parser = new XMLParser("Hooks");
    private Object instance;
    
    public Actor() {
    }

    public Actor(final Object Obj) {
        super(Obj);
        this.instance = Obj;
        this.parser = new XMLParser("Hooks");
    }
    
    //public int getTile() {
    //	return getInt("com.locopk.client.rs.Player", "tile"), instance);
    //}

    public String getName(){
        return getString(parser.getClass("Player"), parser.getField("name"), instance);
    }

    public int getCombatLevel() {
        return getInt("com.locopk.client.rs.Player", "combatLevel", instance);
    }

    public int getHeadIcon() {
        return getInt("com.locopk.client.rs.Player", "headIcon", instance);
    }

    public String getOverheadPrayer() {
        switch (getHeadIcon()) {
            case 0:
                return "MELEE";
            case 1:
                return "RANGE";
            case 2:
                return "MAGE";
            case 3:
                return "RETRIBUTION";
            case 4:
                return "SMITE";
            case 5:
                return "REDEMPTION";
            case 255:
                return "NONE";
            default:
                return null;
        }
    }

    public int getID() {
        return getInt("com.locopk.client.rs.Player", "playerId", instance);
    }

    public int[] getEquipment() {
        int[] result = getIntArray("com.locopk.client.rs.Player", "equipment", instance).clone();
        for (int i = 0; i < result.length; i++) {
            if (result[i] >= 512)
                result[i] = (result[i] - 512);
        }
        return result;
    }

	public Object getTile() {
		// TODO Auto-generated method stub
		return null;
	}
}
