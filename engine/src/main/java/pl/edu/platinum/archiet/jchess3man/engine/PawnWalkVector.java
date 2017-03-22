package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    @NotNull
    @Contract("_ -> !null")
    public PawnWalkVector head(int ignored) {
        return this;
    }

    @Override
    @Nullable
    @Contract(pure = true, value = "_ -> null")
    public RankVector tail(int ignored) {
        return null;
    }
}
