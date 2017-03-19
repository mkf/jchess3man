package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michał Krzysztof Feiler on 02.02.17.
 */
public class Piece {
    public final FigType type;
    public final Color color;

    public Piece(FigType type, Color color) {
        this.type = type;
        this.color = color;
    }

    public static final Map<FigType, Map<Color, Character>> runeMap;

    @NotNull
    @Contract("_ -> !null")
    private static Map<Color, Character> bCM(char... c) {
        return Collections.unmodifiableMap(new HashMap<Color, Character>() {{
            //put(Color.ZeroColor, c[0]);
            put(null, c[0]);
            put(Color.White, c[1]);
            put(Color.Gray, c[2]);
            put(Color.Black, c[3]);
        }});
    }

    static {
        runeMap = Collections.unmodifiableMap(new HashMap<FigType, Map<Color, Character>>() {{
            //put(FigType.ZeroFigType, bCM('~', '=', (char) 0x2014, '-'));
            put(FigType.Rook, bCM('?', 'R', (char) 0x2656, 'r'));
            put(FigType.Knight, bCM('?', 'N', (char) 0x2658, 'n'));
            put(FigType.Bishop, bCM('?', 'B', (char) 0x2657, 'b'));
            put(FigType.Queen, bCM('?', 'Q', (char) 0x2655, 'q'));
            put(FigType.King, bCM('?', 'K', (char) 0x2654, 'k'));
            put(FigType.Pawn, bCM('?', 'P', (char) 0x2659, 'p'));
        }});
    }

    public char toChar() {
        return runeMap.get(type).get(color);
    }

    public Character toCharacter() {
        return runeMap.get(type).get(color);
    }

    public String toString() {
        return toCharacter().toString();
    }

    public boolean equals(Object ano) {
        return (ano instanceof Piece) && equals((Piece) ano);
    }

    public boolean equals(Piece ano) {
        return type == ano.type && color == ano.color;
    }

    public int hashCode() {
        return color.index << 3 | type.index;
    }
}
