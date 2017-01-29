package pl.edu.platinum.archiet.jchess3man.engine;


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

    public abstract AxisVector head();

    public AxisVector head(int ignored) {
        return head();
    }

    public abstract Vector tail(int fromRank);

}
