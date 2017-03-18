import pl.edu.platinum.archiet.jchess3man.engine.FromToPromMove;
import pl.edu.platinum.archiet.jchess3man.engine.GameState;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

/**
 * Created by Michał Krzysztof Feiler on 18.03.17.
 */
public interface SingleMoveStreamingAI {
    ReadAtomicThinking thinking(GameState s);

    class AtomicThinking {
        private final AtomicReference<FromToPromMove> i;
        public final AtomicBoolean isNew;
        public final AtomicBoolean noMore;

        public AtomicThinking() {
            i = new AtomicReference<>(null);
            isNew = new AtomicBoolean(false);
            noMore = new AtomicBoolean(false);
        }

        public FromToPromMove get() {
            FromToPromMove ret = i.get();
            isNew.set(false);
            return ret;
        }

        public void set(FromToPromMove move) {
            if (!noMore.get()) {
                i.set(move);
                isNew.set(true);
                notifyAll();
            }
        }

        public boolean goOn() {
            return !stopped();
        }

        public boolean stopped() {
            return noMore.get();
        }

        public void stop() {
            noMore.set(true);
            noMore.notifyAll();
        }
    }

    class ReadAtomicThinking {
        private final AtomicThinking i;
        public final boolean stoppable;

        public ReadAtomicThinking(AtomicThinking what, boolean stoppable) {
            i = what;
            this.stoppable = stoppable;
        }

        public FromToPromMove get() {
            return i.get();
        }

        public boolean isNew() {
            return i.isNew.get();
        }

        public void waitForNew() {
            while (!isNew()) {
                try {
                    i.wait();
                } catch (InterruptedException ignored) {
                }
            }
        }

        public boolean stopped() {
            return i.stopped();
        }

        public void stop() {
            i.stop();
        }

        public ReadAtomicThinking unstoppable() {
            return new ReadAtomicThinking(i, false);
        }
    }
}
