package pl.edu.platinum.archiet.jchess3man.engine;

/**
 * Created by Michał Krzysztof Feiler on 24.01.17.
 */
public abstract class Vector {
    public abstract int rank();

    public abstract int file();

    //public Pos addTo(Pos from) {return from.addVec(this);}
    abstract Pos addTo(Pos from) throws VectorAdditionFailedException;

    public boolean toBool() {
        return rank() != 0 || file() != 0;
    }

    public abstract Iterable<? extends Vector> units(int fromRank);

    public abstract Iterable<Pos> emptiesFrom(Pos from);

    public abstract Iterable<Color> moats(Pos from);
}

