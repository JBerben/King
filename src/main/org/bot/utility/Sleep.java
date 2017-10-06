package src.main.org.bot.utility;

import java.util.Random;

public class Sleep {

    private static final Random random = new Random();

    public static int random(int Min, int Max) {
        return random.nextInt(Math.abs(Max - Min)) + Min;
    }

    public static void sleep(int Time) {
        try {
            Thread.sleep(Time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepRandom(int Min, int Max) {
        sleep(random(Min, Max));
    }
}
