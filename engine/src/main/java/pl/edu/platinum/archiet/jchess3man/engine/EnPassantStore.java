package pl.edu.platinum.archiet.jchess3man.engine;

import java.util.ArrayList;

/**
 * Created by Michał Krzysztof Feiler on 02.02.17.
 */
public class EnPassantStore {
    public final Pos prev;
    public final Pos last;

    public EnPassantStore(Pos prev, Pos last) {
        this.prev = prev;
        this.last = last;
    }

    public EnPassantStore appeared(Pos p) {
        return new EnPassantStore(last, p);
    }

    public EnPassantStore nothing() {
        return new EnPassantStore(last, null);
    }

    public boolean match(Pos p) {
        return p.equals(last) || p.equals(prev);
    }

    public static final EnPassantStore empty =
            new EnPassantStore(null, null);

    public ArrayList<Pos> toList() {
        ArrayList<Pos> ret = new ArrayList<>(2);
        ret.set(0, prev);
        ret.set(1, last);
        return ret;
    }
}
