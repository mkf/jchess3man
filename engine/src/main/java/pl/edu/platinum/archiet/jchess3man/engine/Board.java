package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Optional.ofNullable;

/**
 * Created by Michał Krzysztof Feiler on 02.02.17.
 */
public interface Board {

    default String string() {
        StringBuilder builder = new StringBuilder(300);
        builder.append('[');
        for (int i = 5; i >= 0; i--) {
            builder.append('[');
            for (int j = 0; j < 24; j++) {
                Optional<Fig> what = Optional.ofNullable(get(i, j));
                builder.append(what.isPresent() ? what.get().toString() : "__");
                builder.append(" ");
            }
            if (i != 0) builder.append("] \n ");
        }
        builder.append("]]");
        return builder.toString();
    }

    default List<List<Fig>> toListOfRanksOfFiles() {
        ArrayList<List<Fig>> ret = new ArrayList<>();
        for (int i = 0; i < 6; i++) ret.add(i, toListOfSquaresInRank(i));
        return ret;
    }

    default List<Fig> toListOfSquaresInRank(int rank) {
        ArrayList<Fig> ret = new ArrayList<>(24);
        for (int i = 0; i < 24; i++) ret.add(get(rank, i));
        return ret;
    }

    default List<Fig> toListOfSquares() {
        ArrayList<Fig> ret = new ArrayList<>();
        for (int i = 0; i < 24 * 6; i++)
            ret.add(i, get(i / 24, i % 24));
        return ret;
    }

    default Map<Pos, Fig> toMapOfFigs() {
        HashMap<Pos, Fig> ret = new HashMap<>(144);
        AllPosIterable allPosIterable = new AllPosIterable();
        for (Pos pos : allPosIterable) {
            if (!isEmpty(pos)) ret.put(pos, get(pos));
        }
        return ret;
    }

    @Contract(pure = true)
    Board copy();

    @Contract(pure = true)
    MutableBoard mutableCopy();

    //List<Fig> toListOfSquares();
    @Contract(pure = true, value = "null -> fail")
    @Nullable
    default Fig get(Pos pos) {
        return get(pos.rank, pos.file);
    }

    @Contract(pure = true)
    @Nullable
    default Fig get(int rank, int file) {
        return get(new Pos(rank, file));
    }

    @Contract(pure = true)
    default boolean isEmpty(int rank, int file) {
        return get(rank, file) == null;
    }

    @Contract(pure = true, value = "null -> fail")
    default boolean isEmpty(Pos pos) {
        return isEmpty(pos.rank, pos.file);
    }

    @Nullable
    default Pos _whereIsKing(Color who) {
        final Fig.King suchKing = new Fig.King(who);
        for (final Pos pos : new AllPosIterable())
            if (suchKing.equals(get(pos))) return pos;
        return null;
    }

    default Optional<Pos> whereIsKing(Color who) {
        return ofNullable(_whereIsKing(who));
    }

    default boolean isThereAThreat(
            Pos to,
            Pos from,
            PlayersAlive playersAlive,
            EnPassantStore enPassantStore
    ) {
        return isThereAThreat(to, from, playersAlive, enPassantStore, get(from));
    }

    default boolean isThereAThreat(
            Pos to,
            Pos from,
            PlayersAlive playersAlive,
            EnPassantStore enPassantStore,
            Fig fig
    ) {
        return isThereAThreat(to, from, playersAlive, enPassantStore,
                fig.vecs(from, to));
    }

    default boolean isThereAThreat(
            Pos to,
            Pos from,
            PlayersAlive playersAlive,
            EnPassantStore enPassantStore,
            Iterable<? extends Vector> vecs
    ) {
        return isThereAThreat(to, from, playersAlive, enPassantStore,
                StreamSupport
                        .stream(vecs.spliterator(), false)
        );
    }

    default boolean isThereAThreat(
            Pos to,
            Pos from,
            PlayersAlive playersAlive,
            EnPassantStore enPassantStore,
            Stream<? extends Vector> vecs
    ) {
        GameState before = new GameState(
                this, MoatsState.noBridges, null,
                CastlingPossibilities.zero, enPassantStore,
                0, 0, playersAlive);
        Stream<Boolean> streamOfBools = vecs.map(
                (Vector vec) -> {
                    try {
                        Move move = new Move(vec, from, before);
                        Optional<Impossibility> impossibilityOptional =
                                move.checkPossibilityOppositeColor();
                        return !impossibilityOptional.isPresent() ||
                                Impossibility.canI(impossibilityOptional.get());
                    } catch (VectorAdditionFailedException e) {
                        e.printStackTrace();
                        throw new AssertionError(e);
                    } catch (NeedsToBePromotedException ignored) {
                        return true;
                    }
                }
        );
        //Stream<Boolean> streamOfBools = streamOfImpossibilities
        //        .map((Impossibility impos) -> Impossibility.canI(impos));
        Optional<Boolean> ourEnd = streamOfBools
                .filter(Boolean::booleanValue).findFirst();
        return ourEnd.isPresent();
    }

