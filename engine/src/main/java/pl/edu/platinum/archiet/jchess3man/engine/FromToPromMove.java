package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by Michał Krzysztof Feiler on 18.02.17.
 */
public class FromToPromMove extends FromToProm {
    public final GameState before;

    public FromToPromMove(Pos from, Pos to, GameState before) {
        this(from, to, before, null);
    }

    public FromToPromMove(Pos from, Pos to, GameState before, @Nullable FigType pawnPromotion) {
        super(from, to, pawnPromotion);
        this.before = before;
    }

    public void generateVecs() throws NullPointerException, NeedsToBePromotedException {
        Fig fsq = before.board.get(from);
        if (fsq != null) generateVecs(fsq);
        else throw new NullPointerException(from.toString() + "\n" + before.board.string());
    }

    public Stream<EitherStateOrIllMoveExcept> generateAfters() throws NeedsToBePromotedException {
        return generateAfters(true);
    }

    public Stream<EitherStateOrIllMoveExcept> generateAftersWOEvaluatingDeathNorCheckingCheckJustCheckInitiation(
    ) throws NeedsToBePromotedException {
        return generateAfters(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static class EitherStateOrIllMoveExcept {
        public final Optional<GameState> state;
        public final Optional<IllegalMoveException> exception;

        private EitherStateOrIllMoveExcept(
                Optional<GameState> state,
                Optional<IllegalMoveException> exception) {
            this.state = state;
            this.exception = exception;
        }

        public EitherStateOrIllMoveExcept(GameState state) {
            this(Optional.of(state), Optional.empty());
        }

        public EitherStateOrIllMoveExcept(IllegalMoveException exception) {
            this(Optional.empty(), Optional.of(exception));
        }

        EitherStateOrIllMoveExcept() {
            this(Optional.empty(), Optional.empty());
        }

        public boolean isState() {
            return state.isPresent();
        }

        public boolean isException() {
            return exception.isPresent();
        }

        public Stream<GameState> flatMapState() {
            return state.isPresent() ? Stream.of(state.get()) : Stream.empty();
        }

        public Stream<IllegalMoveException> flatMapException() {
            return exception.isPresent() ? Stream.of(exception.get()) : Stream.empty();
        }
    }

    public Stream<EitherStateOrIllMoveExcept> generateAfters(boolean withEvalDeath) throws NeedsToBePromotedException {
        if (!areVecsGenerated()) generateVecs();
        assert (vecs != null);
        return StreamSupport
                .stream(vecs.spliterator(), false)
                .map(vec -> {
                    try {
                        return new Move(from, vec, before);
                    } catch (VectorAdditionFailedException | NeedsToBePromotedException e) {
                        throw new AssertionError(e);
                    }
                })
                .map(withEvalDeath
                        ? (move -> {
                    try {
                        GameState after = move.after();
                        return new EitherStateOrIllMoveExcept(after);
                    } catch (CheckInitiatedThruMoatException | ImpossibleMoveException e) {
                        return new EitherStateOrIllMoveExcept(e);
                    } catch (NeedsToBePromotedException e) {
                        throw new AssertionError(e);
                    }
                })
                        : (move -> {
                    try {
                        return new EitherStateOrIllMoveExcept(move
                                .afterWOEvaluatingDeathNorCheckingCheckJustCheckInitiation());
                    } catch (NeedsToBePromotedException e) {
                        throw new AssertionError(e);
                    } catch (ImpossibleMoveException | CheckInitiatedThruMoatException e) {
                        return new EitherStateOrIllMoveExcept(e);
                    }
                }));
    }
}
