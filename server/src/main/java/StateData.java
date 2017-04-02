import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import pl.edu.platinum.archiet.jchess3man.engine.*;

/**
 * Created by Michał Krzysztof Feiler on 01.04.17.
 */
public class StateData {
    private Long id;
    private Board board;
    private MoatsState moatsState;
    private Color movesNext;
    private CastlingPossibilities castlingPossibilities;
    private EnPassantStore enPassantStore;
    private Integer halfMoveClock;
    private Integer fullMoveNumber;
    private PlayersAlive playersAlive;

    public void setState(GameState s) {
        board = s.board;
        moatsState = s.moatsState;
        movesNext = s.movesNext;
        castlingPossibilities = s.castlingPossibilities;
        enPassantStore = s.enPassantStore;
        halfMoveClock = s.halfMoveClock;
        fullMoveNumber = s.fullMoveNumber;
        playersAlive = s.alivePlayers;
    }

    public GameState getState() {
        return new GameState(
                board,
                moatsState,
                movesNext,
                castlingPossibilities,
                enPassantStore,
                halfMoveClock,
                fullMoveNumber,
                playersAlive
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Board getBoard() {
        return board;
    }

    public String getHexBoard() {
        return board.toHexConcatRanks();
    }

    public void setHexBoard(String hexBoard) {
        board = new Fun1DSeqBoardImpl(hexBoard, true);
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public MoatsState getMoatsState() {
        return moatsState;
    }

    public void setMoatsState(MoatsState moatsState) {
        this.moatsState = moatsState;
    }

    public int getMoats() {
        return moatsState.toInt();
    }

    public void setMoats(int moats) {
        moatsState = new MoatsState(moats);
    }

    public int getMovesNext() {
        return movesNext.toInt();
    }

    public void setMovesNext(Color movesNext) {
        this.movesNext = movesNext;
    }

    public void setMovesNext(int movesNext) {
        this.movesNext = Color.byIndex(movesNext);
    }

    @NotNull
    @Contract(pure = true)
    public static boolean[] booleanArrayOfCastlingPossibilities(@NotNull CastlingPossibilities c) {
        return new boolean[]{
                c.w.k, c.w.q, c.g.k, c.g.q, c.b.k, c.b.q
        };
    }

    public CastlingPossibilities getCastlingPossibilities() {
        return castlingPossibilities;
    }

    public int getCastling() {
        return castlingPossibilities.toInt();
    }

    public void setCastlingPossibilities(CastlingPossibilities castlingPossibilities) {
        this.castlingPossibilities = castlingPossibilities;
    }

    public void setCastling(int castling) {
        castlingPossibilities = new CastlingPossibilities(castling);
    }

    public EnPassantStore getEnPassantStore() {
        return enPassantStore;
    }

    public String getEnPassant() {
        return enPassantStore.legacyHex();
    }

    public void setEnPassantStore(EnPassantStore enPassantStore) {
        this.enPassantStore = enPassantStore;
    }

    public void setEnPassant(String legacyHex) {
        this.enPassantStore = new EnPassantStore(legacyHex);
    }

    public Integer getHalfMoveClock() {
        return halfMoveClock;
    }

    public void setHalfMoveClock(Integer halfMoveClock) {
        this.halfMoveClock = halfMoveClock;
    }

    public Integer getFullMoveNumber() {
        return fullMoveNumber;
    }

    public void setFullMoveNumber(Integer fullMoveNumber) {
        this.fullMoveNumber = fullMoveNumber;
    }

    public PlayersAlive getPlayersAlive() {
        return playersAlive;
    }

    public int getAliveColors() {
        return playersAlive.toInt();
    }

    public void setPlayersAlive(PlayersAlive playersAlive) {
        this.playersAlive = playersAlive;
    }

    public void setAliveColors(int aliveColors) {
        this.playersAlive = new PlayersAlive(aliveColors);
    }
}
