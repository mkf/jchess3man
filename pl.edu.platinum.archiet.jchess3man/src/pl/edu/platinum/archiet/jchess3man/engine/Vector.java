package pl.edu.platinum.archiet.jchess3man.engine;

/**
 * Created by Michał Krzysztof Feiler on 24.01.17.
 */
public abstract class Vector {
    public int rank();

    public int file();

    //public Pos addTo(Pos from) {return from.addVec(this);}
    Pos addTo(Pos from);

    public boolean toBool() {
        return ((rank != null) && (file != null)) && (rank != 0 || file != 0);
    }

    public Iterable<Vector> units(int fromrank);

    public Iterable<Pos> emptiesFrom(Pos from);

    public Iterable<Color> moats(Pos from);
}

