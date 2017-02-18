package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;

/**
 * Created by Michał Krzysztof Feiler on 02.02.17.
 */
public class EnPassantStore {
    public final Integer prev;
    public final Integer last;

    public EnPassantStore(Pos prev, Pos last) {
        if (prev != null) {
            assert (prev.rank == 3 || prev.rank == 2);
            this.prev = prev.file;
        } else this.prev = null;
        if (last != null) {
            assert (last.rank == 3 || last.rank == 2);
            this.last = last.file;
        } else this.last = null;
    }

    public EnPassantStore(Integer prev, Integer last) {
        this.prev = prev;
        this.last = last;
    }

    public EnPassantStore() {
        this.prev = null;
        this.last = null;
    }

    private EnPassantStore(Object prev, Object last) {
        if (prev == null) this.prev = null;
        else if (prev instanceof Integer) this.prev = (Integer) prev;
        else if (prev instanceof Pos) {
            Pos prevC = (Pos) prev;
            assert (prevC.rank == 2 || prevC.file == 3);
            this.prev = prevC.file;
        } else {
            throw new IllegalArgumentException(prev.toString());
        }
        if (last == null) this.last = null;
        else if (prev instanceof Integer) this.last = (Integer) last;
        else if (last instanceof Pos) {
            Pos lastC = (Pos) last;
            assert (lastC.rank == 2 || lastC.file == 3);
            this.last = lastC.file;
        } else {
            throw new IllegalArgumentException(last.toString());
        }
    }

    public EnPassantStore appeared(Integer p) {
        return new EnPassantStore(last, p);
    }

    public EnPassantStore appeared(Pos p) {
        return appeared(p.file);
    }

    public EnPassantStore nothing() {
        return new EnPassantStore(last, null);
    }

    public boolean match(Pos p) {
        if (/*p.rank!=3 &&*/ p.rank != 2) throw new IllegalArgumentException();
        return match(p.file);
    }

    private boolean match(int f) {
        return matchLast(f) || matchPrev(f);
    }

    public boolean matchLast(Pos p) {
        if (/*p.rank!=3&&*/p.rank != 2) throw new IllegalArgumentException();
        return matchLast(p.file);
    }

    @Contract(pure = true)
    private boolean matchLast(int f) {
        return last != null && last == f;
    }

    public boolean matchPrev(Pos p) {
        if (/*p.rank!=3&&*/p.rank != 2) throw new IllegalArgumentException();
        return matchPrev(p.file);
    }

    @Contract(pure = true)
    private boolean matchPrev(int f) {
        return prev != null && prev == f;
    }

    public static final EnPassantStore empty =
            new EnPassantStore();

    public ArrayList<Integer> toList() {
        ArrayList<Integer> ret = new ArrayList<>(2);
        ret.set(0, prev);
        ret.set(1, last);
        return ret;
    }

    public Integer[] toArray() {
        return new Integer[]{prev, last};
    }
}
