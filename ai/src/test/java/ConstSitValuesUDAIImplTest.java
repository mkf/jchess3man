import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.platinum.archiet.jchess3man.engine.GameState;

/**
 * Created by Michał Krzysztof Feiler on 18.03.17.
 */
class ConstSitValuesUDAIImplTest {
    ConstSitValuesUDAIImpl def = new ConstSitValuesUDAIImpl(
            null, null);

    @BeforeEach
    void setUp() {

    }

    @Test
    void first() {
        System.out.println(def.decide(GameState.newGame));
    }

}