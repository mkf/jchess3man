package pl.edu.platinum.archiet.jchess3man.engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Michał Krzysztof Feiler on 26.03.17.
 */
class Fun1DSeqBoardImplTest {
    @Test
    void rnf() {
        System.out.println(new Fun1DSeqBoardImpl(NewGameBoardImpl.c, true).string());
    }

    @Test
    void nrnf() {
        System.out.println(new Fun1DSeqBoardImpl(NewGameBoardImpl.c, false).string());
    }

}