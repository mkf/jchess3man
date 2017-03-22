package pl.edu.platinum.archiet.jchess3man.engine;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Michał Krzysztof Feiler on 27.01.17.
 */
public abstract class AxisVector extends ContinuousVector {
    public final boolean direc;

    public AxisVector(int abs, boolean direc) {
        super(abs);
        this.direc = direc;
    }

    public AxisVector(int t) {
        super(Math.abs(t));
        this.direc = (t > 0);
    }

    public int t() {
        return direc ? abs : -abs;
    }

    public abstract @NotNull AxisVector head();

    public @NotNull AxisVector head(int ignored) {
        return head();
    }

    public abstract @Nullable AxisVector tail(int fromRank);

}
