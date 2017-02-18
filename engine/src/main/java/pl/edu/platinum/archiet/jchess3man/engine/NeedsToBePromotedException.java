package pl.edu.platinum.archiet.jchess3man.engine;

/**
 * Created by Michał Krzysztof Feiler on 08.02.17.
 */
public class NeedsToBePromotedException extends Exception {
    public final BoundVec boundVec;

    public NeedsToBePromotedException(BoundVec boundVec) {
        this.boundVec = boundVec;
    }
}
