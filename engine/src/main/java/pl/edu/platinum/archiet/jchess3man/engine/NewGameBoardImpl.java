package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;

import javaslang.collection.HashMap;

import java.nio.channels.SeekableByteChannel;

import static pl.edu.platinum.archiet.jchess3man.engine.CastlingVector.kfm;

/**
 * Created by Michał Krzysztof Feiler on 18.02.17.
 */
public class NewGameBoardImpl implements ImmutableBoard {
    public static final NewGameBoardImpl c = new NewGameBoardImpl();

    /**
     * @return not really a copy, just the same singleton constant
     */
    @NotNull
    @Override
    public ImmutableBoard copy() {
        return c;
    }

    /**
     * @return filled MutableHashMapBoardImpl
     */
    @NotNull
    @Override
    public MutableBoard mutableCopy() {
        MutableBoard mut = new MutableHashMapBoardImpl();
        mut.fill(c);
        return mut;
    }

    @Override
    public @Nullable Fig get(int rank, int file) {
        return Pos.getNewGame(rank, file);
    }

    @Override
    public @Nullable Fig get(@NotNull Pos pos) {
        return pos.getNewGame();
    }

    @Override
    public boolean isEmpty(int rank, int file) {
        return rank > 1;
    }

    @Override
    public boolean isEmpty(@NotNull Pos pos) {
        return pos.emptyOnNewGame();
    }

    @Override
    @NotNull
    @Contract(pure = true, value = "null -> fail")
    public Pos _whereIsKing(@NotNull Color who) {
        return Pos.newGameKingPos(who);
    }

    public static Seq<Integer> filesForColor(Color who) {
        int fir = who.segm() << 3;
        int nex = fir + 8;
        return Seq.range(fir, nex);
    }

    @Contract("null -> null; !null->!null")
    public static Seq<Pos> friends(@Nullable Color who) {
        if (who == null) return null;
        return filesForColor(who)
                .flatMap(file -> Seq.of(0, 1).map(rank -> new Pos(rank, file)));
    }

    @Contract("!null -> !null")
    private static Seq<Pos> sNE(@Nullable Seq<Pos> seq) {
        if (seq == null) return Seq.empty();
        return seq;
    }

    public static Seq<Pos> notFriends(Color who, PlayersAlive pa) {
        Color n = who.next();
        Color p = who.previous();
        n = pa.get(n) ? n : null;
        p = pa.get(p) ? p : null;
        Seq<Pos> sN = friends(n);
        Seq<Pos> sP = friends(p);
        if (sN == null && sP == null) return Seq.empty();
        if (sN == null) return sP;
        if (sP == null) return sN;
        return Seq.concat(sN, sP);
    }

    @Override
    public Tuple2<Seq<Pos>, Seq<Pos>> friendsAndOthers(Color who, PlayersAlive pa) {
        return new Tuple2<>(
                sNE(friends(pa.get(who) ? who : null)),
                notFriends(who, pa));
    }

    @Override
    public ImmutableBoard put(@NotNull Pos pos, @Nullable Fig fig) {
        return new DiffNGBoardImpl(HashMap.of(pos, fig));
    }
}
