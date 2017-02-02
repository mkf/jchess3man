package pl.edu.platinum.archiet.jchess3man.engine;

import java.util.Iterator;

/**
 * Created by Michał Krzysztof Feiler on 02.02.17.
 */
public class AllPosIterable implements Iterable<Pos> {
    @Override
    public Iterator<Pos> iterator() {
        return new PosIterator();
    }
}
