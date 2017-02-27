package pl.edu.platinum.archiet.jchess3man.engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Michał Krzysztof Feiler on 27.02.17.
 */
class LongDiagonalVectorTest {
    @Test
    void longerDiagonalVectorTo() {
        System.out.println((new Pos(5, 0)).continuousVectorsTo(new Pos(1, 6)).toString());
    }

}