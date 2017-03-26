package pl.edu.platinum.archiet.jchess3man.engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Michał Krzysztof Feiler on 26.03.17.
 */
class FunHashMapBoardImplTest {
    @Test
    void get() {
        System.out.println(new FunHashMapBoardImpl(NewGameBoardImpl.c).string());
    }

}