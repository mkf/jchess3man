package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by Michał Krzysztof Feiler on 18.02.17.
 */
public class FromToProm {
    public final Pos from;
    public final Pos to;
    public final @Nullable FigType pawnPromotion;
    protected boolean vecsAreGenerated = false;
    protected @Nullable Iterable<? extends Vector> vecs = null;

    public FromToProm(Pos from, Pos to) {
        this(from, to, null);
    }

    public FromToProm(Pos from, Pos to, @Nullable FigType pawnPromotion) {
        this.from = from;
        this.to = to;
        this.pawnPromotion = pawnPromotion;
    }

    @Override
    public String toString() {
        return from.toString() + "→" + to.toString() + "Þþ" + FigType.string(pawnPromotion);
    }

    public boolean areVecsGenerated() {
        return vecsAreGenerated;
    }

    public void generateVecs(@NotNull Fig fsq) throws NullPointerException, NeedsToBePromotedException {
        vecs = fsq.vecs(from, to);
        checkPromotions();
        vecsAreGenerated = true;
    }

    private void checkPromotions() throws NeedsToBePromotedException {
        boolean started = false;
        @Nullable ArrayList<PawnPromVector> theRet = null;
        assert vecs != null;
        for (final Vector vec : vecs) {
            if (vec instanceof PawnVector) {
                PawnVector pv = (PawnVector) vec;
                if (pv.reqProm(from.rank)) {
                    if (pawnPromotion == null)
                        try {
                            throw new NeedsToBePromotedException(new BoundVec(pv, from));
                        } catch (VectorAdditionFailedException e) {
                            throw new AssertionError(e);
                        }
                    started = true;
                    theRet = new ArrayList<>(2);
                }
                if (started) {
                    theRet.add(PawnPromVector.fromPawnVector(pv, pawnPromotion));
                }
            }
        }
        if (started) vecs = theRet;
    }

}
