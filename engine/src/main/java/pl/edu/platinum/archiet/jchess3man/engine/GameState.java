package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.lambda.Seq;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by Michał Krzysztof Feiler on 03.02.17.
 * GameState describes a single game state, i.e.: board state, moats state,
 * which color moves next, what castlings are possible, where one could
 * capture en passant, how many "half moves" are on the counter and
 * how many moves have been done since the beginning, and which players
 * are still active.
 * GameState aims at immutability, though it happens that there is an
 * extension to interface [Board] called [MutableBoard].
 * One simply shouldn't, though, modify board after assigning it to a GameState.
 */
public class GameState {
    /**
     * Board describes board state
     */
    public final Board board;
    /**
     * moatsState tells us which moats were bridged already
     */
    public final MoatsState moatsState;
    /**
     * movesNext is the color of the player who moves next
     */
    public final Color movesNext;
    /**
     * castlingPossibilities is about which players could still perform
     * a queenside or a kingside castling
     */
    public final CastlingPossibilities castlingPossibilities;
    /**
     * enPassantStore is about on which files you can capture en passant
     */
    public final EnPassantStore enPassantStore;
    /**
     * see https://chessprogramming.wikispaces.com/Halfmove+Clock
     * TODO: implement fifty-move rule and improve that counter as I ain't really sure whether it resets on all events it should
     */
    public final int halfMoveClock;
    /**
     * fullMoveNumber is the count of moves performed since the beginning of the game
     */
    public final int fullMoveNumber;
    /**
     * alivePlayers tells us which colors are still active
     */
    public final PlayersAlive alivePlayers;

    /**
     * Just a basic constructor for GameState
     *
     * @param board                 will be assigned as even if mutable
     * @param moatsState            will be assigned as is
     * @param movesNext             will be assigned as is
     * @param castlingPossibilities will be assigned as is
     * @param enPassantStore        will be assigned as is
     * @param halfMoveClock         will be assigned as is
     * @param fullMoveNumber        will be assigned as is
     * @param alivePlayers          will be assigned as is
     */
    public GameState(
            Board board,
            MoatsState moatsState,
            Color movesNext,
            CastlingPossibilities castlingPossibilities,
            EnPassantStore enPassantStore,
            int halfMoveClock,
            int fullMoveNumber,
            PlayersAlive alivePlayers) {
        this.board = board;
        this.moatsState = moatsState;
        this.movesNext = movesNext;
        this.castlingPossibilities = castlingPossibilities;
        this.enPassantStore = enPassantStore;
        this.halfMoveClock = halfMoveClock;
        this.fullMoveNumber = fullMoveNumber;
        this.alivePlayers = alivePlayers;
    }

    /**
     * A cloning constructor for GameState.
     * Everything will be assigned as is except Board if it
     * turns out to be a MutableBoard. In such a case,
     * source.board.mutableCopy() is assigned.
     *
     * @param source source GameState to copy
     */
    public GameState(GameState source) {
        this(
                (source.board instanceof MutableBoard)
                        ? source.board.mutableCopy()
                        : source.board,
                source.moatsState,
                source.movesNext,
                source.castlingPossibilities,
                source.enPassantStore,
                source.halfMoveClock,
                source.fullMoveNumber,
                source.alivePlayers
        );
    }

    /**
     * A new game state, i.e. new game board, no bridges,
     * white moves next, all castling possible, empty enPassantStore,
     * halfMoveClock and fullMoveNumber are at 0, all players active
     */
    public static final GameState newGame = new GameState(
            NewGameBoardImpl.c,
            MoatsState.noBridges,
            Color.White,
            CastlingPossibilities.all,
            EnPassantStore.empty,
            0, 0,
            PlayersAlive.all
    );

