package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Michał Krzysztof Feiler on 18.02.17.
 */
public class EmptyBoardImpl implements Board {
    public static final EmptyBoardImpl c = new EmptyBoardImpl();

    @Override
    public Map<Pos, Fig> toMapOfFigs() {
        return Collections.emptyMap();
    }

    @Override
    public Board copy() {
        return c;
    }

    @Override
    public MutableBoard mutableCopy() {
        return new MutableHashMapBoardImpl();
    }

    @Override
    public Fig get(Pos pos) {
        assert (pos != null);
        return null;
    }

    @Override
    public Fig get(int rank, int file) {
        return null;
    }

    @Override
    public boolean isEmpty(int rank, int file) {
        return false;
    }

    @Override
    public boolean isEmpty(Pos pos) {
        assert (pos != null);
        return false;
    }

    @Override
    public @Nullable Pos _whereIsKing(Color who) {
        return null;
    }

    @Override
    public Optional<Pos> whereIsKing(Color who) {
        return Optional.empty();
    }

    @Override
    public boolean checkEmpties(Iterable<Pos> which) {
        return true;
    }
}
