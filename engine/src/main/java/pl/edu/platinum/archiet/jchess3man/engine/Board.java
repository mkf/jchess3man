package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
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

    /**
     * @return a nice String representation of the board
     */
    default @NotNull String string() {
        StringBuilder builder = new StringBuilder(300);
        builder.append('[');
        for (int i = 5; i >= 0; i--) {
            builder.append('[');
            for (int j = 0; j < 24; j++) {
                Optional<Fig> what = Optional.ofNullable(get(i, j));
                builder.append(what.map(Fig::toString).orElse("__"));
                builder.append(" ");
            }
            if (i != 0) builder.append("] \n ");
        }
        builder.append("]]");
        return builder.toString();
    }

    /**
     * @return a List of six ranks (Lists of squares in a rank by files)
     */
    default @NotNull List<@NotNull List<@Nullable Fig>> toListOfRanksOfFiles() {
        ArrayList<@NotNull List<@Nullable Fig>> ret = new ArrayList<>();
        for (int i = 0; i < 6; i++) ret.add(i, toListOfSquaresInRank(i));
        return ret;
    }

    /**
     * @param rank which rank to return
     * @return rank — a List of squares in it
     */
    default @NotNull List<@Nullable Fig> toListOfSquaresInRank(int rank) {
        ArrayList<@Nullable Fig> ret = new ArrayList<>(24);
        for (int i = 0; i < 24; i++) ret.add(get(rank, i));
        return ret;
    }

    /**
     * @return list of all squares from (0,0) thru (0,1), (1,0), (1,2), (5,22) to (5,23)
     */
    default @NotNull List<@Nullable Fig> toListOfSquaresConcatRanks() {
        ArrayList<@Nullable Fig> ret = new ArrayList<>();
        for (int i = 0; i < 24 * 6; i++)
            ret.add(i, get(idx(true, i)));
        return ret;
    }

    /**
     * @return array of all squares from (0,0) thru (0,1), (1,0), (1,2), (5,22) to (5,23)
     */
    default @Nullable Fig[] toArrayOfSquaresConcatRanks() {
        @Nullable Fig[] ret = new Fig[24 * 6];
        for (int i = 0; i < 24 * 6; i++)
            ret[i] = get(idx(true, i));
        return ret;
    }

    default Seq<@Nullable Fig> toSeqOfSquaresConcatRanks() {
        return Seq.range(0, 24 * 6)
                .map(i -> idx(true, i))
                .map(this::get);
    }

    default Seq<Integer> toSeqOfSevenBitConcatRanks() {
        return toSeqOfSquaresConcatRanks()
                .map(Fig::toSevenBitInt);
    }

    default Seq<Tuple2<Integer, Integer>>
    toSeqOfFirstThreeAndAnotherFourBitsConcatRanks() {
        return toSeqOfSevenBitConcatRanks()
                .map(t -> new Tuple2<>((t & 0xF0) >> 4, t & 0xF));
    }

    default Seq<Tuple2<String, String>>
    toSeqOfSevenBitAsTwoHexDigitsConcatRanks() {
        return toSeqOfFirstThreeAndAnotherFourBitsConcatRanks()
                .map(t -> t
                        .map1(Integer::toHexString)
                        .map2(Integer::toHexString));
    }

    default Seq<String>
    toSeqOfJoinedHexDigitsOfSevenBitConcatRanks() {
        return toSeqOfSevenBitAsTwoHexDigitsConcatRanks()
                .map(t -> t.v1 + t.v2);
    }

    default Seq<Integer> toSeqOfSeparatedHexDigitsOfSevenBitConcatRanks() {
        return toSeqOfFirstThreeAndAnotherFourBitsConcatRanks()
                .flatMap(t -> Seq.of(t.v1, t.v2));
    }

    default Seq<String> toSeqOfHexSevenBitConcatRanks() {
        return toSeqOfSeparatedHexDigitsOfSevenBitConcatRanks()
                .map(Integer::toHexString);
    }

    default String toHexConcatRanks() {
        return toSeqOfHexSevenBitConcatRanks()
                .collect(Collectors.joining());
    }

    /**
     * @param rnf  24 files, 24 files, 24 files. Else 6 ranks, 6 ranks, 6 ranks...
     * @param rank rank in corresponding position
     * @param file file in corresponding position
     * @return corresponding array index
     */
    @Contract(pure = true)
    static int idx(boolean rnf, int rank, int file) {
        return !rnf ? file * 6 + rank : rank * 24 + file;
    }

    /**
     * @param rnf 24 files, 24 files, 24 files. Else 6 ranks, 6 ranks, 6 ranks...
     * @param idx index in such an array
     * @return corresponding position
     */
    @Contract(pure = true)
    static Pos idx(boolean rnf, int idx) {
        return !rnf ? new Pos(idx % 6, idx / 6) : new Pos(idx / 24, idx % 24);
    }

    /**
     * @return list of all squares from (0,0) thru (1,0), (0,1), (1,1), (2,1), (1,2), (5,22), (4,23) to (5,23)
     */
    default @NotNull List<@Nullable Fig> toListOfSquaresConcatFiles() {
        ArrayList<@Nullable Fig> ret = new ArrayList<>();
        for (int i = 0; i < 24 * 6; i++)
            ret.add(i, get(idx(false, i)));
        return ret;
    }

    /**
     * @return array of all squares from (0,0) thru (1,0), (0,1), (1,1), (2,1), (1,2), (5,22), (4,23) to (5,23)
     */
    default @Nullable Fig[] toArrayOfSquaresConcatFiles() {
        @Nullable Fig[] ret = new Fig[24 * 6];
        for (int i = 0; i < 24 * 6; i++)
            ret[i] = get(idx(true, i));
        return ret;
    }

    /**
     * @return a Map of all not empty squares by positions
     */
    default @NotNull Map<@NotNull Pos, @Nullable Fig> toMapOfFigs() {
        HashMap<@NotNull Pos, @Nullable Fig> ret = new HashMap<>(144);
        AllPosIterable allPosIterable = new AllPosIterable();
        for (Pos pos : allPosIterable) {
            if (!isEmpty(pos)) ret.put(pos, get(pos));
        }
        return ret;
    }

    /**
     * A copy() method what we don't really use nor implement but we probably should
     *
     * @return this, new this(this.b) or something like that or a real copy sometimes
     */
    @Contract(pure = true)
    @NotNull Board copy();

    /**
     * @return now this one is a real copy, it is always a new object of type MutableBoard
     * (often and by default MutableHashMapBoardImpl) that is then filled from the source board
     */
    @Contract(pure = true)
    default @NotNull MutableBoard mutableCopy() {
        MutableBoard board = new MutableHashMapBoardImpl();
        board.fill(this);
        return board;
    }

    @Contract(pure = true)
    default @NotNull ImmutableBoard immutable() {
        return new FunHashMapBoardImpl(this);
    }

    /**
     * @param pos position to check
     * @return either a fig that is there or null if it's empty
     */
    @Contract(pure = true, value = "null -> fail")
    @Nullable
    default Fig get(@NotNull Pos pos) {
        return get(pos.rank, pos.file);
    }

    /**
     * @param rank which rank (from 0 most outward to 5 near the center) to get square
     * @param file which file (from left queenside white 0 to right black kingside 23) to get square
     * @return either a fig that is there or null if it's empty
     */
    @Contract(pure = true)
    @Nullable
    default Fig get(int rank, int file) {
        return get(new Pos(rank, file));
    }

    /**
     * @param rank which rank (from 0 most outward to 5 near the center) to get square
     * @param file which file (from left queenside white 0 to right black kingside 23) to get square
     * @return whether that square is empty
     */
    @Contract(pure = true)
    default boolean isEmpty(int rank, int file) {
        return get(rank, file) == null;
    }

    /**
     * @param pos position to check
     * @return whether that square is empty
     */
    @Contract(pure = true, value = "null -> fail")
    default boolean isEmpty(@NotNull Pos pos) {
        return isEmpty(pos.rank, pos.file);
    }

    /**
     * @param who color of the king we are looking for
     * @return first position where there is a king of that color or null if none found
     */
    @Nullable
    default Pos _whereIsKing(@NotNull Color who) {
        final Fig.King suchKing = new Fig.King(who);
        for (final Pos pos : new AllPosIterable())
            if (suchKing.equals(get(pos))) return pos;
        return null;
    }

    /**
     * @param who color of the king we are looking for
     * @return first position where there is a king of that color or, if none found, empty Optional
     */
    default @NotNull Optional<Pos> whereIsKing(@NotNull Color who) {
        return ofNullable(_whereIsKing(who));
    }

    /**
     * @param to             the square that we want to know whether it is under a threat
     *                       such as a check
     * @param from           the square that the potentially threatening fig is
     * @param playersAlive   which players are still active
     * @param enPassantStore where one could capture en passant
     * @return whether there is such a threat
     */
    default boolean isThereAThreat(
            @NotNull Pos to,
            @NotNull Pos from,
            @NotNull PlayersAlive playersAlive,
            @NotNull EnPassantStore enPassantStore
    ) {
        Fig fig = get(from);
        if (fig == null) throw new NullPointerException(from.toString());
        return isThereAThreat(to, from, playersAlive, enPassantStore, fig);
    }

    /**
     * @param to the square that we want to know whether it is under a threat
     *           such as a check
     * @param from the square that the potentially threatening fig is
     * @param playersAlive which players are still active
     * @param enPassantStore where one could capture en passant
     * @param fig what is the fig at [from]?
     * @return whether there is such a threat
     */
    default boolean isThereAThreat(
            @NotNull Pos to,
            @NotNull Pos from,
            @NotNull PlayersAlive playersAlive,
            @NotNull EnPassantStore enPassantStore,
            @NotNull Fig fig
    ) {
        return isThereAThreat(to, from, playersAlive, enPassantStore,
                fig.vecs(from, to));
    }

    /**
     * @param to the square that we want to know whether it is under a threat
     *           such as a check
     * @param from the square that the potentially threatening fig is
     * @param playersAlive which players are still active
     * @param enPassantStore where one could capture en passant
     * @param vecs not taking the board state into considerations, what are the
     *             from [from] to [to] for the threatening fig?
     * @return whether there is such a threat
     */
    default boolean isThereAThreat(
            @NotNull Pos to,
            @NotNull Pos from,
            @NotNull PlayersAlive playersAlive,
            @NotNull EnPassantStore enPassantStore,
            @NotNull Iterable<? extends Vector> vecs
    ) {
        return isThereAThreat(to, from, playersAlive, enPassantStore,
                StreamSupport
                        .stream(vecs.spliterator(), false)
        );
    }

    /**
     * @param to the square that we want to know whether it is under a threat
     *           such as a check
     * @param from the square that the potentially threatening fig is
     * @param playersAlive which players are still active
     * @param enPassantStore where one could capture en passant
     * @param vecs not taking the board state into considerations, what are the
     *             from [from] to [to] for the threatening fig?
     * @return whether there is such a threat
     */
    default boolean isThereAThreat(
            @NotNull Pos to,
            @NotNull Pos from,
            @NotNull PlayersAlive playersAlive,
            @NotNull EnPassantStore enPassantStore,
            @NotNull Stream<@NotNull ? extends Vector> vecs
    ) {
        GameState before = new GameState(
                this, MoatsState.noBridges, null,
                CastlingPossibilities.zero, enPassantStore,
                0, 0, playersAlive);
        Stream<Boolean> streamOfBools = vecs.map(
                (Vector vec) -> {
                    try {
                        VecMove move = new VecMove(vec, from, before);
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

    /**
     * @param where the square that we want to know whether it is under a threat
     *              such as a check
     * @param pa    which players are still active
     * @param ep    where one could capture en passant
     * @return the positions of threatening enemy figs
     */
    default @NotNull Stream<@NotNull Pos> threatChecking(
            @NotNull Pos where,
            @NotNull PlayersAlive pa, @NotNull EnPassantStore ep) {
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

    /**
     * @param who whose king do we want to examine situation
     * @param pa  which players are still active
     * @return the positions of the enemy figs which are threatening Who's king
     */
    default @NotNull Stream<@NotNull Pos> checkChecking(
            @NotNull Color who, @NotNull PlayersAlive pa) {
        assert (pa.get(who));
        Pos kingPos = _whereIsKing(who);
        assert (kingPos != null);
        return threatChecking(kingPos, pa, EnPassantStore.empty);
    }

    /**
     * Checks whether all positions in [which] are empty
     *
     * @param which positions to check
     * @return true if all are empty, false if at least one is not
     */
    default boolean checkEmpties(@NotNull Iterable<@NotNull Pos> which) {
        for (final Pos pos : which) if (!isEmpty(pos)) return false;
        return true;
    }

    /**
     * Simple struct containing position [pos] and boolean [friend].
     */
    class FriendOrNot {
        public final boolean friend;
        public final @NotNull Pos pos;

        public FriendOrNot(boolean friend, @NotNull Pos pos) {
            this.friend = friend;
            this.pos = pos;
        }
    }

    default boolean equals(@NotNull Board ano) {
        for (Pos pos : new AllPosIterable()) {
            Fig a = get(pos);
            Fig b = ano.get(pos);
            if (a == b || a != null && a.equals(b))
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
                            assert fig != null;
                            return isThereAThreat(ich, nasz, pa, ep, fig)
                                    ? Stream.of(fig.type) : Stream.empty();
                        }));
        List<Pos> oth = othersT.v2.toList();
        Seq<FigType> ed = friendsT.v2.parallel()
                .flatMap(nasz -> Seq.seq(oth)
                        .flatMap(ich -> {
                            Fig fig = get(nasz);
                            assert fig != null;
                            return isThereAThreat(nasz, ich, pa, ep, fig)
                                    ? Stream.of(fig.type) : Stream.empty();
                        }));
        return new Tuple2<>(ing, ed);
    }
}

