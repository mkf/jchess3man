package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.Contract;

/**
 * Created by Michał Krzysztof Feiler on 02.02.17.
 * An enum for FigType, without ZeroFigType which is not even deprecated but commented now
 */
public enum FigType {
    //ZeroFigType((byte) 0),
    Rook((byte) 1),
    Knight((byte) 2),
    Bishop((byte) 3),
    Queen((byte) 4),
    King((byte) 5),
    Pawn((byte) 6);
    public final byte index;

    FigType(byte index) {
        this.index = index;
    }

    public static final FigType[] vals = {
            null,
            Rook, Knight, Bishop, Queen, King, Pawn
    };

    @Contract(value = "null -> false", pure = true)
    public boolean equals(FigType b) {
        return b != null && b.index == index;
    }

    @Contract(pure = true)
    public static FigType fromIndex(int index) {
        return vals[index];
    }

    @Contract(pure = true)
    public static FigType fromIndex(byte index) {
        return vals[index];
    }

    @Contract(pure = true)
    public final int toInt() {
        return (int) index;
    }

    @Contract(pure = true, value = "null -> _")
    public static int toInt(FigType a) {
        return a == null ? 0 : a.toInt();
    }

    public static final String[] strings = {"t0", "tR", "tN", "tB", "tQ", "tK", "tP"};

    @Contract(pure = true)
    public String toString() {
        return strings[index];
    }

    @Contract(value = "null -> !null", pure = true)
    public static String string(FigType a) {
        return a == null ? "t_" : a.toString();
    }

    public static final FigType[] firstRankNewGame = {
            Rook,
            Knight,
            Bishop,
            Queen,
            King,
            Bishop,
            Knight,
            Rook
    };

    public final boolean canIPromTo() {
        switch (this) {
            case King:
                return false;
            case Pawn:
                return false;
            case Rook:
                return true;
            case Queen:
                return true;
            case Bishop:
                return true;
            case Knight:
                return true;
        }
        throw new AssertionError(this);
    }
}
