package pl.edu.platinum.archiet.jchess3man.engine;

import pl.edu.platinum.archiet.jchess3man.engine.helpers.SingleElementIterable;

import java.util.Collections;

import static java.lang.Boolean.logicalOr;

/**
 * Created by Michał Krzysztof Feiler on 29.01.17.
 */
public class PawnCapVector extends DiagonalVector implements PawnVector {
    public final boolean inward;

    public PawnCapVector(boolean inward, boolean plusFile) {
        super(1, plusFile);
        this.inward = inward;
    }

    @Override
    public boolean inward() {
        return inward;
    }

    @Override
    public boolean reqpc() {
        return !this.inward;
    }

    @Override
    public boolean reqProm(int rank) {
        return (!inward) && (rank == 1);
    }

    public boolean thruCenter(int fromRank) {
        return inward && fromRank == 5;
    }

    public Iterable<Color> moats(int ignored) {
        return moats();
    }

    public Iterable<Color> moats() {
        return Collections.emptyList();
    }

    public boolean creek(Pos from) {
        return from.rank < 3 &&
                logicalOr(
                        plusFile && from.file % 8 == 7,
                        !plusFile && from.file % 8 == 0);
    }

    @Override
    Pos addTo(Pos from) throws VectorAdditionFailedException {
        if (creek(from)) {
            throw new VectorAdditionFailedException(from, this);
        }
        if (thruCenter(from.rank)) {
            return new Pos(
                    5,
                    SolelyThruCenterDiagonalVector.addFile(from.file, plusFile));
        }
        return new Pos(
                from.rank + (inward ? 1 : -1),
                (from.file + (plusFile ? 1 : -1)) % 24);
    }

    @Override
    public Vector tail(int ignored) {
        return new ZeroVector();
    }

    @Override
    public PawnCapVector head(int ignored) {
        return this;
    }

    @Override
    Iterable<ContinuousVector> unitsContinuous(int fromRank) {
        return new SingleElementIterable<>(this);
    }

    /**
     * reversed() in case of PawnCapVector returns a reverse that is impossible
     * to perform. But would it be a good idea to return null as in the current
     * Dart codebase?
     *
     * @return an impossible to perform reverse PawnCapVector
     */
    @Override
    public PawnCapVector reversed() {
        return new PawnCapVector(!inward, !plusFile);
    }

    @Override
    public Iterable<Pos> emptiesFrom(Pos ignored) {
        return Collections.emptyList();
    }
}
