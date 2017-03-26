package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by Michał Krzysztof Feiler on 18.02.17.
 */
public class EmptyBoardImpl implements ImmutableBoard {
    public static final EmptyBoardImpl c = new EmptyBoardImpl();

    @NotNull
    @Override
    public Map<Pos, Fig> toMapOfFigs() {
        return Collections.emptyMap();
    }

    @Override
    public @NotNull ImmutableBoard copy() {
        return c;
    }

    @NotNull
    @Override
    public MutableBoard mutableCopy() {
        return new MutableHashMapBoardImpl();
    }

    @Override
    @Contract("_->null")
    public Fig get(@NotNull Pos pos) {
        return null;
    }

    @Override
    @Contract("_,_->null")
    public @Nullable Fig get(int rank, int file) {
        return null;
    }

    @Override
    @Contract("_,_->true")
    public boolean isEmpty(int rank, int file) {
        return true;
    }

    @Override
    @Contract("_->true")
    public boolean isEmpty(@NotNull Pos pos) {
        return true;
    }

    @Override
    @Contract("_->null")
    public @Nullable Pos _whereIsKing(@NotNull Color who) {
        return null;
    }

    @NotNull
    @Override
    public Optional<Pos> whereIsKing(@NotNull Color who) {
        return Optional.empty();
    }

    @Override
    @Contract("_->true")
    public boolean checkEmpties(@NotNull Iterable<Pos> which) {
        return true;
    }

    @Override
    public Stream<FriendOrNot> friendsAndNot(Color who, PlayersAlive pa) {
        return Stream.empty();
    }

    @Override
    public Tuple2<Seq<Pos>, Seq<Pos>> friendsAndOthers(Color who, PlayersAlive pa) {
        return new Tuple2<>(Seq.empty(), Seq.empty());
    }
}
