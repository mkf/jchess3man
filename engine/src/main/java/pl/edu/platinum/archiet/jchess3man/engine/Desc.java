package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.lambda.Seq;

import java.util.ArrayList;

/**
 * Created by Michał Krzysztof Feiler on 18.02.17.
 * Desc is a struct of [from], [to] and optionally [pawnPromotion] [FigType]
 * also storing it's generated [vecs] Iterable reference in it
 */
public class Desc extends FromTo {
    /**
     * the optional, i.e. nullable, pawnPromotion
     * current implementation probably should work with any non-null FigType always there
     * but it would look badly in the string representations
     */
    public final @Nullable FigType pawnPromotion;
    /**
     * whether generateVecs() was ever called and returned into [vecs]
     */
    protected boolean vecsAreGenerated = false;
    /**
     * vecs, the Iterable of vectors generated by generateVecs()
     * initially null, use after checking [vecsAreGenerated] or [areVecsGenerated()]
     */
    protected @Nullable Iterable<? extends Vector> vecs = null;

    /**
     * A constructor for Desc with null pawnPromotion
     *
     * @param from starting position
     * @param to   destination position
     */
    public Desc(Pos from, Pos to) {
        this(from, to, null);
    }

    public Desc(Desc s) {
        this(s.from, s.to, s.pawnPromotion, s.vecs, s.vecsAreGenerated);
    }

    /**
     * A constructor for Desc
     *
     * @param from          starting position
     * @param to            destination position
     * @param pawnPromotion optional pawnPromotion
     */
    public Desc(Pos from, Pos to, @Nullable FigType pawnPromotion) {
        super(from, to);
        this.pawnPromotion = pawnPromotion;
    }

    protected Desc(Pos from, Pos to, @Nullable FigType pawnPromotion,
                   @Nullable Iterable<? extends Vector> vecs,
                   boolean vecsAreGenerated) {
        this(from, to, pawnPromotion);
        this.vecs = vecs;
        this.vecsAreGenerated = vecsAreGenerated;
    }

    protected Desc(Desc source, @NotNull FigType pawnPromotion) {
        this(source.from, source.to, pawnPromotion,
                source.vecs, source.vecsAreGenerated);
        if (vecsAreGenerated && !pawnPromotion.equals(source.pawnPromotion))
            vecs = Seq.seq(source.vecs).map(vec -> {
                if (vec instanceof PawnPromVector)
                    return ((PawnPromVector) vec).withProm(pawnPromotion);
                return vec;
            });
    }

    protected static Seq<FigType> seqPromPossible = Seq.of(
            FigType.Queen, FigType.Rook, FigType.Bishop, FigType.Knight);

    Seq<? extends Desc> promPossible() {
        if (pawnPromotion == null) return Seq.of(this);
        return seqPromPossible.map(prom -> new Desc(this, prom));
    }

    /**
     * @param obj object tested
     * @return whether obj is a FromTo and this.equals((FromTo) obj)
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof FromTo && equals((FromTo) obj);
    }

    /**
     * @param b tested object
     * @return if b is a Desc return this.equals((Desc) b), else
     * whether this.pawnPromotion is null and b.equals(this)
     */
    @Override
    public boolean equals(FromTo b) {
        return b instanceof Desc
                ? equals((Desc) b)
                : pawnPromotion == null && b.equals(this);
    }

    /**
     * @param b tested object
     * @return whether this.equalsNoProm(b) and pawnPromotion is the same
     */
    public boolean equals(Desc b) {
        return equalsNoProm(b) &&
                (pawnPromotion == null && b.pawnPromotion == null ||
                        pawnPromotion != null &&
                                pawnPromotion.equals(b.pawnPromotion));
    }

    /**
     * @return hashcode composed of super.hashcode with bitwise left-shifted pawnPromotion
     */
    @Override
    public int hashCode() {
        return super.hashCode() | (FigType.toInt(pawnPromotion) <<
                ((finalBitSizeOfAPos << 1) + 1));
    }

    /**
     * @return string repr composed of [from]→[to]Þþ[pawnPromotion]
     */
    @Override
    public String toString() {
        return from.toString() + "→" + to.toString() + "Þþ" + FigType.string(pawnPromotion);
    }

    /**
     * @return whether generateVecs has finished already (i.e. vecsAreGenerated is true)
     */
    public boolean areVecsGenerated() {
        return vecsAreGenerated;
    }

    /**
     * stores fsq.vecs(from,to) in vecs field, runs checkPromotions and sets vecsAreGenerated to true
     *
     * @param fsq a Fig to call Fig.vecs(from,to) on
     * @throws NullPointerException       if fsq is null, I guess
     * @throws NeedsToBePromotedException if checkPromotions throws one, i.e.
     *                                    if pawnPromotion is null and promotion is required
     */
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