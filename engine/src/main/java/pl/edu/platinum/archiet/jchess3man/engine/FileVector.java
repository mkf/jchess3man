package pl.edu.platinum.archiet.jchess3man.engine;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    @NotNull
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
    @Nullable
    public FileVector tail(int ignored) {
        return tail();
    }

    @Nullable
    public FileVector tail() {
        if (abs > 0) {
            if (abs > 1) {
                return new FileVector(abs - 1, direc);
            }
            return null;
        }
        throw new AssertionError(this);
    }

    public Iterable<@NotNull Color> moats(Pos from) {
        if (from.rank != 0) return Collections.emptyList();
        return moats(from.file);
    }

    public Iterable<@NotNull Color> moats(int from) {
        int left = from % 8;
        int tm = direc ? 7 - left : left;
        Color start = Pos.colorSegm(from);
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

