package pl.edu.platinum.archiet.jchess3man.engine;

/**
 * Created by Michał Krzysztof Feiler on 28.01.17.
 */
public class DirectDiagonalVector extends DiagonalVector {
    public final boolean inward;

    public DirectDiagonalVector(int abs, boolean inward, boolean plusFile) {
        super(abs, plusFile);
        this.inward = inward;
    }

    public DirectDiagonalVector(int rank, int file) {
        super(Math.abs(rank), file > 0);
        this.inward = rank > 0;
        if ((Math.abs(rank) != Math.abs(file)))
            throw new IllegalArgumentException(
                    "cannot construct DirectDiagonal from rank" + rank + ",file" + file
            );
    }

    @Override
    public boolean inward() {
        return inward;
    }

    @Override
    public DiagonalVector reversed() {
        return new DirectDiagonalVector(abs, !inward, !plusFile);
    }

    @Override
    public DirectDiagonalVector head(int ignored) {
        return head();
    }

    public DirectDiagonalVector head() {
        return new DirectDiagonalVector(1, inward, plusFile);
    }

    @Override
    public Vector tail(int ignored) {
        return tail();
    }

    public Vector tail() {
        if (abs > 1) {
            return new DirectDiagonalVector(abs - 1, inward, plusFile);
        }
        return new ZeroVector();
    }

    @Override
    public Pos addTo(Pos from) {
        return new Pos(from.rank + rank(), (from.file + file() + 24) % 24);
    }

    @Override
    public DirectDiagonalVector shortToCenterAlmost(int fromRank) {
        return this;
    }
}
