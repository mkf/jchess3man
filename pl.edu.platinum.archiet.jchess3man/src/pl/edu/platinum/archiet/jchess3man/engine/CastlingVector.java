package pl.edu.platinum.archiet.jchess3man.engine;

import pl.edu.platinum.archiet.jchess3man.engine.helpers.SingleElementIterable;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Created by Michał Krzysztof Feiler on 24.01.17.
 */
public abstract class CastlingVector extends JumpVector {
    public Iterable<CastlingVector> units(int fromRank) {
        assert (fromRank == 0);
        return new SingleElementIterable<>(this);
    }

    public static CastlingVector castlingVector(Pos from, Pos to) throws CannotConstructVectorException {
        if (from.rank == 0 && to.rank == 0 && from.file % 8 == kfm)
            switch (to.file % 8) {
                case 2:
                    return QueensideCastlingVector.c;
                case 6:
                    return KingsideCastlingVector.c;
            }
        throw new CannotConstructVectorException(from, to);
    }

    //public /*pseudo-abstract*/ static final int[] empties = {};

    public int[] empties() {
        if (this instanceof KingsideCastlingVector)
            return KingsideCastlingVector.empties;
        if (this instanceof QueensideCastlingVector)
            return QueensideCastlingVector.empties;
        throw new IllegalArgumentException("not a Kingside not QueenSide, " + this.toString());
    }

    @Override
    public boolean toBool() {
        return true;
    }

    @Override
    public int rank() {
        return 0;
    }

    private static final int kfm = 4;

    @Override
    Pos addTo(Pos from) {
        assert from.rank == 0 && from.file % 8 == kfm;
        return new Pos(0, from.file + file());
    }

    @Override
    public Iterable<Color> moats(Pos from) {
        return Collections.emptyList();
    }

    private class EmptyFromAdding {
        private final int a;

        EmptyFromAdding(int add) {
            this.a = add;
        }

        Pos giveFor(int rem) {
            return new Pos(0, a + rem);
        }
    }

    @Override
    public Iterable<Pos> emptiesFrom(Pos from) {
        assert (from.file % 8 == kfm);
        assert (from.rank == 0);
        int add = from.file - kfm;
        EmptyFromAdding adder = new EmptyFromAdding(add);
        return Arrays.stream(empties()).mapToObj(adder::giveFor).collect(Collectors.toList());
    }
}
