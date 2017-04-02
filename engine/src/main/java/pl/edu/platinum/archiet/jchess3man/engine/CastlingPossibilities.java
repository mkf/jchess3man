package pl.edu.platinum.archiet.jchess3man.engine;

import org.jooq.lambda.tuple.Tuple6;

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

    public CastlingPossibilities(int i) {
        w = ColorEntry.n((i & 1 << 4) != 0, (i & 1 << 5) != 0);
        g = ColorEntry.n((i & 1 << 2) != 0, (i & 1 << 3) != 0);
        b = ColorEntry.n((i & 1) != 0, (i & 1 << 1) != 0);
    }

    public CastlingPossibilities(Map<Color, ColorEntry> from, ColorEntry defVal) {
        ColorEntry w = from.getOrDefault(Color.White, defVal);
        ColorEntry g = from.getOrDefault(Color.Gray, defVal);
        ColorEntry b = from.getOrDefault(Color.Black, defVal);
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

    public Tuple6<Boolean, Boolean, Boolean, Boolean, Boolean, Boolean> array() {
        return new Tuple6<>(
                w.k, w.q,
                g.k, g.q,
                b.k, b.q
        );
    }

    public int toInt() {
        return (w.k ? 1 << 5 : 0) | (w.q ? 1 << 4 : 0) |
                (g.k ? 1 << 3 : 0) | (g.q ? 1 << 2 : 0) |
                (b.k ? 1 << 1 : 0) | (b.q ? 1 : 0);
    }
}
