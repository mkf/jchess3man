package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.Contract;

/**
 * Created by Michał Krzysztof Feiler on 03.02.17.
 */
public class BoundVec {
    public final Vector vec;
    public final Pos from;
    public final Pos to;

    public BoundVec(Vector vec, Pos from)
            throws VectorAdditionFailedException, NeedsToBePromotedException {
        this.vec = vec;
        this.from = from;
        to = from.addVec(vec);
        assert (to != null);
        if (to.equals(from))
            /*
            As stated in Clif's email from Mon, 2 Nov 2015 11:32:54 -0500
            Message-Id: <150c90b53b0-12f7-145cf@webprd-m97.mail.aol.com>
            In-Reply-To: <20151102070250.GA6328@tichy>
            which was In-Reply-To: <150c5fbe264-6425-13c7e@webprd-m75.mail.aol.com>
            which was In-Reply-To: <20151101235348.GA32145@tichy>
            which was In-Reply-To: <150c55f51d7-6425-13626@webprd-m75.mail.aol.com>
            which was In-Reply-To: <20151031110645.GA25362@tichy>
            moving to the same square is forbidden
            */
            throw new VectorAdditionFailedException.SameSquare(from, vec);
        if (vec instanceof PawnVector && !(vec instanceof PawnPromVector) && ((PawnVector) vec).reqProm(from.rank))
            throw new NeedsToBePromotedException(this);
    }

    public BoundVec(Pos from, Vector vec) throws
            VectorAdditionFailedException, NeedsToBePromotedException {
        this(vec, from);
    }

    @Contract(pure = true)
    public String toString() {
        return "BVF" + from.toString() + "→\\" + vec.toString() + "\\";
    }

    @Contract(pure = true)
    public Iterable<Color> moats() {
        return vec.moats(from);
    }

    @Contract(pure = true)
    public Iterable<Pos> empties() throws VectorAdditionFailedException {
        return vec.emptiesFrom(from);
    }

}
