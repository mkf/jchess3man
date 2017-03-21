package pl.edu.platinum.archiet.jchess3man.engine;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class FileVector extends AxisVector {
    public FileVector(int abs, boolean direc) {
        super(abs % 24, direc);
    }

    @Override
    public String toString() {
        return "FileVec" + t();
    }

    public FileVector(int t) {
        super(t % 24);
    }

    public int file() {
        return t();
    }

    public int rank() {
        return 0;
    }

    public FileVector head() {
        if (abs > 0) {
            if (abs > 1) {
                return new FileVector(1, direc);
            }
            return this;
        }
        throw new AssertionError(this);
    }

    @Override
    public Vector tail(int ignored) {
        return tail();
    }

    public Vector tail() {
        if (abs > 0) {
            if (abs > 1) {
                return new FileVector(abs - 1, direc);
            }
            return new ZeroVector();
        }
        throw new AssertionError(this);
    }

    public Iterable<@NotNull Color> moats(Pos from) {
        if (from.rank != 0) return Collections.emptyList();
        int left = from.file % 8;
        int tm = direc ? 8 - left : left;
        Color start = from.colorSegm();
        int moating = abs - tm;
        ArrayList<Color> moatsToReturn = new ArrayList<>();
        if (moating > 0) {
            moatsToReturn.add(direc ? start : start.previous());
            if (moating > 8) {
                moatsToReturn.add(start.next());
                if (moating > 16) {
                    moatsToReturn.add(direc ? start.previous() : start);
                }
            }
        }
        return moatsToReturn;
    }

    @Override
    public Pos addTo(Pos from) {
        return new Pos(from.rank, (from.file + this.file() + 24) % 24);
    }
}

