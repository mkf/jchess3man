package pl.edu.platinum.archiet.jchess3man.engine;

import java.util.Map;

/**
 * Created by Michał Krzysztof Feiler on 03.02.17.
 */
public class CastlingPossibilities {
    public enum ColorEntry {
        No(false, false),
        All(true, true),
        QueensideOnly(true, false),
        KingsideOnly(false, true);
        public final boolean q;
        public final boolean k;

        ColorEntry(boolean q, boolean k) {
            this.q = q;
            this.k = k;
        }

        public static ColorEntry n(boolean q, boolean k) {
            if (k && q) return All;
            if (k) return KingsideOnly;
            if (q) return QueensideOnly;
            return No;
        }

        public ColorEntry changeQueenside(boolean queenside) {
            return n(queenside, k);
        }

        public ColorEntry changeKingside(boolean kingside) {
            return n(q, kingside);
        }

        public ColorEntry offQueenside() {
            return k ? KingsideOnly : No;
        }

        public ColorEntry offKingside() {
            return q ? QueensideOnly : No;
        }
    }

    public final ColorEntry w;
    public final ColorEntry g;
    public final ColorEntry b;

    public CastlingPossibilities(ColorEntry w, ColorEntry g, ColorEntry b) {
        this.w = w;
        this.g = g;
        this.b = b;
    }

    public CastlingPossibilities(Map<Color, ColorEntry> from, ColorEntry defVal) {
        ColorEntry w = from.containsKey(Color.White) ? from.get(Color.White) : defVal;
        ColorEntry g = from.containsKey(Color.Gray) ? from.get(Color.Gray) : defVal;
        ColorEntry b = from.containsKey(Color.Black) ? from.get(Color.Black) : defVal;
        this.w = w == null ? defVal : w;
        this.g = g == null ? defVal : g;
        this.b = g == null ? defVal : b;
    }

    public static final CastlingPossibilities all =
            new CastlingPossibilities(
                    ColorEntry.All, ColorEntry.All, ColorEntry.All);
    public static final CastlingPossibilities zero =
            new CastlingPossibilities(
                    ColorEntry.No, ColorEntry.No, ColorEntry.No);

    public ColorEntry get(Color who) {
        switch (who) {
            case Black:
                return b;
            case Gray:
                return g;
            case White:
                return w;
        }
        throw new IllegalArgumentException(who.toString());
    }

    public CastlingPossibilities change(Color c, ColorEntry what) {
        if (what == null) return this; //should it be there?
        switch (c) {
            case White:
                return new CastlingPossibilities(what, g, b);
            case Gray:
                return new CastlingPossibilities(w, what, b);
            case Black:
                return new CastlingPossibilities(w, g, what);
        }
        return this;
    }

    public CastlingPossibilities sayNoTo(Color c) {
        return change(c, ColorEntry.No);
    }
}
