package pl.edu.platinum.archiet.jchess3man.engine;

import javaslang.Tuple2;
import javaslang.collection.HashMap;
import javaslang.collection.Set;
import javaslang.control.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * Created by Michał Krzysztof Feiler on 25.03.17.
 * This is an immutable and persistent (i.e. functional)
 * implementation of (Immutable)Board utilizing
 * javaslang.collection.HashMap and storing difference from newGame
 */
public class DiffNGBoardImpl implements ImmutableBoard {
    public final @NotNull HashMap<@NotNull Pos, @Nullable Fig> b;

    public DiffNGBoardImpl(@NotNull HashMap<@NotNull Pos, @Nullable Fig> b) {
        this.b = b;
    }

    public DiffNGBoardImpl(@NotNull NewGameBoardImpl s) {
        this(HashMap.empty());
    }

    @NotNull
    public static DiffNGBoardImpl empty() {
        return new DiffNGBoardImpl(NewGameBoardImpl.c);
    }

    @Override
    public DiffNGBoardImpl put(@NotNull Pos pos, @Nullable Fig fig) {
        final boolean equality =
                fig == null ? pos.emptyOnNewGame() : fig.equals(pos.getNewGame());
        final @NotNull HashMap<@NotNull Pos, @Nullable Fig>
                ret = equality ? b.remove(pos) : b.put(pos, fig);
        return new DiffNGBoardImpl(ret);
    }

    /**
     * @return not really a copy, just new([this.b])
     */
    @NotNull
    @Override
    public DiffNGBoardImpl copy() {
        return new DiffNGBoardImpl(b);
    }

    @Override
    public @Nullable Fig get(@NotNull Pos pos) {
        Option<@Nullable Fig> d = getDiffOpt(pos);
        return d.getOrElse(pos::getNewGame);
    }

    public @NotNull Option<@Nullable Fig> getDiffOpt(@NotNull Pos pos) {
        return b.get(pos);
    }

    @Override
    public boolean isEmpty(@NotNull Pos pos) {
        Option<@Nullable Fig> d = getDiffOpt(pos);
        return d.map(Objects::isNull).getOrElse(pos::emptyOnNewGame);
    }

    @NotNull
    @Override
    public Optional<Pos> whereIsKing(@NotNull Color who) {
        final Pos def = Pos.newGameKingPos(who);
        final Option<Fig> inDiff = b.get(def);
        if (inDiff.isEmpty()) return Optional.of(def);

        final Fig aKing = new Fig.King(who);
        if (inDiff.get().equals(aKing)) return Optional.of(def);

        final HashMap<@NotNull Pos, @Nullable Fig> filterKingsofColor = b.filter(
                (Tuple2<@NotNull Pos, @Nullable Fig> tup) -> aKing.equals(tup._2));

        final Set<@NotNull Pos> posSet = filterKingsofColor.keySet();
        final Option<@NotNull Pos> anyOfThem = posSet.toOption();
        return anyOfThem.toJavaOptional();
    }
}
