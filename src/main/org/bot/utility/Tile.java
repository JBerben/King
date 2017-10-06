package src.main.org.bot.utility;

public final class Tile {

    private final int x, y;
    private final int plane;

    public Tile(int x, int y, int plane) {
        this.x = x;
        this.y = y;
        this.plane = plane;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPlane() {
        return plane;
    }
}
