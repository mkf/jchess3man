package pl.edu.platinum.archiet.jchess3man.engine;

/**
 * Created by Michał Krzysztof Feiler on 29.01.17.
 */
public class PawnWalkVector extends RankVector implements PawnVector {
    public PawnWalkVector(boolean direc) {
        super(1, direc);
    }


    @Override
    public boolean reqpc() {
        return !direc;
    }

    @Override
    public boolean reqProm(int rank) {
        return false;
    }

    @Override
    public PawnWalkVector head(int ignored) {
        return this;
    }

    @Override
    public ZeroVector tail(int ignored) {
        return new ZeroVector();
    }
}
