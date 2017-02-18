package pl.edu.platinum.archiet.jchess3man.engine;

/**
 * Created by Michał Krzysztof Feiler on 08.02.17.
 */
public class WeInCheckException extends ImpossibleMoveException {
    public WeInCheckException(Impossibility.WeInCheck impossibility) {
        super(impossibility);
    }
}
