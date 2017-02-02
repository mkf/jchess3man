package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.Contract;

/**
 * Created by Michał Krzysztof Feiler on 02.02.17.
 */
public enum FigType {
    ZeroFigType(0), Rook(1), Knight(2), Bishop(3), Queen(4), King(5), Pawn(6);
    public final int index;

    FigType(int index) {
        this.index = index;
    }

    public static final FigType[] vals = values();

    @Contract(pure = true)
    public static FigType fromIndex(int index) {
        return vals[index];
    }

    @Contract(pure = true)
    public int toInt() {
        return index;
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
