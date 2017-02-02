package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.Contract;

/**
 * Created by Michał Krzysztof Feiler on 02.02.17.
 */
public enum FigType {
    ZeroFigType((byte) 0),
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

    public static final FigType[] vals = values();

    @Contract(pure = true)
    public static FigType fromIndex(int index) {
        return vals[index];
    }

    public static FigType fromIndex(byte index) {
        return vals[index];
    }

    @Contract(pure = true)
    public int toInt() {
        return (int) index;
    }

    public static final String[] strings = {"t0", "tR", "tN", "tB", "tQ", "tK", "tP"};

    @Contract(pure = true)
    public String toString() {
        return strings[index];
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
}
