package pl.edu.platinum.archiet.jchess3man.engine;

/**
 * Created by Michał Krzysztof Feiler on 18.03.17.
 * FromTo is a struct of [from] and [to]
 */
public class FromTo {
    /**
     * the starting position
     */
    public final Pos from;
    /**
     * the destination position
     */
    public final Pos to;
    protected static final int finalBitSizeOfAPos = Integer.highestOneBit(
            (new Pos(5, 23)).toInt()) + 1;

    /**
     * Just a simple constructor for FromTo
     *
     * @param from the starting position
     * @param to   the destination position
     */
    public FromTo(Pos from, Pos to) {
        this.from = from;
        this.to = to;
    }

    /**
     * @return from.toInt() with bitwise left-shifted to.toInt()
     */
    @Override
    public int hashCode() {
        return from.toInt() | (to.toInt() << finalBitSizeOfAPos);
    }

    /**
     * @param obj object tested
     * @return returns whether obj is instanceof FromTo and this.equals((FromTo) obj)
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof FromTo && equals((FromTo) obj);
    }

    /**
     * @param b FromTo tested
     * @return whether from.equals(b.from) and to.equals(b.to)
     */
    public boolean equalsNoProm(FromTo b) {
        return from.equals(b.from) && to.equals(b.to);
    }

    /**
     * @param b FromTo or Desc tested
     * @return whether equalsNoProm(b) and
     * if b is FromToPro whether b.pawnPromotion is null
     */
    public boolean equals(FromTo b) {
        return equalsNoProm(b) &&
                !(b instanceof Desc && ((Desc) b).pawnPromotion != null);
    }
}
