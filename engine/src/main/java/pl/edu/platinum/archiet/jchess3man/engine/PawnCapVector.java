package pl.edu.platinum.archiet.jchess3man.engine;

import pl.edu.platinum.archiet.jchess3man.engine.helpers.SingleElementIterable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.Boolean.logicalOr;

/**
 * Created by Michał Krzysztof Feiler on 29.01.17.
 */
public class PawnCapVector extends DiagonalVector implements PawnVector {
    public PawnCapVector(boolean inward, boolean plusFile) {
        super(1,inward, plusFile);
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

    static final List<PawnCapVector> allCombinations = Arrays.asList(
            new PawnCapVector(false, false),
            new PawnCapVector(false, true),
            new PawnCapVector(true, false),
            new PawnCapVector(true, true)
    );

    public static PawnCapVector pawnCapVector(Pos from, Pos to) throws CannotConstructVectorException {
        for (PawnCapVector tryin : allCombinations) {
            try {
                if (tryin.addTo(from).equals(to)) return tryin;
            } catch (VectorAdditionFailedException ignored) {
            }
        }
        throw new CannotConstructVectorException(from, to);
    }

    public boolean creek(Pos from) {
        return from.rank < 3 &&
                logicalOr(
                        plusFile && from.file % 8 == 7,
                        !plusFile && from.file % 8 == 0);
    }

    @Override
    public Pos addTo(Pos from) throws VectorAdditionFailedException {
        if (creek(from)) {
            throw new VectorAdditionFailedException(from, this);
        }
        if (thruCenter(from.rank)) {
            /*
            return new Pos(
                    5,
                    SolelyThruCenterDiagonalVector.addFile(from.file, plusFile));
                    */
            return super.addTo(from);
        }
        return new Pos(
                from.rank + (inward ? 1 : -1),
                (from.file + (plusFile ? 1 : 24 - 1)) % 24);
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
    public PawnCapVector reversed() {
        return new PawnCapVector(!inward, !plusFile);
    }

    @Override
    public Iterable<Pos> emptiesFrom(Pos ignored) {
        return Collections.emptyList();
    }

    @Override
    public void manipulateMutableAfterBoard(MutableBoard b, Pos from, EnPassantStore ep, Pos to) throws VectorAdditionFailedException {
        Color col = b.get(from).color;
        //emptying if enpassant
        //if(before.enPassantStore.matchLast(boundVec.to))
        //    return !before.board.get(boundVec.to)
        //            .color.equals(who().previous());
        if (Boolean.logicalOr(
                ep.matchLast(to) && b.get(3, to.file).color == col.previous(),
                ep.matchPrev(to) && b.get(3, to.file).color == col.next()))
            b.clr(3, to.file);
        b.move(from, to);
    }
}
