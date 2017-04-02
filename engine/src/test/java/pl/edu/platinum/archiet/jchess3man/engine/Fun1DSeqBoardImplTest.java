package pl.edu.platinum.archiet.jchess3man.engine;

import org.junit.jupiter.api.Test;

/**
 * Created by Michał Krzysztof Feiler on 26.03.17.
 */
class Fun1DSeqBoardImplTest {
    @Test
    void rnf() {
        System.out.println(
                new Fun1DSeqBoardImpl(
                        NewGameBoardImpl.c, true).string());
    }

    @Test
    void notRnf() {
        System.out.println(
                new Fun1DSeqBoardImpl(
                        NewGameBoardImpl.c, false).string());
    }

    @Test
    void hexBoard() {
        String hexNewGame =
                NewGameBoardImpl.c.toHexConcatRanks();
        System.out.println(hexNewGame);
        Fun1DSeqBoardImpl board = new Fun1DSeqBoardImpl(
                hexNewGame,
                true
        );
        System.out.println(board.b);
        System.out.println(board.string());
    }

}