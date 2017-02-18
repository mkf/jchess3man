package pl.edu.platinum.archiet.jchess3man.engine;

/**
 * Created by Michał Krzysztof Feiler on 24.01.17.
 */
public interface Vector {
    int rank();

    int file();

    //public Pos addTo(Pos from) {return from.addVec(this);}
    Pos addTo(Pos from) throws VectorAdditionFailedException;

    default boolean toBool() {
        return rank() != 0 || file() != 0;
    }

    Iterable<? extends Vector> units(int fromRank);

    Iterable<Pos> emptiesFrom(Pos from) throws VectorAdditionFailedException;

    Iterable<Color> moats(Pos from);

    default MutableBoard mutableAfterBoard(Board oldb, Pos from, EnPassantStore ep) throws VectorAdditionFailedException {
        MutableBoard b = oldb.mutableCopy();
        manipulateMutableAfterBoard(b, from, ep);
        return b;
    }

    default void manipulateMutableAfterBoard(MutableBoard b, Pos from, EnPassantStore ep) throws VectorAdditionFailedException {
        manipulateMutableAfterBoard(b, from, ep, from.addVec(this));
    }

    default void manipulateMutableAfterBoard(MutableBoard b, Pos from, EnPassantStore ep, Pos to) throws VectorAdditionFailedException {
        b.move(from, to);
    }
}

