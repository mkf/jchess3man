package pl.edu.platinum.archiet.jchess3man.engine;

/**
 * Created by Michał Krzysztof Feiler on 28.01.17.
 */
public class LongDiagonalVector extends DiagonalVector {
    public LongDiagonalVector(int abs, boolean plusFile) {
        super(abs, plusFile);
    }

    @Override
    public boolean inward() {
        return true;
    }

    /**
     * @return a reverse LongDiagonalVector.
     * Important is a fact that this method depends on the
     * LongDiagonalVector always representing a Vector
     * which will pass through the center. Otherwise it
     * will return a vector perpendicular to the expected one.
     * It would need to have access to fromRank to do better.
     */
    @Override
    public LongDiagonalVector reversed() {
        return new LongDiagonalVector(abs, !plusFile);
    }

    @Override
    public DiagonalVector head(int fromRank) {
        if (fromRank == 5) return new SolelyThruCenterDiagonalVector(plusFile);
        return this.shortToCenterAlmost(fromRank).head();
    }

    @Override
    public Vector tail(int fromRank) {
        if (abs > 1) {
            if (fromRank >= 4) {
                if (fromRank >= 5)
                    return shortFromCenter(fromRank);
                if (thruCenterAndFurther(fromRank))
                    return new SolelyThruCenterDiagonalVector(plusFile);
            }
            return new LongDiagonalVector(abs - 1, plusFile);
        }
        return new ZeroVector();
    }

    public DirectDiagonalVector shortFromCenter(int fromRank) {
        return new DirectDiagonalVector(abs - 5 + fromRank, false, !plusFile);
    }

    public SolelyThruCenterDiagonalVector solelyThruCenter() {
        return new SolelyThruCenterDiagonalVector(plusFile);
    }

    @Override
    Pos addTo(Pos from) throws VectorAdditionFailedException {
        Pos p = from.addVec(shortToCenterAlmost(from.rank));
        WhetherThruCenterAndWhetherFurther whetherThruCenterAndWhetherFurther =
                whetherThruCenterAndWhetherFurther(from.rank);
        if (whetherThruCenterAndWhetherFurther.whetherThruCenter)
            p = p.addVec(solelyThruCenter());
        if (whetherThruCenterAndWhetherFurther.whetherFurther)
            p = p.addVec(shortFromCenter(from.rank));
        return p;
    }
}
