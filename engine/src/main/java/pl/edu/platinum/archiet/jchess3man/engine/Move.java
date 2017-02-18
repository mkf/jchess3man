package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Created by Michał Krzysztof Feiler on 03.02.17.
 */
public class Move<T extends Vector> {
    public final BoundVec<T> boundVec;
    public final GameState before;

    @Contract(pure = true)
    public Move(BoundVec<T> boundVec, GameState before) {
        this.boundVec = boundVec;
        this.before = before;
    }

    @Contract(pure = true)
    public Move(T vec, Pos from, GameState before) throws VectorAdditionFailedException, NeedsToBePromotedException {
        this(new BoundVec<T>(vec, from), before);
    }

    @Contract(pure = true)
    public Fig fromSquare() {
        return before.board.get(boundVec.from);
    }

    @Contract(pure = true)
    public Fig what() {
        return fromSquare();
    }

    @Contract(pure = true)
    public Fig toSquare() {
        return before.board.get(boundVec.to);
    }

    @Contract(pure = true)
    public Color who() throws NullPointerException {
        return what().color;
    }

    @Contract(pure = true)
    private boolean isThereNothingToMoveHere() {
        return what() == null;
    }

    @Contract(pure = true)
    private boolean doesntThatColorMoveNow() {
        return who() != before.movesNext;
    }

    @Contract(pure = true)
    private boolean cannotWeEnPassant() {
        assert (boundVec.vec instanceof PawnCapVector);
        if (toSquare() != null) return true;
        if (before.enPassantStore.matchLast(boundVec.to))
            return !before.board.get(boundVec.to)
                    .color.equals(who().previous());
        if (before.enPassantStore.matchPrev(boundVec.to))
            return !before.board.get(boundVec.to)
                    .color.equals(who().next());
        return true;
    }

    @Contract(pure = true)
    private boolean areWeAttemptingToCapOurOwnPiece() {
        return toSquare() != null && toSquare().color == who();
    }


    @Contract(pure = true)
    private boolean areNotAllEmpties() {
        try {
            return before.board.checkEmpties(boundVec.empties());
        } catch (VectorAdditionFailedException e) {
            e.printStackTrace();
            throw new AssertionError(e);
        }
    }

    @Contract(pure = true)
    private boolean checkCastlingImpossibility() {
        assert (boundVec.vec instanceof CastlingVector);
        return !((CastlingVector) boundVec.vec).
                checkPossibility(
                        before.castlingPossibilities.get(who())
                );
    }

    @Contract(pure = true)
    private boolean areWeCapturingThruMoats() {
        return toSquare() != null && boundVec.moats().iterator().hasNext();
    }

    @Nullable
    @Contract(pure = true)
    private Color areWePassingAnUnbridgedMoat() {
        for (final Color current : boundVec.moats())
            if (!before.moatsState.isBridgedBetweenThisAndNext(current))
                return current;
        return null;
    }

    @Contract(pure = true)
    @NotNull
    public Optional<Impossibility> checkPossibility() {
        return Optional.ofNullable(checkPossibility(true));
    }

    @Contract(pure = true, value = " -> !null")
    public Optional<Impossibility> checkPossibilityOppositeColor() {
        return Optional.ofNullable(checkPossibility(false));
    }

    @Nullable
    @Contract(pure = true)
    private Impossibility checkPossibility(boolean sameNotOppositeColor) {
        if (isThereNothingToMoveHere())
            return new Impossibility.NothingToMoveHere(boundVec.from);
        if (doesntThatColorMoveNow() == sameNotOppositeColor)
            return new Impossibility.ThatColorDoesNotMoveNow(who());
        if (boundVec.vec instanceof PawnCapVector && cannotWeEnPassant())
            return new Impossibility.CannotEnPassant(boundVec.to, before.enPassantStore);
        if (areWeAttemptingToCapOurOwnPiece())
            return new Impossibility.CannotCaptureSameColor(who(), boundVec.to, toSquare());
        if (areNotAllEmpties()) return new Impossibility.NotAllEmpties();
        if (boundVec.vec instanceof CastlingVector && checkCastlingImpossibility())
            return new Impossibility.ForbiddenCastling(
                    before.castlingPossibilities.get(who()),
                    (CastlingVector) boundVec.vec
            );
        if (areWeCapturingThruMoats())
            return new Impossibility.CapturingThruMoats(boundVec.moats(), boundVec.to, toSquare());
        Color unbridgedMoat = areWePassingAnUnbridgedMoat();
        if (unbridgedMoat != null)
            return new Impossibility.PassingNonBridgedMoat(
                    unbridgedMoat, before.moatsState, boundVec.moats());
        return null;
    }

    @Contract(pure = true)
    public void checkIfNotPromotedDespiteSuchANeed() throws NeedsToBePromotedException {
        if ((boundVec.vec instanceof PawnVector)
                && !(boundVec.vec instanceof PawnPromVector)
                && ((PawnVector) boundVec.vec).reqProm(boundVec.from.rank))
            throw new NeedsToBePromotedException(boundVec);
    }

    @Contract(pure = true)
    public void throwImpossibility() throws ImpossibleMoveException {
        Optional<Impossibility> impossibility = checkPossibility();
        if (impossibility.isPresent())
            throw new ImpossibleMoveException(impossibility.get());
    }

