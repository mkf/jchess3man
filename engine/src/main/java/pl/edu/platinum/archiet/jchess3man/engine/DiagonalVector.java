package pl.edu.platinum.archiet.jchess3man.engine;

import pl.edu.platinum.archiet.jchess3man.engine.helpers.SingleElementIterable;

/**
 * Created by Michał Krzysztof Feiler on 28.01.17.
 */
public abstract class DiagonalVector extends ContinuousVector {
    public final boolean plusFile;

    public DiagonalVector(int abs, boolean plusFile) {
        super(abs);
        this.plusFile = plusFile;
    }

    public abstract boolean inward();

    public int rank() {
        return inward() ? abs : -abs;
    }

    public int file() {
        return plusFile ? abs : -abs;
    }

    @Override
    public boolean toBool() {
        return abs > 0;
    }

    public boolean badNotInward() {
        return (!inward()) && abs > 5;
    }

    public boolean thruCenter(int fromRank) {
        return inward() && (fromRank + abs > 5);
    }

    public boolean thruCenterAndFurther(int fromRank) {
        return inward() && (fromRank + abs > 6);
    }

    protected class WhetherThruCenterAndWhetherFurther {
        public final boolean whetherThruCenter;
        public final boolean whetherFurther;

        WhetherThruCenterAndWhetherFurther(boolean tC, boolean f) {
            this.whetherFurther = f;
            this.whetherThruCenter = tC;
        }
    }

    public WhetherThruCenterAndWhetherFurther whetherThruCenterAndWhetherFurther(int fromRank) {
        if (!inward())
            return new WhetherThruCenterAndWhetherFurther(false, false);
        int fromPlusAbs = fromRank + abs;
        if (fromPlusAbs <= 5)
            return new WhetherThruCenterAndWhetherFurther(false, false);
        if (fromPlusAbs <= 6)
            return new WhetherThruCenterAndWhetherFurther(true, false);
        return new WhetherThruCenterAndWhetherFurther(true, true);
    }

    public abstract DiagonalVector reversed();

    @Override
    public Iterable<Color> moats(Pos from) {
        return new SingleElementIterable<>(moat(from));
    }

    public Color moat(Pos from) {
        return moat(from, false);
    }

    public Color moat(Pos from, boolean noreverse) {
        if (from.rank == 0) {
            if (plusFile) {
                switch (from.file % 8) {
                    case 7:
                        return from.colorSegm();
                }
            } else {
                switch (from.file % 8) {
                    case 0:
                        return from.colorSegm().previous();
                }
            }
        }
        if (!noreverse) try {
            return reversed().moat(addTo(from), true);
        } catch (VectorAdditionFailedException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected DirectDiagonalVector _shortToCenterAlmost(int fromRank) {
        return new DirectDiagonalVector(
                thruCenter(fromRank) ? 5 - fromRank : abs, inward(), plusFile);
    }

    public DirectDiagonalVector shortToCenterAlmost(int fromRank) {
        return _shortToCenterAlmost(fromRank);
    }
}
