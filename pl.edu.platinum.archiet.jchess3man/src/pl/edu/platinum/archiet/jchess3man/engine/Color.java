package pl.edu.platinum.archiet.jchess3man.engine;

/**
 * Created by Michał Krzysztof Feiler on 24.01.17.
 */
public class Color {
    //public final byte index;
    public final int index;

    public Color(/*byte*/ int index) {
        this.index = index;
        assert (index >= 0 && index < 4);
    }

    public Color(short index) {
        this((int) index);
    }

    public Color(byte index) {
        this((int) index);
    }

    public static Color fromSegm(byte segm) {
        return new Color(segm + 1);
    }

    public static Color fromSegm(short segm) {
        return new Color(segm + 1);
    }

    public static Color fromSegm(int segm) {
        return new Color(segm + 1);
    }

    public static final Color zeroColor = new Color(0);
    public static final Color white = new Color(1);
    public static final Color gray = new Color(2);
    public static final Color black = new Color(3);

    public int toInt() {
        return /*(int)*/ index;
    }

    public short toShort() {
        return (short) index;
    }

    public short toByte() {
        return (byte) index;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Color)) return false;
        Color col = (Color) obj;
        return equals(col);
    }

    public boolean equals(Color other) {
        return (other.index == index);
    }

    public int hashCode() {
        return toInt();
    }

    public Color next() {
        switch (this.index) {
            case 0: //zeroColor:
                return white;
            case 1: //white:
                return gray;
            case 2: //gray:
                return black;
            case 3: //black:
                return white;
        }
        throw new AssertionError(this);
    }

    public Color previous() {
        switch (this.index) {
            case 0: //zeroColor:
                return black;
            case 1: //white:
                return black;
            case 2: //gray:
                return white;
            case 3: //black:
                return gray;
        }
        throw new AssertionError(this);
    }

    public int board() {
        return index - 1;
    }

    public static final Color[] colors = {white, gray, black};
    public static final String[] strings = {"ZeroColor", "White", "Gray", "Black"};

    public String toString() {
        return strings[index];
    }
}
