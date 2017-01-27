package pl.edu.platinum.archiet.jchess3man.engine;

import pl.edu.platinum.archiet.jchess3man.engine.helpers.SingleElementIterable;

import java.util.Collections;

/**
 * Created by Michał Krzysztof Feiler on 25.01.17.
 */
public class KnightVector extends JumpVector {
    /**
     * Towards the center, i.e. inwards
     */
    public final boolean inward;

    /**
     * Positive file direction (switched upon mirroring)
     */
    public final boolean plusFile;

    /**
     * One rank closer to the center?
     * (about that one more (twice instead of once) rank or file
     */
    public final boolean centerOneCloser;

    public KnightVector(boolean inward, boolean plusFile, boolean centerOneCloser) {
        this.inward = inward;
        this.plusFile = plusFile;
        this.centerOneCloser = centerOneCloser;
    }

    /**
     * Two times increment rank and once file?
     */
    public boolean moreRank() {
        return centerOneCloser == inward;
    }

    /**
     * Two times increment file and once rank?
     */
    public boolean moreFile() {
        return !moreRank();
    }

    public int rank() {
        return (inward ? 1 : -2) + (centerOneCloser ? 1 : 0);
    }

    public int file() {
        return (moreFile() ? 2 : 1) * (plusFile ? 1 : -1);
    }

    @Override
    Pos addTo(Pos from) {
        if (inward && (centerOneCloser && from.rank >= 4 || from.rank == 5))
            if (centerOneCloser)
                return new Pos(
                        (5 + 4) - from.rank,
                        (from.rank + (plusFile ? 1 : -1) + 12) % 24);
            else return new Pos(5, (from.rank + (plusFile ? 2 : -2) + 12) % 24);
        else return new Pos(from.rank + rank(), (from.file + file()) % 24);
    }

    /**
     * helper for [this.moat]
     */
    private static boolean xoreq(Pos f, Pos t) {
        if (f.rank > 2 && t.rank > 2) return null;
        int w = xrqnmv(f.file % 8, t.file % 8);
        switch (f.rank) {
            case 0:
                return t.rank == w;
            case w:
                return t.rank == 0;
            default:
                return false;
        }
    }

    /**
     * helper map for [xoreq]
     */
    private static int xrqnmv(int ffm, int tfm) {
        switch (ffm) {
            case 6:
                return tfm == 0 ? 1 : -100;
            case 7:
                return tfm == 1 ? 1 : tfm == 0 ? 2 : -100;
            case 0:
                return tfm == 6 ? 1 : tfm == 7 ? 2 : -100;
            case 1:
                return tfm == 7 ? 1 : -100;
            default:
                return -120;
        }
    }

    public Color moat(Pos from) {
        Pos to = addTo(from);
        boolean _xoreq = xoreq(from, to);
        return _xoreq == true ? Color.fromSegm(((from.file+2)~/8)%3) : null;
    }

    public Iterable<Color> moats(Pos from) {
        Color m = moat(from);
        if(m!=null) return new SingleElementIterable<>(m);
        return Collections.emptyList();
    }

    public boolean toBool() {
        return inward!=null && plusFile!=null && centerOneCloser!=null;
    }
    public Iterable<Vector> units(int _) {
        return new SingleElementIterable<>(this);
    }
}
