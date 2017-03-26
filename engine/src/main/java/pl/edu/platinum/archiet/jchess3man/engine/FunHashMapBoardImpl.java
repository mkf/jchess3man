package pl.edu.platinum.archiet.jchess3man.engine;

import javaslang.collection.Array;
import javaslang.collection.HashMap;
import javaslang.control.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Created by Michał Krzysztof Feiler on 25.03.17.
 * This is an immutable and persistent (i.e. functional)
 * implementation of (Immutable)Board utilizing
 * javaslang.collection.HashMap
 */
public class FunHashMapBoardImpl implements ImmutableBoard {
    public final @NotNull HashMap<@NotNull Pos, @NotNull Fig> b;

    public FunHashMapBoardImpl(@NotNull HashMap<@NotNull Pos, @NotNull Fig> b) {
        this.b = b;
    }

    public FunHashMapBoardImpl(Board source) {
        this.b = HashMap.ofAll(source.toMapOfFigs()).filter(tup -> tup._2 != null);
    }

    @Override
    public FunHashMapBoardImpl put(@NotNull Pos pos, @Nullable Fig fig) {
        if (fig == null) return clr(pos);
        return new FunHashMapBoardImpl(b.put(pos, fig));
    }

    @Override
    public FunHashMapBoardImpl clr(@NotNull Pos pos) {
        return new FunHashMapBoardImpl(b.remove(pos));
    }

    /**
     * @return not really a copy, just new([this.b])
     */
    @NotNull
    @Override
    public FunHashMapBoardImpl copy() {
        return new FunHashMapBoardImpl(b);
    }

    @Override
    public @Nullable Fig get(@NotNull Pos pos) {
        final Optional<@NotNull Fig> ret = getOpt(pos);
        return ret.isPresent() ? ret.get() : null;
    }

    public @NotNull Optional<Fig> getOpt(@NotNull Pos pos) {
        return b.get(pos).toJavaOptional();
    }

    @Override
    public boolean isEmpty(@NotNull Pos pos) {
        return !b.containsKey(pos);
    }

    @NotNull
    @Override
    public Optional<Pos> whereIsKing(@NotNull Color who) {
        final Fig aKing = new Fig.King(who);
        return b.filter(tup -> tup._2.equals(aKing))
                .keySet().toJavaStream().findAny();
    }
}
