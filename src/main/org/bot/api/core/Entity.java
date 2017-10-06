package src.main.org.bot.api.core;

import static src.main.org.bot.reflection.Integers.getInt;
import static src.main.org.bot.reflection.Integers.getIntArray;
import static src.main.org.bot.reflection.Strings.getString;

public class Entity {

    private Object entityInstance;

    public Entity() {
    }

    public Entity(Object obj) {
        this.entityInstance = obj;
    }

    public String getSpokenText() {
        return getString("com.locopk.client.rs.Entity", "textSpoken", entityInstance);
    }

    public int getInteractingEntity() {
        return getInt("com.locopk.client.rs.Entity", "interactingEntity", entityInstance);
    }

    public int getAnimation() {
        return getInt("com.locopk.client.rs.Entity", "executingAnim", entityInstance);
    }

    public int getAnimId() {
        return getInt("com.locopk.client.rs.Entity", "animId", entityInstance);
    }

    public int getSmallX() {
        return getIntArray("com.locopk.client.rs.Entity", "smallX", entityInstance)[0];
    }

    public int getSmallY() {
        return getIntArray("com.locopk.client.rs.Entity", "smallY", entityInstance)[0];
    }

    //public Tile getTile() {
   //     return new Tile(getSmallX() + Client.getBaseX(), getSmallY() + Client.getBaseY(), Client.getPlane());
   //}

    public int getFacingDirection() {
        return getInt("com.locopk.client.rs.Entity", "turnDirection", entityInstance);
    }

    public int getMaxHealth() {
        return getInt("com.locopk.client.rs.Entity", "maxHealth", entityInstance);
    }

    public int getCurrentHealth() {
        return getInt("com.locopk.client.rs.Entity", "currentHealth", entityInstance);
    }

    public int getAbsX() {
        return getInt("com.locopk.client.rs.Entity", "absX", entityInstance);
    }

    public int getAbsY() {
        return getInt("com.locopk.client.rs.Entity", "absY", entityInstance);
    }

    public int getLoopCycleStatus() {
        return getInt("com.locopk.client.rs.Entity", "loopCycleStatus", entityInstance);
    }

    public boolean isUnderAttack() {
        return getLoopCycleStatus() > Client.getLoopCycle();
    }
}
