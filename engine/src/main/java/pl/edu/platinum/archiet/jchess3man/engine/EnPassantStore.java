package pl.edu.platinum.archiet.jchess3man.engine;

import com.google.common.base.Splitter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple;

import java.util.ArrayList;
import java.util.Optional;

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

    public EnPassantStore(String legacyHex) {
        final Iterable<String> splitIter = Splitter.fixedLength(2).split(legacyHex);
        final Seq<String> split = Seq.seq(splitIter);
        final Optional<String> optPrevRank = split.get(0);
        final Optional<String> optPrevFile = split.get(1);
        final Optional<String> optLastRank = split.get(2);
        final Optional<String> optLastFile = split.get(3);
        assert optLastFile.isPresent() && optLastRank.isPresent() &&
                optPrevFile.isPresent() && optPrevRank.isPresent() : legacyHex;
        final String prevRank = optPrevRank.get();
        final String prevFile = optPrevFile.get();
        final String lastRank = optLastRank.get();
        final String lastFile = optLastFile.get();
        switch (prevRank) {
            case "7f":
                assert prevFile.equals("7f") : legacyHex;
                prev = null;
                break;
            case "02":
            case "03":
                prev = Integer.parseInt(prevFile, 16);
                assert prev < 24 : legacyHex;
                break;
            default:
                throw new AssertionError(legacyHex);
        }
        switch (lastRank) {
            case "7f":
                assert lastFile.equals("7f") : legacyHex;
                last = null;
                break;
            case "02":
            case "03":
                last = Integer.parseInt(lastFile, 16);
                assert last < 24 : legacyHex;
                break;
            default:
                throw new AssertionError(legacyHex);
        }
    }

    @Contract("null -> !null")
    @NotNull
    public static String legacyHex(@Nullable Integer i) {
        if (i == null) return "7f";
        String s = Integer.toHexString(i);
        switch (s.length()) {
            case 1:
                return "0" + s;
            case 2:
                return s;
            default:
                throw new AssertionError(s);
        }
    }

    public String legacyHex() {
        return (prev == null ? "7f" : "02") +
                legacyHex(prev) +
                (last == null ? "7f" : "02") +
                legacyHex(last);
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
