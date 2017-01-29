package pl.edu.platinum.archiet.jchess3man.engine;

import java.util.Collections;

/**
 * Created by Michał Krzysztof Feiler on 28.01.17.
 */
public class RankVector extends AxisVector {
    public RankVector(int abs, boolean direc) {
        super(abs, direc);
    }

    public RankVector(int t) {
        super(t);
    }

    public int file() {
        return 0;
    }

    public int rank() {
        return t();
    }

    public boolean thruCenter(int fromRank) {
        return direc && (fromRank + rank() > 5);
    }

    public pl.edu.platinum.archiet.jchess3man.engine.RankVector head() {
        if (abs > 0) {
            if (abs > 1) {
                return new pl.edu.platinum.archiet.jchess3man.engine.RankVector(1, direc);
            }
            return this;
        }
        throw new AssertionError(this);
    }

    public Vector tail(int fromRank) {
        if (abs > 0) {
            if (abs > 1) {
                return new pl.edu.platinum.archiet.jchess3man.engine.RankVector(abs - 1, direc && fromRank != 5);
            }
            return new ZeroVector();
        }
        throw new AssertionError(this);
    }

    @Override
    public Iterable<Color> moats(Pos from) {
        return Collections.emptyList();
    }

    @Override
    Pos addTo(Pos from) {
        if (thruCenter(from.rank))
            return new Pos(11 - (from.rank + abs), (from.file + 12) % 24);
        else return new Pos(from.rank + rank(), from.file);
    }
}
