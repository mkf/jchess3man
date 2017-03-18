import pl.edu.platinum.archiet.jchess3man.engine.FromToPromMove;
import pl.edu.platinum.archiet.jchess3man.engine.GameState;

import javax.lang.model.type.NullType;
import java.util.Optional;
import java.util.function.Predicate;

import java.util.concurrent.Future;
import java.util.stream.Stream;

/**
 * Created by Michał Krzysztof Feiler on 18.03.17.
 */
public interface SingleMoveUltimateDecisionAI {
    FromToPromMove decide(GameState s);

    abstract class AfterWhat<T extends SingleMoveStreamingAI> {
        public final T of;
        public final Predicate<T> p;

        public AfterWhat(T of, Predicate<T> p) {
            this.of = of;
            this.p = p;
        }

        FromToPromMove decide(GameState s) {
            SingleMoveStreamingAI.ReadAtomicThinking our =
                    of.thinking(s);
            do {
                our.waitForNew();
                if (p.test(of)) break;
            } while (true);
            our.stop();
            return our.get();
        }
    }
}
