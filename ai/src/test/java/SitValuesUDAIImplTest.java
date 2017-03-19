import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.platinum.archiet.jchess3man.engine.FigType;
import pl.edu.platinum.archiet.jchess3man.engine.GameState;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Michał Krzysztof Feiler on 18.03.17.
 */
class SitValuesUDAIImplTest {
    SitValuesUDAIImpl tiny = new SitValuesUDAIImpl(
            0.5, null, FigType.Queen);
    SitValuesUDAIImpl def = new SitValuesUDAIImpl(
            null, null, FigType.Queen);

    @BeforeEach
    void setUp() {

    }

    @Test
    void first() {
        System.out.println(tiny.decide(GameState.newGame));
    }

    @Test
    void second() {
        System.out.println(def.decide(GameState.newGame));
    }

}