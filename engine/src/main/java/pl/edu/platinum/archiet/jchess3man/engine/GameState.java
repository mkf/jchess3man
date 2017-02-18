package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by Michał Krzysztof Feiler on 03.02.17.
 */
public class GameState {
    public final Board board;
    public final MoatsState moatsState;
    public final Color movesNext;
    public final CastlingPossibilities castlingPossibilities;
    public final EnPassantStore enPassantStore;
    public final int halfMoveClock;
    public final int fullMoveNumber;
    public final PlayersAlive alivePlayers;

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

    public static final GameState newGame = new GameState(
            NewGameBoardImpl.c,
            MoatsState.noBridges,
            Color.White,
            CastlingPossibilities.all,
            EnPassantStore.empty,
            0, 0,
            PlayersAlive.all
    );
    public GameState(GameState source,
                     Optional<PlayersAlive> withPlayersAlive
    ) {
        this(source, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), withPlayersAlive);
    }

    public GameState(GameState source, PlayersAlive withPlayersAlive) {
        this(source, Optional.of(withPlayersAlive));
    }

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
                withBoard.isPresent() ? withBoard.get() : (
                        (source.board instanceof MutableBoard) ? source.board.mutableCopy() : source.board),
                withMoatsState.isPresent() ? withMoatsState.get() : source.moatsState,
                withMovesNext.isPresent() ? withMovesNext.get() : source.movesNext,
                withCastlingPossibilities.isPresent() ? withCastlingPossibilities.get() : source.castlingPossibilities,
                withEnPassantStore.isPresent() ? withEnPassantStore.get() : source.enPassantStore,
                withHalfMoveClock.isPresent() ? withHalfMoveClock.get() : source.halfMoveClock,
                withFullMoveNumber.isPresent() ? withFullMoveNumber.get() : source.fullMoveNumber,
                withPlayersAlive.isPresent() ? withPlayersAlive.get() : source.alivePlayers
        );
    }

    public Stream<Pos> amIinCheck(Color who) {
        return board.checkChecking(who, alivePlayers);
    }

    public Stream<Pos> amIinCheck() {
        return amIinCheck(movesNext);
    }

    @Contract(pure = true)
    public boolean _canIMoveWOCheck(Color who) {
        for (final Pos from : new AllPosIterable())
            if (!board.isEmpty(from) && board.get(from).color == who)
                for (final Pos to : AMFT.getIterableFor(from))
                    for (final Vector vec : board.get(from).vecs(from, to))
                        try {
                            final Move m = new Move<>(vec, from, this);
                            try {
                                //noinspection ResultOfMethodCallIgnored
                                m.afterWOEvaluatingDeathNorCheckingCheckJustCheckInitiation();
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
}
