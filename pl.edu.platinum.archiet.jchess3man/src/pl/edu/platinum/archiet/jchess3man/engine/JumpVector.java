package pl.edu.platinum.archiet.jchess3man.engine;

import pl.edu.platinum.archiet.jchess3man.engine.helpers.SingleElementIterable;

/**
 * Created by Michał Krzysztof Feiler on 24.01.17.
 */
public abstract class JumpVector extends Vector {
    public Iterable<? extends JumpVector> units(int ignored) {
        return new SingleElementIterable<>(this);
    }
}
