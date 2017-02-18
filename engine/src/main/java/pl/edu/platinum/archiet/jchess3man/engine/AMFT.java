package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.Contract;

import java.util.*;

/**
 * Created by Michał Krzysztof Feiler on 08.02.17.
 */
public class AMFT {
    private final static Map<Pos, Set<Pos>> m = amftInit();

    private static Map<Pos, Set<Pos>> amftInit() {
        HashMap<Pos, Set<Pos>> ourmap = new HashMap<>();
        for (final Pos from : new AllPosIterable()) {
            HashSet<Pos> our = new HashSet<>();
            for (final Pos to : new AllPosIterable()) {
                if (!from.continuousVectorsTo(to).isEmpty() ||
                        from.optionalKnightVectorTo(to).isPresent())
                    our.add(to);
            }
            ourmap.put(from, our);
        }
        return ourmap;
    }

    @Contract("_ -> !null")
    public static Iterable<Pos> getIterableFor(Pos pos) {
        return new Iterable<>() {
            private final Pos p = pos;

            @Override
            public Iterator<Pos> iterator() {
                return m.get(p).iterator();
            }
        };
    }
}
