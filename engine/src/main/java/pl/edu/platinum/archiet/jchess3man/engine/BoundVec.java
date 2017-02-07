package pl.edu.platinum.archiet.jchess3man.engine;

/**
 * Created by Michał Krzysztof Feiler on 03.02.17.
 */
public class BoundVec<T extends Vector> {
    public final T vec;
    public final Pos from;
    public final Pos to;

    public BoundVec(T vec, Pos from)
            throws VectorAdditionFailedException {
        this.vec = vec;
        this.from = from;
        this.to = from.addVec(vec);
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
    }

    public String toString() {
        return "BVF" + from.toString() + "→\\" + vec.toString() + "\\";
    }
}
