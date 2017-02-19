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
        return new Iterable<Pos>() {
            private final Pos p = pos;

            @Override
            public Iterator<Pos> iterator() {
                return m.get(p).iterator();
            }
        };
    }

    public static void showAMFT(Pos p, int vfile) {
        boolean[][] ret = new boolean[6][24];
        for (Pos val : getIterableFor(p))
            ret[val.rank][val.file] = true;
        System.out.println(p.toString());
        for (int i = 5; i >= 0; i--) {
            for (int j = vfile; j < 24 + vfile; j++) {
                if (ret[i][(j + 24) % 24]) System.out.print("▓");
                else if ((j + 24) % 24 == p.file && i == p.rank) System.out.print("█");
                else System.out.print("░");
            }
            System.out.println();
        }
    }
}