    /**
     * @param afterBoard the board after the move
     * @return MoatsState after the move
     */
    @Contract(pure = true)
    public MoatsState afterMoatsState(Board afterBoard) {
        MoatsState moatsState = before.moatsState;
        if ((!(boundVec.vec instanceof CastlingVector))
                && ((!(boundVec.vec instanceof PawnVector)) ||
                (boundVec.vec instanceof PawnPromVector))) {
            for (final Color current : Color.colors) {
                if (moatsState.areBridgedOnBothSidesOf(current)) break;
                if (!before.alivePlayers.get(current)) {
                    moatsState = moatsState.bridgeOnBothSidesOf(current);
                    break;
                }
                for (int i = current.segm() * 8; i < (current.segm() + 1) * 8; i++)
                    if (afterBoard.get(0, i).color == current) {
                        moatsState = moatsState.bridgeOnBothSidesOf(current);
                        break;
                    }
            }
        }
        return moatsState;
    }

    /**
     * @return the EnPassantStore after the move. Pure.
     */
    @Contract(pure = true)
    public EnPassantStore afterEnPassantStore() {
        if (boundVec.vec instanceof PawnLongJumpVector)
            return before.enPassantStore
                    .appeared(
                            ((PawnLongJumpVector) boundVec.vec)
                                    .enPassantField(boundVec.from));
        return before.enPassantStore.nothing();
    }

    @Contract(pure = true)
    public static CastlingPossibilities.ColorEntry afterColorCastling(
            CastlingPossibilities.ColorEntry colorCastling,
            FigType whatType,
            Color who,
            Pos from
    ) {
        switch (whatType) {
            case King:
                return CastlingPossibilities.ColorEntry.No;
            case Rook:
                if (from.rank == 0)
                    switch (from.file - (who.segm() * 8)) {
                        case 0:
                            return colorCastling.offQueenside();
                        case 7:
                            return colorCastling.offKingside();
                    }
        }
        return colorCastling;
    }

    @Contract(pure = true)
    public CastlingPossibilities afterCastling() {
        CastlingPossibilities castlingPossibilities =
                before.castlingPossibilities.change(
                        who(), afterColorCastling(
                                before.castlingPossibilities.get(who()),
                                what().type,
                                who(),
                                boundVec.from
                        )
                );
        if (boundVec.to.rank == 0) switch (boundVec.to.file % 8) {
            case 7:
                Color segmCol = boundVec.to.colorSegm();
                return castlingPossibilities.change(segmCol,
                        castlingPossibilities.get(segmCol).offKingside());
            case 0:
                segmCol = boundVec.to.colorSegm();
                return castlingPossibilities.change(segmCol,
                        castlingPossibilities.get(segmCol).offQueenside());
            case CastlingVector.kfm:
                return castlingPossibilities
                        .change(boundVec.to.colorSegm(),
                                CastlingPossibilities.ColorEntry.No);
        }
        return castlingPossibilities;
    }

    public boolean couldYouDoItEvenIfYouWouldGetChecked() {
        try {
            //noinspection ResultOfMethodCallIgnored
            afterWOEvaluatingDeathNorCheckingCheckJustCheckInitiation();
        } catch (NeedsToBePromotedException ignored) {
        } catch (IllegalMoveException ignored) {
            return false;
        }
        return true;
    }

    @Contract(pure = true)
    @NotNull
    public GameState afterWOEvaluatingDeathNorCheckingCheckJustCheckInitiation() throws
            NeedsToBePromotedException, ImpossibleMoveException, CheckInitiatedThruMoatException {
        throwImpossibility();
        checkIfNotPromotedDespiteSuchANeed();
        MutableBoard b;
        try {
            b = boundVec.vec.mutableAfterBoard(before.board, boundVec.from, before.enPassantStore);
        } catch (VectorAdditionFailedException e) {
            e.printStackTrace();
            throw new AssertionError(e);
        }
        EnPassantStore enPassantStore = afterEnPassantStore();
        if (boundVec.moats().iterator().hasNext()) {
            if (before.board.isThereAThreat(
                    before.board._whereIsKing(who().previous()),
                    boundVec.to,
                    before.alivePlayers,
                    enPassantStore
            ) || before.board.isThereAThreat(
                    before.board._whereIsKing(who().next()),
                    boundVec.to,
                    before.alivePlayers,
                    enPassantStore
            )
                    ) throw new CheckInitiatedThruMoatException();
        }
        return new GameState(
                b,
                afterMoatsState(b),
                before.alivePlayers.get(before.movesNext.next())
                        ? before.movesNext.next()
                        : before.movesNext.previous(),
                afterCastling(),
                enPassantStore,
                what().type == FigType.Pawn || toSquare() != null ? 0 :
                        before.halfMoveClock + 1,
                before.fullMoveNumber + 1,
                before.alivePlayers
        );
    }

    public static GameState evaluateDeathThrowingCheck(GameState next, Color whatColor)
            throws WeInCheckException {
        Optional<Pos> heyItsCheck = next.amIinCheck(whatColor).findFirst();
        if (heyItsCheck.isPresent())
            throw new WeInCheckException(
                    new Impossibility.WeInCheck(heyItsCheck.get()));
        PlayersAlive newAliveColors = next.evalDeath();
        if (newAliveColors.equals(next.alivePlayers)) return next;
        return new GameState(next, newAliveColors);
    }

    public GameState after() throws
            CheckInitiatedThruMoatException,
            ImpossibleMoveException,
            NeedsToBePromotedException {
        return evaluateDeathThrowingCheck(
                afterWOEvaluatingDeathNorCheckingCheckJustCheckInitiation(),
                who());
    }
}
