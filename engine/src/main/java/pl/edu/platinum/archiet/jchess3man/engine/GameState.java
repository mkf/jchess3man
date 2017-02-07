package pl.edu.platinum.archiet.jchess3man.engine;

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
                        ? source.board.copy()
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
}
