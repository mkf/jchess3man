package pl.edu.platinum.archiet.jchess3man.engine.helpers;

/**
 * Created by Michał Krzysztof Feiler on 02.02.17.
 */
public class BooleanHelpers {
    /**
     * @noinspection BooleanMethodIsAlwaysInverted
     */
    public static boolean beq(Boolean obj, boolean eqTo) {
        return obj != null && obj == eqTo;
    }
}
