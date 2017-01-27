package pl.edu.platinum.archiet.jchess3man.engine;

/**
 * Created by Michał Krzysztof Feiler on 24.01.17.
 */
public class Color {
    //public final byte index;
    public final int index;

    public Color(/*byte*/ int index) { this.index = index; assert (index>=0 && index<4); }
    public Color(short index) { this((int) index); }
    public Color(byte index) { this((int) index); }
    public static Color fromSegm(byte segm) { return new Color(segm+1); }
    public static Color fromSegm(short segm) { return new Color(segm+1); }
    public static Color fromSegm(int segm) { return new Color(segm+1); }
    public static Color zeroColor = new Color(0);
    public static Color white = new Color(1);
    public static Color gray = new Color(2);
    public static Color black = new Color(3);
    public int toInt() {return /*(int)*/ index; }
    public short toShort() {return (short) index; }
    public short toByte() {return (byte) index; }
    public boolean equals(Object obj) {
        if(!(obj instanceof Color)) return false;
        Color col = (Color) obj;
        return equals(col);
    }
    public boolean equals(Color other) {
        return (other.index==index);
    }
    public int hashCode() { return toInt(); }
    public Color next() {
        switch (this) {
            case zeroColor: return white;
            case white: return gray;
            case gray: return black;
            case black: return white;
        }
    }
    public Color previous() {
        switch (this) {
            case zeroColor: return black;
            case white: return black;
            case gray: return white;
            case black: return gray;
        }
        throw new AssertionError();
    }
    public int board() { return index-1; }
    public static Color[] colors = {white, gray, black};
    public static String[] strings = {"ZeroColor", "White", "Gray", "Black"};
    public String toString() {return strings[index]; }
}
