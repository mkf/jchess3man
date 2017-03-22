package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.edu.platinum.archiet.jchess3man.engine.helpers.SingleElementIterable;

import java.util.Collections;

/**
 * Created by Michał Krzysztof Feiler on 28.01.17.
 */
public class DiagonalVector extends ContinuousVector {
    public final boolean plusFile;
    public final boolean inward;

    public DiagonalVector(int abs, boolean inward, boolean plusFile) {
        super(abs);
        this.plusFile = plusFile;
        this.inward = inward;
    }

    @Override
    public String toString() {
        return "Diag["+abs+(inward?"↑":"↓")+(plusFile?"→":"←")+"]";
    }

    public DiagonalVector shortFromCenter(int fromRank) {
        return new DiagonalVector(abs - 5 + fromRank, false, !plusFile);
    }

    public DiagonalVector solelyThruCenter() {
        return new DiagonalVector(1, true, plusFile);
    }

    public int rank() {
        return inward ? abs : -abs;
    }

    public int file() {
        return plusFile ? abs : -abs;
    }

    @Override
    public boolean toBool() {
        return abs > 0;
    }

    public boolean badNotInward() {
        return (!inward) && abs > 5;
    }

    public boolean thruCenter(int fromRank) {
        return inward && (fromRank + abs > 5);
    }

    public boolean thruCenterAndFurther(int fromRank) {
        return inward && (fromRank + abs > 6);
    }

    protected static class WhetherThruCenterAndWhetherFurther {
        public final boolean whetherThruCenter;
        public final boolean whetherFurther;

        WhetherThruCenterAndWhetherFurther(boolean tC, boolean f) {
            this.whetherFurther = f;
            this.whetherThruCenter = tC;
        }
    }

    public WhetherThruCenterAndWhetherFurther whetherThruCenterAndWhetherFurther(int fromRank) {
        if (!inward)
            return new WhetherThruCenterAndWhetherFurther(false, false);
        int fromPlusAbs = fromRank + abs;
        if (fromPlusAbs <= 5)
            return new WhetherThruCenterAndWhetherFurther(false, false);
        if (fromPlusAbs <= 6)
            return new WhetherThruCenterAndWhetherFurther(true, false);
        return new WhetherThruCenterAndWhetherFurther(true, true);
    }

    @Override
    public Iterable<@NotNull Color> moats(Pos from) {
        @Nullable Color m = moat(from);
        if (m == null) return Collections.emptyList();
        return new SingleElementIterable<>(m);
    }

    public @Nullable Color moat(Pos from) {
        /*
        return moat(from, false);
    }

    public Color moat(Pos from, boolean noreverse) {
    */
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
        } else {
            try {
                Pos to = addTo(from);
                if (to.rank == 0) {
                    if (!plusFile) {
                        switch (to.file % 8) {
                            case 7:
                                return to.colorSegm();
                        }
                    } else {
                        switch (to.file % 8) {
                            case 0:
                                return to.colorSegm().previous();
                        }
                    }
                }
            } catch (VectorAdditionFailedException e) {
                e.printStackTrace();
            }
        }
        /*
        if (!noreverse) try {
            return reversed().moat(addTo(from), true);
        } catch (VectorAdditionFailedException e) {
            e.printStackTrace();
        }
        */
        return null;
    }

    protected DiagonalVector _shortToCenterAlmost(int fromRank) {
        return new DiagonalVector(
                thruCenter(fromRank) ? 5 - fromRank : abs, inward, plusFile);
    }

    public DiagonalVector shortToCenterAlmost(int fromRank) {
        return _shortToCenterAlmost(fromRank);
    }

    @Override
    @NotNull
    public DiagonalVector head(int fromRank) {
        return new DiagonalVector(1, inward, plusFile);
    }

    @Override
    @Nullable
    public DiagonalVector tail(int fromRank) {
        if (abs > 1) {
            if (fromRank >= 4) {
                if (fromRank >= 5)
                    return shortFromCenter(fromRank);
                if (thruCenterAndFurther(fromRank))
                    return new DiagonalVector(1, true, plusFile);
            }
            return new DiagonalVector(abs - 1, true, plusFile);
        }
        return null;
    }

    @Override
    public Pos addTo(Pos from) throws VectorAdditionFailedException {
        /*
        if(from.rank==5 && inward && abs==1) {
            return new Pos(5, addFileThruCenter(from.file,plusFile));
        }
        Pos p = from.addVec(shortToCenterAlmost(from.rank));
        WhetherThruCenterAndWhetherFurther whetherThruCenterAndWhetherFurther =
                whetherThruCenterAndWhetherFurther(from.rank);
        if (whetherThruCenterAndWhetherFurther.whetherThruCenter)
            p = p.addVec(solelyThruCenter());
        if (whetherThruCenterAndWhetherFurther.whetherFurther)
            p = p.addVec(shortFromCenter(from.rank));
        return p;
        */
        if (!inward) {
            int toRank = from.rank - abs;
            if (toRank < 0) throw new VectorAdditionFailedException(from, this);
            return new Pos(
                    toRank,
                    plusFile
                            ? (from.file + abs) % 24
                            : (from.file - abs + 24) % 24);
        }
        int fromPlusAbs = from.rank + abs;
        if (fromPlusAbs < 5) {
            return new Pos(
                    fromPlusAbs,
                    plusFile
                            ? (from.file + abs) % 24
                            : (from.file - abs + 24) % 24);
        } else {
            int further = fromPlusAbs - 6;
            int howMuchHere = 5 - from.rank;
            int fileAfterDirect = plusFile
                    ? (from.file + howMuchHere) % 24
                    : (from.file - howMuchHere + 24) % 24;
            if (further == -1) return new Pos(5, fileAfterDirect);
            else {
                if (further > 5) throw new VectorAdditionFailedException(from, this);
                int rankAfter = 5 - further;
                int solelyThruCenterFile =
                        (fileAfterDirect + (plusFile ? -10 + 24 : 10)) % 24;
                if (further == 0) return new Pos(5, solelyThruCenterFile);
                else return new Pos(
                        rankAfter,
                        !plusFile
                                ? (solelyThruCenterFile + further) % 24
                                : (solelyThruCenterFile - further + 24) % 24);
            }
        }
    }
}