    default Stream<Pos> threatChecking(Pos where, PlayersAlive pa, EnPassantStore ep) {
        //noinspection ConstantConditions
        Color who = get(where).color;
        Stream<Pos> first = StreamSupport.stream(
                new AllPosIterable().spliterator(), false);
        return first.flatMap((Pos pos) -> {
            Fig tjf = get(pos);
            return tjf != null && tjf.color != who && pa.get(tjf.color) &&
                    isThereAThreat(where, pos, pa, ep, tjf)
                    ? Stream.of(pos) : Stream.empty();
        });
    }

    default Stream<Pos> checkChecking(Color who, PlayersAlive pa) {
        assert (pa.get(who));
        return threatChecking(_whereIsKing(who), pa, EnPassantStore.empty);
    }

    /**
     * Checks whether all positions in [which] are empty
     *
     * @param which positions to check
     * @return true if all are empty, false if at least one is not
     */
    default boolean checkEmpties(Iterable<Pos> which) {
        for (final Pos pos : which) if (!isEmpty(pos)) return false;
        return true;
    }

    class FriendOrNot {
        public final boolean friend;
        public final Pos pos;

        public FriendOrNot(boolean friend, Pos pos) {
            this.friend = friend;
            this.pos = pos;
        }
    }

    default boolean equals(Board ano) {
        for (Pos pos : new AllPosIterable()) {
            Fig a = get(pos);
            Fig b = ano.get(pos);
            if (a == b || a.equals(b))
                return false;
        }
        return true;
    }

    default Stream<FriendOrNot> friendsAndNot(Color who, PlayersAlive pa) {
        if (pa.get(who)) {
            Stream<Pos> allPos = StreamSupport.stream(
                    new AllPosIterable().spliterator(), false);
            return allPos.flatMap((Pos pos) -> {
                final Fig tjf = get(pos);
                if (tjf == null || !pa.get(tjf.color)) return Stream.empty();
                return Stream.of(new FriendOrNot(tjf.color == who, pos));
            });
        } else return Stream.empty();
    }

    default Tuple2<Seq<Pos>, Seq<Pos>> friendsAndOthers(Color who, PlayersAlive pa) {
        Stream<FriendOrNot> stream = friendsAndNot(who, pa);
        return friendsAndOthers(stream);
    }

    static Tuple2<Seq<Pos>, Seq<Pos>> friendsAndOthers(Stream<FriendOrNot> stream) {
        Tuple2<Seq<FriendOrNot>, Seq<FriendOrNot>> duplicated =
                Seq.seq(stream).duplicate();
        Tuple2<Seq<FriendOrNot>, Seq<FriendOrNot>> friendsAndOthers =
                duplicated.v1.partition(some -> some.friend);
        Seq<Pos> friends = friendsAndOthers.v1.map(some -> some.pos);
        Seq<Pos> others = friendsAndOthers.v2.map(some -> some.pos);
        return new Tuple2<>(friends, others);
    }

    default Tuple2<Seq<FigType>, Seq<FigType>> threateningAndThreatened(
            Color who, PlayersAlive pa, EnPassantStore ep
    ) {
        Tuple2<Seq<Pos>, Seq<Pos>> fAO = friendsAndOthers(who, pa);
        return threateningAndThreatened(who, pa, ep, fAO);
    }

    default Tuple2<Seq<FigType>, Seq<FigType>> threateningAndThreatened(
            Color who, PlayersAlive pa, EnPassantStore ep,
            Stream<FriendOrNot> stream
    ) {
        Seq<FriendOrNot> primary = Seq.seq(stream);
        return threateningAndThreatened(who, pa, ep, friendsAndOthers(primary));
    }

    default Tuple2<Seq<FigType>, Seq<FigType>> threateningAndThreatened(
            Color who, PlayersAlive pa, EnPassantStore ep,
            Tuple2<Seq<Pos>, Seq<Pos>> fAO
    ) {
        Seq<Pos> friends = fAO.v1;
        Seq<Pos> others = fAO.v2;
        return threateningAndThreatened(who, pa, ep, friends, others);
    }

    default Tuple2<Seq<FigType>, Seq<FigType>> threateningAndThreatened(
            Color who, PlayersAlive pa, EnPassantStore ep,
            Seq<Pos> friends, Seq<Pos> others
    ) {
//           others.forEach(ich -> friends.forEach(nasz -> ));
        Tuple2<Seq<Pos>, Seq<Pos>> friendsT = friends.duplicate();
        Tuple2<Seq<Pos>, Seq<Pos>> othersT = others.duplicate();
        List<Pos> free = friendsT.v1.toList();
        Seq<FigType> ing = othersT.v1.parallel()
                .flatMap(ich -> Seq.seq(free)
                        .flatMap(nasz -> {
                            Fig fig = get(ich);
                            return isThereAThreat(ich, nasz, pa, ep, fig)
                                    ? Stream.of(fig.type) : Stream.empty();
                        }));
        List<Pos> oth = othersT.v2.toList();
        Seq<FigType> ed = friendsT.v2.parallel()
                .flatMap(nasz -> Seq.seq(oth)
                        .flatMap(ich -> {
                            Fig fig = get(nasz);
                            return isThereAThreat(nasz, ich, pa, ep, fig)
                                    ? Stream.of(fig.type) : Stream.empty();
                        }));
        return new Tuple2<>(ing, ed);
    }
}

