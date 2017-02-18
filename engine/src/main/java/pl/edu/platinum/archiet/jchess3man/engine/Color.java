package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.Contract;

/**
 * Created by Michał Krzysztof Feiler on 24.01.17.
 */
public enum Color {
    //ZeroColor((byte) 0),
    White((byte) 1), Gray((byte) 2), Black((byte) 3);
    public final byte index;

    Color(byte index) {
        this.index = index;
    }

    public static final Color[] colorsFromZero =
            new Color[]{null, White, Gray, Black};
    public static final Color[] colors = {White, Gray, Black};
    @Deprecated
    public static final String[] strings = {"ZeroColor", "White", "Gray", "Black"};

    @Contract(pure = true)
    public static Color byIndex(int index) {
        return colorsFromZero[index];
    }

    @Contract(pure = true)
    public static Color byIndex(byte index) {
        return colorsFromZero[index];
    }

    @Contract(pure = true)
    public static Color byIndex(short index) {
        return colorsFromZero[index];
    }

    @Contract(pure = true)
    public static Color fromSegm(byte segm) {
        return colors[segm];
    }

    @Contract(pure = true)
    public static Color fromSegm(short segm) {
        return colors[segm];
    }

    @Contract(pure = true)
    public static Color fromSegm(int segm) {
        return colors[segm];
    }

    @Contract(pure = true)
    public int toInt() {
        return (int) index;
    }

    @Contract(pure = true)
    public short toShort() {
        return (short) index;
    }

    @Contract(pure = true)
    public short toByte() {
        return index;
    }

    public Color next() {
        switch (this) {
            //case null/ZeroColor:
            //    return White;
            case White:
                return Gray;
            case Gray:
                return Black;
            case Black:
                return White;
        }
        throw new AssertionError(this);
    }

    public Color previous() {
        switch (this) {
            //case null/ZeroColor:
            //    return Black;
            case White:
                return Black;
            case Gray:
                return White;
            case Black:
                return Gray;
        }
        throw new AssertionError(this);
    }

    @Contract(pure = true)
    public int segm() {
        return (int) index - 1;
    }
}
