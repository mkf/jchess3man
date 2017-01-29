package pl.edu.platinum.archiet.jchess3man.engine;

/**
 * Created by Michał Krzysztof Feiler on 28.01.17.
 */
public class CannotConstructVectorException extends Throwable {
    public final Pos from;
    public final Pos to;

    public CannotConstructVectorException(Pos from, Pos to) {
        this.from = from;
        this.to = to;
    }
}
