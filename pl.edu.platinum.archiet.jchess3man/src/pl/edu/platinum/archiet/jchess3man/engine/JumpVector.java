package pl.edu.platinum.archiet.jchess3man.engine;

import pl.edu.platinum.archiet.jchess3man.engine.helpers.SingleElementIterable;

/**
 * Created by Michał Krzysztof Feiler on 24.01.17.
 */
public abstract class JumpVector extends Vector {
    Iterable<Vector> units(int _) {
        return new SingleElementIterable<>(this);
    }
}
