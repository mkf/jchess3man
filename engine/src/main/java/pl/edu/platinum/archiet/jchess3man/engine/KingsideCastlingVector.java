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

    @Override
    public boolean checkPossibility(CastlingPossibilities.ColorEntry c) {
        return c.k;
    }

    @Override
    public void manipulateMutableAfterBoard(MutableBoard b, Pos from, EnPassantStore ep, Pos to) throws VectorAdditionFailedException {
        b.move(from, to);
        b.move(new Pos(0, from.file + 3), new Pos(0, from.file + 1));
    }
}
