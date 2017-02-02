package pl.edu.platinum.archiet.jchess3man.engine;

/**
 * Created by Michał Krzysztof Feiler on 01.02.17.
 */
public class KingsideCastlingVector extends CastlingVector {
    public int file() {
        return 2;
    }

    public static final int[] empties = {5, 6};
    public static final KingsideCastlingVector c = new KingsideCastlingVector();
}
