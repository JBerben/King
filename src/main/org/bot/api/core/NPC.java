package src.main.org.bot.api.core;

import java.util.ArrayList;

import src.main.org.bot.client.logger.LogType;
import src.main.org.bot.client.logger.Logger;

public class NPC extends Entity {

    private Object instance;

    public NPC() {
    }

    public NPC(final Object Obj) {
        super(Obj);
        this.instance = Obj;
    }

    public static final NPC[] getAllNPCs() {
        final ArrayList<NPC> NPCList = new ArrayList<>();
        final Object[] NPCs = Client.getNPCArray();
        for (int i = 0; i < NPCs.length; i++) {
            if (NPCs[i] == null) {
                continue;
            }
            final NPC n = new NPC(NPCs[i]);
            NPCList.add(n);
        }
        return NPCList.toArray(new NPC[NPCList.size()]);
    }

    public static void printAllNPCs() {
        for (NPC n : getAllNPCs()) {
            Logger.write(n.isUnderAttack() + " : " + n.isUnderAttack(), LogType.DEBUG);
        }
    }
}
