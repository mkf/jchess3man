package pl.edu.platinum.archiet.jchess3man.engine;

import pl.edu.platinum.archiet.jchess3man.engine.helpers.SingleElementIterable;

import java.util.Collections;

/**
 * Created by Michał Krzysztof Feiler on 28.01.17.
 */
public class SolelyThruCenterDiagonalVector extends DiagonalVector {
    public SolelyThruCenterDiagonalVector(boolean plusFile) {
        super(1, plusFile);
    }

    public boolean inward() {
        return true;
    }

    public SolelyThruCenterDiagonalVector reversed() {
        return new SolelyThruCenterDiagonalVector(!plusFile);
    }

    @Override
    public Iterable<Color> moats(Pos from) {
        return Collections.emptyList();
    }

    @Override
    public SolelyThruCenterDiagonalVector head(int ignored) {
        return this;
    }

    @Override
    public ZeroVector tail(int fromrank) {
        return new ZeroVector();
    }

    @Override
    Iterable<ContinuousVector> unitsContinuous(int fromRank) {
        return new SingleElementIterable<>(this);
    }

    @Override
    Pos addTo(Pos from) throws VectorAdditionFailedException {
        if (from.rank != 5)
            throw new VectorAdditionFailedException(from, this);
        return new Pos(5, addFile(from.file, plusFile));
    }

    static int addFile(int posFile, boolean plusFile) {
        return (posFile + (plusFile ? -10 : 10)) % 24;
    }
}
