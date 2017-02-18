package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.Contract;

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
