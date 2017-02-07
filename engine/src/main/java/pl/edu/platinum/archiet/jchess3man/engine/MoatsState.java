package pl.edu.platinum.archiet.jchess3man.engine;

import java.util.Map;

/**
 * Created by Michał Krzysztof Feiler on 02.02.17.
 */
public class MoatsState {
    public final boolean bw;
    public final boolean wg;
    public final boolean gb;

    public MoatsState(boolean bw, boolean wg, boolean gb) {
        this.bw = bw;
        this.wg = wg;
        this.gb = gb;
    }

    /***
     * Creates a MoatsState from a map {White:bw, Gray:wg, Black:gb}
     * @param from   is a map Color→Boolean
     *               where Color is the next to the moat
     *               which is between this and previous
     * @param defVal the default value in case some bool was missing or null
     */
    public MoatsState(Map<Color, Boolean> from, boolean defVal) {
        Boolean bw = from.containsKey(Color.White) ? from.get(Color.White) : defVal;
        Boolean wg = from.containsKey(Color.Gray) ? from.get(Color.Gray) : defVal;
        Boolean gb = from.containsKey(Color.Black) ? from.get(Color.Black) : defVal;
        this.bw = bw == null ? defVal : bw;
        this.wg = wg == null ? defVal : wg;
        this.gb = gb == null ? defVal : gb;
    }

    public static final MoatsState allBridged =
            new MoatsState(true, true, true);
    public static final MoatsState noBridges =
            new MoatsState(false, false, false);

    public boolean isBridgedBetween(Color a, Color b) {
        if (a.next() == b) return isBridgedBetweenThisAndPrevious(b);
        else return isBridgedBetweenThisAndPrevious(a);
    }

    public boolean isBridgedBetweenThisAndPrevious(Color col) {
        switch (col) {
            case White:
                return bw;
            case Gray:
                return wg;
            case Black:
                return gb;
        }
        throw new IllegalArgumentException(col.toString());
    }

    public boolean isBridgedBetweenThisAndNext(Color col) {
        switch (col) {
            case Black:
                return bw;
            case White:
                return wg;
            case Gray:
                return gb;
        }
        throw new IllegalArgumentException(col.toString());
    }

    public boolean isBridgedOnTheOtherSide(Color col) {
        switch (col) {
            case Gray:
                return bw;
            case Black:
                return wg;
            case White:
                return gb;
        }
        throw new IllegalArgumentException(col.toString());
    }

    public boolean areBridgedAllExceptBetweenThisAndNext(Color col) {
        return isBridgedBetweenThisAndPrevious(col)
                && isBridgedOnTheOtherSide(col);
    }

    public boolean areBridgedAllExceptBetweenThisAndPrevious(Color col) {
        return isBridgedBetweenThisAndNext(col)
                && isBridgedOnTheOtherSide(col);
    }

    public boolean areBridgedOnBothSidesOf(Color col) {
        return isBridgedBetweenThisAndPrevious(col)
                && isBridgedBetweenThisAndNext(col);
    }

    public MoatsState changeBetweenThisAndPrevious(Color col, boolean to) {
        switch (col) {
            case White:
                return new MoatsState(to, wg, gb);
            case Gray:
                return new MoatsState(bw, to, gb);
            case Black:
                return new MoatsState(bw, wg, to);
        }
        throw new IllegalArgumentException(col.toString());
    }

    public MoatsState bridgeBetweenThisAndPrevious(Color col) {
        return changeBetweenThisAndPrevious(col, true);
    }

    public MoatsState changeBetweenThisAndNext(Color col, boolean to) {
        switch (col) {
            case Black:
                return new MoatsState(to, wg, gb);
            case White:
                return new MoatsState(bw, to, gb);
            case Gray:
                return new MoatsState(bw, wg, to);
        }
        throw new IllegalArgumentException(col.toString());
    }

    public MoatsState bridgeBetweenThisAndNext(Color col) {
        return changeBetweenThisAndNext(col, true);
    }

    public MoatsState changeOnTheOtherSide(Color col, boolean to) {
        switch (col) {
            case Gray:
                return new MoatsState(to, wg, gb);
            case Black:
                return new MoatsState(bw, to, gb);
            case White:
                return new MoatsState(bw, wg, to);
        }
        throw new IllegalArgumentException(col.toString());
    }

    public MoatsState bridgeOnTheOtherSide(Color col) {
        return changeOnTheOtherSide(col, true);
    }

    public MoatsState changeOnBothSidesOf(Color col, boolean to) {
        switch (col) {
            case Gray:
                return new MoatsState(bw, to, to);
            case Black:
                return new MoatsState(to, wg, to);
            case White:
                return new MoatsState(to, to, gb);
        }
        throw new IllegalArgumentException(col.toString());
    }

    public MoatsState bridgeOnBothSidesOf(Color col) {
        return changeOnBothSidesOf(col, true);
    }
}
