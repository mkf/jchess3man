import org.jetbrains.annotations.Nullable;
import org.jooq.lambda.Seq;
import pl.edu.platinum.archiet.jchess3man.engine.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * Created by Michał Krzysztof Feiler on 18.03.17.
 */
public class SitValuesUDAIImpl extends SitValuesUDAI {
    public final double precision;
    public final @Nullable FigType defPawnProm;
    private double curFixPrec;

    public SitValuesUDAIImpl(
            @Nullable Double precision,
            @Nullable Double ownedToThreatened,
            @Nullable FigType defPawnProm
    ) {
        super(ownedToThreatened);
        this.precision = precision == null ? 0.02 /*0.002*/ /*0.0002*/ : precision;
        this.defPawnProm = defPawnProm;
        assert (this.precision > 0);
    }

    private void worker(double newChance, AtomicReference<Double> makeFloat, GameState after, Color movesNext, ExecutorService executor) {
        final GameState aft = after.evaluateDeath();
        UnaryOperator<Double> addition = d -> d + (sitValue(aft, movesNext));
        if (!aft.alivePlayers.get(movesNext) || //if dead
                newChance < curFixPrec) { //if too deep
            makeFloat.getAndUpdate(addition);
            return;
        }
        AtoInt wwg = new AtoInt(0);
        ArrayList<GameState> possib = new ArrayList<>(2050);
        for (final Pos wFrom : aft.board.friendsAndOthers(aft.movesNext, aft.alivePlayers).v1)
            for (final Pos wTo : AMFT.getIterableFor(wFrom)) {
                wwg.incrementAndGet();
                executor.submit(() -> {
                    FromToPromMove wFromToPromMove =
                            new FromToPromMove(wFrom, wTo, aft);
                    try {
                        wFromToPromMove.generateVecs();
                    } catch (NeedsToBePromotedException e) {
                        wFromToPromMove =
                                new FromToPromMove(wFrom, wTo, aft, defPawnProm);
                        try {
                            wFromToPromMove.generateVecs();
                        } catch (NeedsToBePromotedException e1) {
                            e1.printStackTrace();
                            throw new AssertionError(
                                    "promotion not recognized",
                                    e1);
                        }
                    }
                    try {
                        final Stream<FromToPromMove.EitherStateOrIllMoveExcept>
                                wEitherStateOrIllMoveExceptStream =
                                wFromToPromMove.generateAfters();
                        final Optional<GameState>
                                wAny = wEitherStateOrIllMoveExceptStream
                                .flatMap(FromToPromMove.EitherStateOrIllMoveExcept::flatMapState)
                                .findAny();
                        if (wAny.isPresent()) {
                            GameState wAft = wAny.get();
                            possib.add(wAft);
                        }
                    } catch (NeedsToBePromotedException e) {
                        e.printStackTrace();
                        throw new AssertionError(
                                "promotion not recognized",
                                e);
                    }
                    wwg.decrementAndGet();
                    wwg.notif();
                });
            }
        while (wwg.get() > 0) {
            wwg.oWait();
        }
        double chance = newChance / (double) (possib.size());
        for (final GameState m : possib) {
            wwg.incrementAndGet();
            new Thread(() -> {
                worker(chance, makeFloat, m, movesNext, executor);
                wwg.decrementAndGet();
                wwg.notif();
            }).start();
        }
        while (wwg.get() > 0) {
            wwg.oWait();
        }
    }

    private static class AtoInt extends AtomicInteger {
        AtoInt(int src) {
            super(src);
        }

        synchronized void oWait() {
            try {
                wait();
            } catch (InterruptedException ignored) {
            }
        }

        synchronized void notif() {
            notifyAll();
        }
    }

    private static class AtoBool extends AtomicBoolean {
        AtoBool(boolean src) {
            super(src);
        }

        synchronized void oWait() {
            try {
                wait();
            } catch (InterruptedException ignored) {
            }
        }

        synchronized void notif() {
            notifyAll();
        }
    }

