package pl.edu.platinum.archiet.jchess3man.engine;

/**
 * Created by Michał Krzysztof Feiler on 01.02.17.
 */
public class QueensideCastlingVector extends CastlingVector {
    public int file() {
        return -2;
    }

    public static final int[] empties = {3, 2, 1};

    public static final QueensideCastlingVector c = new QueensideCastlingVector();
}
