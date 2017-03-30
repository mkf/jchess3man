package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;

/**
 * Created by Michał Krzysztof Feiler on 28.01.17.
 * RankVector describes moves inward or outward by [abs]
 */
public class RankVector extends AxisVector {
    /**
     * Simple constructor for RankVector
     *
     * @param abs    count of ranks inwards/outwards
     * @param inward whether inward or outward
     */
    public RankVector(int abs, boolean inward) {
        super(abs, inward);
    }

    /**
     * Constructor for RankVector allowing negative param
     *
     * @param t if positive, inward, else outward. It's absolute
     *          value is the count of ranks inwards/outwards.
     */
    public RankVector(int t) {
        super(t);
    }

    /**
     * @return always 0, as it is a RankVector
     */
    public int file() {
        return 0;
    }

    /**
     * @return t(), i.e. negative if outwards, else positive,
     * count of ranks inwards/outwards
     */
    public int rank() {
        return t();
    }

    /**
     * Whether starting from fromRank we would cross the Center
     *
     * @param fromRank starting rank
     * @return whether the vector is oriented towards the Center and
     * whether not fromRank+abs≤5
     */
    public boolean thruCenter(int fromRank) {
        return direc && (fromRank + abs > 5);
    }

    /**
     * It's just an alias to direc
     *
     * @return direc field
     */
    public boolean inward() {
        return direc;
    }

    /**
     * @return Heading unit. As RankVector is a ContinuousVector,
     * if abs==1 returns this, if not abs ≤ 1 returns new RankVector(1, inward).
     * If abs==0, throws an AssertionError(this)
     */
    @NotNull
    public RankVector head() {
        if (abs > 0) {
            if (abs > 1) {
                return new RankVector(1, direc);
            }
            return this;
        }
        throw new AssertionError(this);
    }

    /**
     * @param fromRank starting rank
     * @return Tail continuous vector. A vector one shorter or null.
     * If head() crossed the center, tail() is oriented outwards.
     */
    @Nullable
    public RankVector tail(int fromRank) {
        if (abs > 0) {
            if (abs > 1) {
                return new RankVector(abs - 1, direc && fromRank != 5);
            }
            return null;
        }
        throw new AssertionError(this);
    }

    /**
     * @param ignored that one's simply ignored
     * @return always Collections.emptyList, as RankVector never crosses moats
     */
    @Override
    public Iterable<@NotNull Color> moats(Pos ignored) {
        return Collections.emptyList();
    }

    /**
     * Returns destination position of move from [from] with [this]
     *
     * @param from starting position
     * @return if we cross the center, Pos of rank 11 - (from.rank + abs) and of opposite file,
     * else Pos of the same file and rank equal from.rank + inward?abs:-abs
     */
    @Override
    public Pos addTo(Pos from) {
        if (thruCenter(from.rank))
            return new Pos(11 - (from.rank + abs), (from.file + 12) % 24);
        else return new Pos(from.rank + rank(), from.file);
    }
}
