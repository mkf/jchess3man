package pl.edu.platinum.archiet.jchess3man.engine;


/**
 * Created by Michał Krzysztof Feiler on 28.01.17.
 */
public class CanIDiagonal {
    public final boolean shortCan;
    public final boolean longCan;
    public final Boolean positiveSgn;

    public CanIDiagonal(boolean shortCan, boolean longCan, boolean positiveSgn) {
        this(shortCan, longCan, (Boolean) positiveSgn);
    }

    public CanIDiagonal(boolean shortCan, boolean longCan, Boolean positiveSgn) {
        this.shortCan = shortCan;
        this.longCan = longCan;
        this.positiveSgn = positiveSgn;
    }

    protected static CanIDiagonal no =
            new CanIDiagonal(false, false, null);

    public boolean toBool() {
        return shortCan || longCan;
    }

    public CanIDiagonal(Pos from, Pos to) {
        if (from.equals(to)) {
            this.shortCan = false;
            this.longCan = false;
            this.positiveSgn = null;
        } else {
            int shorttd = Math.abs(to.rank - from.rank);
            int longtd = to.rank + from.rank;
            if (to.file == (from.file + shorttd) % 24) {
                positiveSgn = true;
                shortCan = true;

            } else if (to.file == (from.file - shorttd) % 24) {
                positiveSgn = false;
                shortCan = true;

            } else if (to.file == (from.file + longtd) % 24) {
                positiveSgn = true;
                shortCan = false;

            } else if (to.file == (from.file - longtd) % 24) {
                positiveSgn = false;
                shortCan = false;

            } else {
                positiveSgn = null;
                shortCan = false;
                longCan = false;
                return;
            }
            longCan = !shortCan ||
                    to.file ==
                            from.file + (positiveSgn ? longtd : -longtd) % 24;
        }
    }
}
