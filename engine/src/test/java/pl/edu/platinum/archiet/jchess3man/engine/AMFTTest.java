package pl.edu.platinum.archiet.jchess3man.engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Michał Krzysztof Feiler on 19.02.17.
 */
class AMFTTest {
    @Test
    void showAMFT() {
        AMFT.showAMFT(new Pos(0, 0), 12);
        AMFT.showAMFT(new Pos(0, 11), -1);
        AMFT.showAMFT(new Pos(0, 12), 0);
        AMFT.showAMFT(new Pos(0, 7), 7 + 12);
        AMFT.showAMFT(new Pos(5, 0), 12);
        AMFT.showAMFT(new Pos(5, 12), 0);
        AMFT.showAMFT(new Pos(5, 7), 7 + 12);
        AMFT.showAMFT(new Pos(4, 0), 12);
        AMFT.showAMFT(new Pos(4, 12), 0);
        AMFT.showAMFT(new Pos(4, 7), 7 + 12);
        AMFT.showAMFT(new Pos(3, 0), 12);
        AMFT.showAMFT(new Pos(3, 12), 0);
        AMFT.showAMFT(new Pos(3, 7), 7 + 12);
    }

}