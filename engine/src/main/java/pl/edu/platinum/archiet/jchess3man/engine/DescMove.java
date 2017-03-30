package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.lambda.Seq;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by Michał Krzysztof Feiler on 18.02.17.
 * DescMove is an old-fashioned Move struct:
 * it contains fields [from], [to], [GameState] [before],
 * and optionally [pawnPromotion]
 */

public class DescMove extends Desc {
    /**
     * the state before move
     * the only field that extends DescMove from Desc
     */
    public final GameState before;

    /**
     * A constructor for the old-fashioned Move struct analogue
     * the move not being a pawn move having to result in promotion
     *
     * @param from   starting position
     * @param to     destination
     * @param before the state before
     */
    public DescMove(Pos from, Pos to, GameState before) {
        this(from, to, before, null);
    }

    /**
     * A constructor for the old-fashioned Move struct analogue
     * if [pawnPromotion] not null, promotion is assumed
     * although currently everything should work and if no promotion may occur
     * a pawnPromotion==FigType.queen would probably change nothing
     *
     * @param from          starting position
     * @param to            destination position
     * @param before        the state before
     * @param pawnPromotion the FigType to which there will be promotion or null
     */
    public DescMove(Pos from, Pos to, GameState before, @Nullable FigType pawnPromotion) {
        super(from, to, pawnPromotion);
        this.before = before;
    }

    protected DescMove(DescMove source, @NotNull FigType pawnPromotion) {
        super(source, pawnPromotion);
        this.before = source.before;
    }

    public DescMove(Desc s, GameState before) {
        this(s.from, s.to, before, s.pawnPromotion);
        vecs = s.vecs;
        vecsAreGenerated = s.vecsAreGenerated;
    }

    Seq<DescMove> promPossible() {
        if (this.pawnPromotion == null) return Seq.of(this);
        return seqPromPossible.map(prom -> new DescMove(this, prom));
    }

    /**
     * @return string representation:
     * `[0,7]→[0,8]Þþt_///pl.edu.platinum.archiet.jchess3man.engine.GameState@3d8c7aca`
     * from→toÞþ[pawnPromotion]///before.toString()
     */
    @Override
    public String toString() {
        return super.toString() + "///" + before.toString();
    }

    /**
     * Gets the Fig from before.board and calls generateVecs(fsq) on it
     *
     * @throws NullPointerException       if the from square is empty on before.board
     * @throws NeedsToBePromotedException if the promotion is required but pawnPromotion is null
     */
    public void generateVecs() throws NullPointerException, NeedsToBePromotedException {
        Fig fsq = before.board.get(from);
        if (fsq != null) generateVecs(fsq);
        else throw new NullPointerException(from.toString() + "\n" + before.board.string());
    }

    /**
     * generates After states with evaluating death and checking check
     *
     * @return stream of Either them (those States) or IllegalMoveExceptions
     * @throws NeedsToBePromotedException if not areVecsGenerated _and_ the promotion is required but pawnPromotion is null
     */
    public Stream<EitherStateOrIllMoveExcept> generateAfters() throws NeedsToBePromotedException {
        return generateAfters(true);
    }

    /**
     * generates After states but without evaluating death nor checking check
     * with check initiation checking though
     *
     * @return stream of Either them (those States) or IllegalMoveExceptions
     * @throws NeedsToBePromotedException if not areVecsGenerated _and_ the promotion is required but pawnPromotion is null
     */
    public Stream<EitherStateOrIllMoveExcept> generateAftersWOEvaluatingDeath(
    ) throws NeedsToBePromotedException {
        return generateAfters(false);
    }

    /**
     * An analogue to something like `Either≤[GameState],[IllegalMoveException]≥`
     */
    public static class EitherStateOrIllMoveExcept {
        /**
         * The state field in this Either
         */
        public final @Nullable GameState state;
        /**
         * The exception field in this Either
         */
        public final @Nullable IllegalMoveException exception;

        private EitherStateOrIllMoveExcept(
                @Nullable GameState state,
                @Nullable IllegalMoveException exception) {
            this.state = state;
            this.exception = exception;
        }

        /**
         * The "state" option of the Either
         *
         * @param state value
         */
        public EitherStateOrIllMoveExcept(@NotNull GameState state) {
            this(state, null);
        }

        /**
         * The "exception" option of the Either
         *
         * @param exception value
         */
        public EitherStateOrIllMoveExcept(@NotNull IllegalMoveException exception) {
            this(null, exception);
        }

        EitherStateOrIllMoveExcept() {
            this(null, null);
        }

        /**
         * @return whether state is present in the Either
         */
        public boolean isState() {
            return state != null;
        }

        /**
         * @return whether exception is present in the Either
         */
        public boolean isException() {
            return exception != null;
        }

        /**
         * @return Stream.of(state) or Stream.empty()
         */
        public Stream<GameState> flatMapState() {
            return isState() ? Stream.of(state) : Stream.empty();
        }

        /**
         * @return Stream.of(exception) or Stream.empty()
         */
        public Stream<IllegalMoveException> flatMapException() {
            return isException() ? Stream.of(exception) : Stream.empty();
        }

        @Override
        public String toString() {
            if (isState()) {
                if (isException()) return "EITH:BOTH:" + state.toString() + "AND" + exception.toString();
                return "EITH:STATE:" + state.toString();
            }
            if (isException()) return "EITH:EXCE:" + exception.toString();
            return "EITH:NONE";
        }
    }

    protected Stream<EitherStateOrIllMoveExcept> generateAfters(boolean withEvalDeath) throws NeedsToBePromotedException {
        if (!areVecsGenerated()) generateVecs();
        assert (vecs != null);
        return StreamSupport
                .stream(vecs.spliterator(), false)
                .map(vec -> {
                    try {
                        return new VecMove(from, vec, before);
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
                        GameState ret = move.afterWOEvaluatingDeath();
                        ret.throwCheck(move.who());
                        return new EitherStateOrIllMoveExcept(ret);
                    } catch (NeedsToBePromotedException e) {
                        throw new AssertionError(e);
                    } catch (ImpossibleMoveException | CheckInitiatedThruMoatException e) {
                        return new EitherStateOrIllMoveExcept(e);
                    }
                }));
    }
}
