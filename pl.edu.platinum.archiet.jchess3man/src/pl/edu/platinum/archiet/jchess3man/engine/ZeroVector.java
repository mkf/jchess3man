package pl.edu.platinum.archiet.jchess3man.engine;

import java.util.Collections;

/**
 * Created by Michał Krzysztof Feiler on 24.01.17.
 */
public class ZeroVector implements Vector {
    public ZeroVector() {
    }

    public int rank() {
        return 0;
    }

    public int file() {
        return 0;
    }

    @Override
    public boolean toBool() {
        return false;
    }

    @Override
    public Pos addTo(Pos from) {
        return from;
    }

    public Iterable<ContinuousVector> units(int ignored) {
        return Collections.emptyList();
    }

    @Override
    public Iterable<Pos> emptiesFrom(Pos from) {
        return Collections.emptyList();
    }

    public Iterable<Color> moats(Pos ignored) {
        return Collections.emptyList();
    }
}
