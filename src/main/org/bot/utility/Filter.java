package src.main.org.bot.utility;

public interface Filter<F> {

    public boolean accept(F value);
}
