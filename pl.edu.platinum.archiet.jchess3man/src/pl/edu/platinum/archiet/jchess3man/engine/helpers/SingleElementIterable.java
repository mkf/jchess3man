package pl.edu.platinum.archiet.jchess3man.engine.helpers;

import java.util.Iterator;

/**
 * Created by Michał Krzysztof Feiler on 25.01.17.
 */
public class SingleElementIterable<T> implements Iterable<T> {
    public final T elem;

    public SingleElementIterable(T element) {
        this.elem = element;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private boolean notYet = true;

            @Override
            public boolean hasNext() {
                return notYet;
            }

            @Override
            public T next() {
                if (!notYet) return null;
                notYet = false;
                return elem;
            }
        };
    }
}
