package src.main.org.updater.util;

public class Stream {
    /**
     * Sends an object to the system output stream.
     *
     * @param object
     */
    public static void log(Object object) {
        System.out.println("King: " + object.toString());
    }

    /**
     * Sends an object to the system output stream.
     *
     * @param sender - name of "divison"
     */
    public static void log(String sender, Object object) {
        System.out.println(sender + ": " + object.toString());
    }
}
