package pl.edu.platinum.archiet.jchess3man.engine;

/**
 * Created by Michał Krzysztof Feiler on 08.02.17.
 */
public class ImpossibleMoveException extends IllegalMoveException {
    public final Impossibility impossibility;

    public ImpossibleMoveException(Impossibility impossibility) {
        this.impossibility = impossibility;
    }
}
