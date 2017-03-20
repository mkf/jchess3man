import org.jetbrains.annotations.Nullable;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;
import pl.edu.platinum.archiet.jchess3man.engine.*;

/**
 * Created by Michał Krzysztof Feiler on 19.03.17.
 */
public abstract class SitValuesUDAI implements SingleMoveUltimateDecisionAI {
    public final double ownedToThreatened;

    public SitValuesUDAI(
            @Nullable Double ownedToThreatened
    ) {
        this.ownedToThreatened =
                ownedToThreatened == null ? 4 : ownedToThreatened;
        assert (this.ownedToThreatened > 0);
    }

    abstract public FromToPromMove decide(GameState s);

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
