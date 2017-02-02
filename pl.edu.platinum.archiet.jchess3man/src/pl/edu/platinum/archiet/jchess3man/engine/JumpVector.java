package pl.edu.platinum.archiet.jchess3man.engine;

import pl.edu.platinum.archiet.jchess3man.engine.helpers.SingleElementIterable;

/**
 * Created by Michał Krzysztof Feiler on 24.01.17.
 */
public interface JumpVector extends Vector {
    default Iterable<? extends JumpVector> units(int ignored) {
        return new SingleElementIterable<>(this);
    }
}
