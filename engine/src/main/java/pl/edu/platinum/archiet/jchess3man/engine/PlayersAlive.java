package pl.edu.platinum.archiet.jchess3man.engine;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Michał Krzysztof Feiler on 02.02.17.
 */
public class PlayersAlive {
    public final boolean w;
    public final boolean g;
    public final boolean b;

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PlayersAlive && equals((PlayersAlive) obj);
    }

    public boolean equals(PlayersAlive ano) {
        return ano.w == w && ano.g == g && ano.b == b;
    }

    @Override
    public int hashCode() {
        return (w ? 0b1 : 0) | (g ? 0b10 : 0) | (b ? 0b100 : 0);
    }

    public PlayersAlive(boolean w, boolean g, boolean b) {
        this.w = w;
        this.g = g;
        this.b = b;
    }

    public PlayersAlive(Map<Color, Boolean> from, boolean defVal) {
        Boolean w = from.containsKey(Color.White) ? from.get(Color.White) : defVal;
        Boolean g = from.containsKey(Color.Gray) ? from.get(Color.Gray) : defVal;
        Boolean b = from.containsKey(Color.Black) ? from.get(Color.Black) : defVal;
        this.w = w == null ? defVal : w;
        this.g = g == null ? defVal : g;
        this.b = b == null ? defVal : b;
    }

    public static final PlayersAlive all =
            new PlayersAlive(true, true, true);
    public static final PlayersAlive zero =
            new PlayersAlive(false, false, false);

    public boolean get(Color who) {
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

    public PlayersAlive change(Color c, boolean what) {
        switch (c) {
            case White:
                return new PlayersAlive(what, g, b);
            case Gray:
                return new PlayersAlive(w, what, b);
            case Black:
                return new PlayersAlive(w, g, what);
        }
        return this;
    }

    public PlayersAlive change(Color c, Boolean what) {
        if (what == null) return this;
        return change(c, what);
    }

    public PlayersAlive die(Color c) {
        return change(c, false);
    }

    public ArrayList<Color> listEm() {
        ArrayList<Color> ret = new ArrayList<>(3);
        for (final Color color : Color.colors)
            if (get(color)) ret.add(color);
        return ret;
    }

}
