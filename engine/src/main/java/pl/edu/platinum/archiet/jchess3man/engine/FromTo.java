package pl.edu.platinum.archiet.jchess3man.engine;

/**
 * Created by Michał Krzysztof Feiler on 18.03.17.
 */
public class FromTo {
    public final Pos from;
    public final Pos to;
    static final int finalBitSizeOfAPos = Integer.highestOneBit(
            (new Pos(5, 23)).toInt()) + 1;

    public FromTo(Pos from, Pos to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public int hashCode() {
        return from.toInt() & (to.toInt() << finalBitSizeOfAPos);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FromTo && equals((FromTo) obj);
    }

    public boolean equals(FromTo b) {
        return from.equals(b.from) && to.equals(b.to);
    }
}
