package src.main.org.bot.api.calculations;

public class Compare {

    public static boolean inRange(int From, int Tolerance) {
        return (From - Tolerance <= From && From <= From + Tolerance);
    }

    public static boolean inRange(int From, int Min, int Max) {
        return (Min <= From && From <= Max);
    }
}
