package pl.edu.platinum.archiet.jchess3man.engine;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Created by Michał Krzysztof Feiler on 21.03.17.
 */
class FromToPromMoveTest {

    @Test
    void newGameRookCap() {
        FromToPromMove fromToPromMove =
                new FromToPromMove(new Pos(0, 7), new Pos(0, 8), GameState.newGame);
        final Stream<FromToPromMove.EitherStateOrIllMoveExcept>
                eitherStateOrIllMoveExceptStream;
        try {
            eitherStateOrIllMoveExceptStream = fromToPromMove.generateAfters();
        } catch (NeedsToBePromotedException e) {
            e.printStackTrace();
            throw new AssertionError(e);
        }
        assertFalse(eitherStateOrIllMoveExceptStream
                .peek(some -> System.out.println(some.toString()))
                .anyMatch(FromToPromMove.EitherStateOrIllMoveExcept::isState));
    }

}