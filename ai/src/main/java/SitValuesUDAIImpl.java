import org.jetbrains.annotations.Nullable;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;
import pl.edu.platinum.archiet.jchess3man.engine.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * Created by Michał Krzysztof Feiler on 18.03.17.
 */
public class SitValuesUDAIImpl implements SingleMoveUltimateDecisionAI {
    public final double precision;
    public final double ownedToThreatened;
    public final @Nullable FigType defPawnProm;
    private double curFixPrec;

    public SitValuesUDAIImpl(
            @Nullable Double precision,
            @Nullable Double ownedToThreatened,
            @Nullable FigType defPawnProm
    ) {
        this.precision = precision == null ? 0.0002 : precision;
        this.ownedToThreatened =
                ownedToThreatened == null ? 4 : ownedToThreatened;
        this.defPawnProm = defPawnProm;
        assert (this.precision > 0);
        assert (this.ownedToThreatened > 0);
    }

    private void worker(double newChance, AtomicReference<Double> makeFloat, GameState aft, Color movesNext) {
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
                new Thread(() -> {
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
                                .filter(FromToPromMove.EitherStateOrIllMoveExcept
                                        ::isState)
                                .map(some -> some.state)
                                .findAny().flatMap(some ->
                                        some.isPresent() ? Optional.empty() : some);
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
                }).start();
            }
        while (wwg.get() > 0) {
            wwg.oWait();
        }
        double chance = newChance / (double) (possib.size());
        for (final GameState m : possib) {
            wwg.incrementAndGet();
            new Thread(() -> {
                worker(chance, makeFloat, m, movesNext);
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
                AMFT.getIterableFor(from).forEach((Pos to) -> new Thread(() -> {
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
                                fromToPromMove.generateAfters();
                        final Optional<GameState>
                                any = eitherStateOrIllMoveExceptStream
                                .filter(FromToPromMove.EitherStateOrIllMoveExcept
                                        ::isState)
                                .map(some -> some.state)
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .findAny();
                        if (any.isPresent()) {
                            GameState aft = any.get();
                            gwg.incrementAndGet();
                            new Thread(() -> {
                                countEm.incrementAndGet();
                                countEm.notif();
                                double newChance;
                                while (wg.get()) {
                                    wg.oWait();
                                }
                                newChance = 1.0 / countEm.get();
                                AtomicReference<Double> makeFloat = new AtomicReference<>(0.0);
                                thoughts.put(new FromTo(from, to), makeFloat);
                                worker(newChance, makeFloat, aft, s.movesNext);
                                gwg.decrementAndGet();
                                gwg.notif();
                            }).start();
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
                }).start()));
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

    public static final double DEATH = -100000;
    public static final double OPDIES = 15000;

    public static int value(FigType ft) {
        switch (ft) {
            case Pawn:
                return 1;
            case Knight:
                return 3;
            case Bishop:
                return 5;
            case Rook:
                return 6;
            case Queen:
                return 10;
            case King:
                return 3;
            case ZeroFigType:
                throw new IllegalArgumentException(ft.toString());
            default:
                throw new IllegalArgumentException(ft.toString());
        }
    }

    public static int value(Fig fig) {
        if (fig == null) return 0;
        return value(fig.type);
    }

    public double sitValue(GameState s, Color who) {
        Board b = s.board;
        Tuple2<Seq<Pos>, Seq<Pos>> tuple2 =
                b.friendsAndOthers(who, s.alivePlayers);
        Tuple2<Seq<Pos>, Seq<Pos>> friendsT = tuple2.v1.parallel().duplicate();
        Tuple2<Seq<Pos>, Seq<Pos>> othersT = tuple2.v2.parallel().duplicate();
        Seq<Pos> friends = friendsT.v1;
        Seq<Pos> others = othersT.v1;
        Tuple2<Seq<FigType>, Seq<FigType>> threateningAndThreatened = b.threateningAndThreatened(who, s.alivePlayers, s.enPassantStore,
                friendsT.v2, othersT.v2);
        Seq<FigType> ing = threateningAndThreatened.v1.parallel();
        Seq<FigType> ed = threateningAndThreatened.v2.parallel();
        int own = friends.mapToInt(o -> value(b.get(o))).sum();
        int their = others.mapToInt(o -> value(b.get(o))).sum();
        int myIch = ing.mapToInt(SitValuesUDAIImpl::value).sum();
        int oniNas = ed.mapToInt(SitValuesUDAIImpl::value).sum();
        double living = (double) (own - their) * ownedToThreatened + (double) (myIch - oniNas);
        if (!s.alivePlayers.get(who)) return DEATH;
        for (final Color p : Color.colors)
            if (!p.equals(who) && !s.alivePlayers.get(p)) living += OPDIES;
        return living;
    }
}
