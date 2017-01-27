package pl.edu.platinum.archiet.jchess3man.engine;

import com.sun.xml.internal.fastinfoset.stax.events.EmptyIterator;

import java.util.Collections;
import java.util.Iterator;

import static java.util.Collections.emptyList;

/**
 * Created by Michał Krzysztof Feiler on 24.01.17.
 */
public class ZeroVector extends Vector {
    public ZeroVector() {};

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
    Pos addTo(Pos from) {
        return from;
    }

    public Iterable<Vector> units(int _) {
        return Collections.emptyList();
    }

    public Iterable<Pos> emptiesFom(Pos _) {
        return Collections.emptyList();
    }

    public Iterable<Color> moats(Pos _) {
        return Collections.emptyList();
    }
}