    @Override
    public FromToPromMove decide(GameState s) {
        ExecutorService executor = Executors.newCachedThreadPool();
        curFixPrec = precision;
        ConcurrentHashMap<FromTo, AtomicReference<Double>> thoughts =
                new ConcurrentHashMap<>(30);
        AtoInt countEm = new AtoInt(0);
        AtoBool wg = new AtoBool(true);
        AtoInt gwg = new AtoInt(0);
        AtoBool nlas = new AtoBool(true);
        //for(final Pos from : new AllPosIterable()) {
        //    for(final Pos to : AMFT.getIterableFor(from)) {
        //    }
        //}
        (new AllPosIterable()).forEach((Pos from) ->
                AMFT.getIterableFor(from).forEach((Pos to) -> executor.submit(() -> {
                    FromToPromMove fromToPromMove =
                            new FromToPromMove(from, to, s);
                    try {
                        fromToPromMove.generateVecs();
                    } catch (NeedsToBePromotedException e) {
                        fromToPromMove =
                                new FromToPromMove(from, to, s, defPawnProm);
                        try {
                            fromToPromMove.generateVecs();
                        } catch (NeedsToBePromotedException e1) {
                            e1.printStackTrace();
                            throw new AssertionError(
                                    "promotion not recognized",
                                    e1);
                        }
                    } catch (NullPointerException ignored) {
                    }
                    try {
                        final Stream<FromToPromMove.EitherStateOrIllMoveExcept>
                                eitherStateOrIllMoveExceptStream =
                                fromToPromMove.generateAftersWOEvaluatingDeath();
                        final Optional<GameState>
                                any = eitherStateOrIllMoveExceptStream
                                .flatMap(FromToPromMove.EitherStateOrIllMoveExcept::flatMapState)
                                .findAny();
                        if (any.isPresent()) {
                            GameState aft = any.get();
                            gwg.incrementAndGet();
                            executor.submit(() -> {
                                countEm.incrementAndGet();
                                countEm.notif();
                                double newChance;
                                while (wg.get()) {
                                    wg.oWait();
                                }
                                newChance = 1.0 / countEm.get();
                                AtomicReference<Double> makeFloat = new AtomicReference<>(0.0);
                                thoughts.put(new FromTo(from, to), makeFloat);
                                worker(newChance, makeFloat, aft, s.movesNext, executor);
                                gwg.decrementAndGet();
                                gwg.notif();
                            });
                        } else gwg.notif();
                    } catch (NeedsToBePromotedException e) {
                        e.printStackTrace();
                        throw new AssertionError(
                                "promotion not recognized",
                                e);
                    } catch (NullPointerException ignored) {
                    }
                    if (from.equals(new Pos(1, 5))) {
                        nlas.set(false);
                        nlas.notif();
                    }
                })));
        wg.set(false);
        wg.notif();
        /*
        try {
                gwg.oWait();
        } catch (InterruptedException ignored) {
        }
        */
        while (nlas.get()) {
            nlas.oWait();
        }
        while (countEm.get() < 1) {
            //countEm.oWait();
            if (countEm.get() > 0) break;
        }
        while (gwg.get() > 0) {
            gwg.oWait();
        }
        executor.shutdownNow();
        /*
        LinkedList<FromTo> ourfts = new LinkedList<>();
        for (final Map.Entry<FromTo, AtomicReference<Double>> entry
                : thoughts.entrySet()) {
            if (entry.getValue().get() >= max-precision)
                ourfts.add(entry.getKey());
        }
        */
        Optional<FromTo> ftso =
                Seq.seq(thoughts.entrySet()).sorted((a, b) -> {
                    double c = a.getValue().get();
                    double d = b.getValue().get();
                    if (c < d) return 1;
                    else if (c > d) return -1;
                    return 0;
                }).findFirst().map(Map.Entry::getKey);
        //assert (!ourfts.isEmpty());
        assert (ftso.isPresent());
        //FromTo ft = ourfts.getLast();
        FromTo ft = ftso.get();
        FromToPromMove m = new FromToPromMove(ft.from, ft.to, s);
        try {
            m.generateVecs();
            return m;
        } catch (NeedsToBePromotedException e) {
            m = new FromToPromMove(ft.from, ft.to, s, defPawnProm);
            return m;
        }
    }

}