    /**
     * A new GameState with (if isPresent()) updated alivePlayers
     * @param source source for cloning
     * @param withPlayersAlive Optional of new PlayersAlive
     */
    public GameState(GameState source,
                     @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
                             Optional<PlayersAlive> withPlayersAlive
    ) {
        this(source, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), withPlayersAlive);
    }

    /**
     * A new GameState with updated alivePlayers
     *
     * @param source           source for cloning
     * @param withPlayersAlive new alivePlayers
     */
    public GameState(GameState source, @NotNull PlayersAlive withPlayersAlive) {
        this(source, Optional.of(withPlayersAlive));
    }

    /**
     * Cloning GameState with updated these fields which as parameters are not null here
     * @param source source GameState for copying
     * @param withBoard update of Board if not null
     * @param withMoatsState update of moatsState if not null
     * @param withMovesNext update of movesNext if not null
     * @param withCastlingPossibilities update of castlingPossibilities if not null
     * @param withEnPassantStore update of enPassantStore if not null
     * @param withHalfMoveClock update of halfMoveClock if not null
     * @param withFullMoveNumber update of fullMoveNumber if not null
     * @param withPlayersAlive update of alivePlayers if not null
     */
    public GameState(GameState source,
                     @Nullable Board withBoard,
                     @Nullable MoatsState withMoatsState,
                     @Nullable Color withMovesNext,
                     @Nullable CastlingPossibilities withCastlingPossibilities,
                     @Nullable EnPassantStore withEnPassantStore,
                     @Nullable Integer withHalfMoveClock,
                     @Nullable Integer withFullMoveNumber,
                     @Nullable PlayersAlive withPlayersAlive
    ) {
        this(
                source,
                Optional.ofNullable(withBoard),
                Optional.ofNullable(withMoatsState),
                Optional.ofNullable(withMovesNext),
                Optional.ofNullable(withCastlingPossibilities),
                Optional.ofNullable(withEnPassantStore),
                Optional.ofNullable(withHalfMoveClock),
                Optional.ofNullable(withFullMoveNumber),
                Optional.ofNullable(withPlayersAlive)
        );
    }

    /**
     * Cloning GameState with updated these fields which as parameters are isPresent() here
     * @param source source GameState for copying
     * @param withBoard update of Board if isPresent
     * @param withMoatsState update of moatsState if isPresent
     * @param withMovesNext update of movesNext if isPresent
     * @param withCastlingPossibilities update of castlingPossibilities if isPresent
     * @param withEnPassantStore update of enPassantStore if isPresent
     * @param withHalfMoveClock update of halfMoveClock if isPresent
     * @param withFullMoveNumber update of fullMoveNumber if isPresent
     * @param withPlayersAlive update of alivePlayers if isPresent
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public GameState(GameState source,
                     Optional<Board> withBoard,
                     Optional<MoatsState> withMoatsState,
                     Optional<Color> withMovesNext,
                     Optional<CastlingPossibilities> withCastlingPossibilities,
                     Optional<EnPassantStore> withEnPassantStore,
                     Optional<Integer> withHalfMoveClock,
                     Optional<Integer> withFullMoveNumber,
                     Optional<PlayersAlive> withPlayersAlive
    ) {
        this(
                withBoard.orElseGet(() -> (
                        (source.board instanceof MutableBoard) ? source.board.mutableCopy() : source.board)),
                withMoatsState.orElseGet(() -> source.moatsState),
                withMovesNext.orElseGet(() -> source.movesNext),
                withCastlingPossibilities.orElseGet(() -> source.castlingPossibilities),
                withEnPassantStore.orElseGet(() -> source.enPassantStore),
                withHalfMoveClock.orElseGet(() -> source.halfMoveClock),
                withFullMoveNumber.orElseGet(() -> source.fullMoveNumber),
                withPlayersAlive.orElseGet(() -> source.alivePlayers)
        );
    }

    /**
     * Whether the player is checked
     * @param who color if the checked player
     * @return stream of positions which are threatening our king
     */
    public Stream<Pos> amIinCheck(Color who) {
        return board.checkChecking(who, alivePlayers);
    }

    /**
     * Whether the player who moves next is checked
     * @return stream of positions which are threatening our king
     */
    public Stream<Pos> amIinCheck() {
        return amIinCheck(movesNext);
    }

    @Contract(pure = true)
    public boolean _canIMoveWOCheck(Color who) {
        return _canIMoveWOCheck(who, false);
    }

    @Contract(pure = true)
    public boolean _canIMoveWOCheck(Color who, boolean useImmutableAfterBoard) {
        for (final Pos from : new AllPosIterable())
            //noinspection ConstantConditions
            if (!board.isEmpty(from) && board.get(from).color == who)
                for (final Pos to : AMFT.getIterableFor(from))
                    //noinspection ConstantConditions
                    for (final Vector vec : board.get(from).vecs(from, to))
                        try {
                            final VecMove m = new VecMove(vec, from, this);
                            try {
                                //noinspection ResultOfMethodCallIgnored
                                m.afterWOEvaluatingDeath(useImmutableAfterBoard);
                            } catch (IllegalMoveException ignored) {
                                return true;
                            }
                        } catch (VectorAdditionFailedException e) {
                            e.printStackTrace();
                            throw new AssertionError(e);
                        } catch (NeedsToBePromotedException ignored) {
                        }
        return false;
    }

    public boolean canIMoveWOCheck(Color who, boolean mutabilitySafe) {
        if (mutabilitySafe && board instanceof MutableBoard) {
            return new GameState(this)._canIMoveWOCheck(who);
        }
        return _canIMoveWOCheck(who);
    }

    public PlayersAlive evalDeath() {
        boolean testCheckmate = true;
        Color player = movesNext;
        PlayersAlive pa = alivePlayers;
        for (int proceduralIndex = 0; proceduralIndex < 3; proceduralIndex++) {
            if (pa.get(player))
                if (testCheckmate)
                    if (!board.whereIsKing(player).isPresent())
                        pa = pa.die(player);
                    else if (!_canIMoveWOCheck(player))
                        pa = pa.die(player);
                    else {
                        testCheckmate = false;
                    }
                else if (!board.whereIsKing(player).isPresent())
                    pa = pa.die(player);
            player = player.next();
        }
        return pa;
    }

    public GameState evaluateDeathThrowingCheck(Color whatColor) throws WeInCheckException {
        return VecMove.evaluateDeathThrowingCheck(this, whatColor);
    }

    public void throwCheck(Color whatColor) throws WeInCheckException {
        VecMove.throwCheck(this, whatColor);
    }

    public GameState evaluateDeath() {
        return VecMove.evaluateDeath(this);
    }

    private Seq<DescMove> genDescMoves(Pos from, Pos to) {
        DescMove move = new DescMove(from, to, this);
        Stream<DescMove.EitherStateOrIllMoveExcept> afters;
        try {
            afters = move.generateAftersWOEvaluatingDeath();
        } catch (NeedsToBePromotedException e) {
            move = new DescMove(from, to, this, FigType.Queen);
            try {
                afters = move.generateAftersWOEvaluatingDeath();
            } catch (NeedsToBePromotedException e1) {
                e1.printStackTrace();
                throw new AssertionError(e1);
            }
        }
        Optional<GameState> any = afters
                .flatMap(DescMove.EitherStateOrIllMoveExcept::flatMapState)
                .findAny();
        if (any.isPresent()) {
            /*
            if (move.pawnPromotion == null)
                //return Seq.of(new Desc(from, to));
                return Seq.of(move);
            else return
                    Seq.of(FigType.Queen, FigType.Rook, FigType.Bishop, FigType.Knight)
                            .map(prom -> new Desc(from, to, prom));
                            */
            return move.promPossible();
        } else return Seq.empty();
    }

    public Seq<DescMove> genDescMoves() {
        Seq<Pos> ours = board.friendsAndOthers(movesNext, alivePlayers).v1.parallel();
        return ours.flatMap(from -> Seq.seq(AMFT.getIterableFor(from)).parallel()
                .flatMap(to -> genDescMoves(from, to)));
    }

    public Seq<GameState> genASAOM(Color ourColor) {
        if (!alivePlayers.get(ourColor) || movesNext.equals(ourColor))
            return Seq.of(this);
        else
            return genDescMoves().flatMap(tft -> genASAOM(ourColor, tft));
    }

    private Seq<GameState> genASAOM(Color ourColor, Desc only) {
        DescMove moveToApply = new DescMove(
                only.from, only.to, this, only.pawnPromotion);
        try {
            Optional<GameState> any = moveToApply.generateAfters()
                    .flatMap(DescMove.EitherStateOrIllMoveExcept::flatMapState)
                    .findAny();
            assert (any.isPresent());
            GameState aft = any.get();
            if (!aft.alivePlayers.get(ourColor) || aft.movesNext.equals(ourColor))
                return Seq.of(aft);
            else return aft.genDescMoves().map(this::genASAOMinternal);
        } catch (NeedsToBePromotedException e) {
            e.printStackTrace();
            throw new AssertionError(e);
        }
    }

    @NotNull
    private GameState genASAOMinternal(DescMove moveToBe) {
        //DescMove moveToBe = new DescMove(
        //        tft.from, tft.to, this, tft.pawnPromotion);
        try {
            Optional<GameState> newAny = moveToBe.generateAfters()
                    .flatMap(DescMove.EitherStateOrIllMoveExcept::flatMapState)
                    .findAny();
            assert newAny.isPresent();
            return newAny.get();
        } catch (NeedsToBePromotedException e) {
            e.printStackTrace();
            throw new AssertionError(e);
        }
    }
}
