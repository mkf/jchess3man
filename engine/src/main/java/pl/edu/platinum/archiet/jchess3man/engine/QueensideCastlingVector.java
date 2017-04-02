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

    @Override
    public boolean checkPossibility(CastlingPossibilities.ColorEntry c) {
        return c.q;
    }

    @Override
    public ImmutableBoard manipulateImmutableAfterBoard(ImmutableBoard b,
                                                        Pos from, EnPassantStore ep, Pos to)
            throws VectorAdditionFailedException {
        return b.move(from, to)
                .move(new Pos(0, from.file - 4),
                        new Pos(0, from.file - 1));
    }

    @Override
    public void manipulateMutableAfterBoard(MutableBoard b, Pos from, EnPassantStore ep, Pos to) throws VectorAdditionFailedException {
        b.move(from, to);
        b.move(new Pos(0, from.file - 4), new Pos(0, from.file - 1));
    }
}
