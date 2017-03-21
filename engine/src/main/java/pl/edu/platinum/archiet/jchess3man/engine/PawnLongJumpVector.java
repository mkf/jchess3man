package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import pl.edu.platinum.archiet.jchess3man.engine.helpers.SingleElementIterable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Michał Krzysztof Feiler on 29.01.17.
 * PawnLongJumpVector describes a two-forward pawn move vector
 */
public class PawnLongJumpVector implements JumpVector, PawnVector {
    /**
     * There are no fields, constructor body is empty
     */
    public PawnLongJumpVector() {
    }

    /**
     * @return 2, always, as it moves two-forward
     */
    public int rank() {
        return 2;
    }

    /**
     * @return 0, always, as there is no file move
     */
    public int file() {
        return 0;
    }

    /**
     * @return false, always, as the pawn couldn't have crossed the center
     */
    public boolean reqpc() {
        return false;
    }

    /**
     * @param ignored is basically ignored
     * @return false, always, as there is no promotion possibility
     */
    public boolean reqProm(int ignored) {
        return false;
    }

    public Pos addTo(Pos from) throws VectorAdditionFailedException {
        if (from.rank != 1) throw new VectorAdditionFailedException(from, this);
        return new Pos(3, from.file);
    }

    /**
     * @param from starting position
     * @param to   destination position
     * @return whether from is (1,x) and to is (3,x) with common x
     */
    @Contract(pure = true)
    public static boolean willDo(Pos from, Pos to) {
        return from.rank == 1 && to.rank == 3 && from.file == to.file;
    }

    /**
     * @param from starting position, it is asserted that rank==1
     * @return new Pos(2, from.file), the destination field for en passant
     */
    public Pos enPassantField(Pos from) {
        assert (from.rank == 1);
        return new Pos(2, from.file);
    }

    /**
     * @param ignored is basically ignored
     * @return new SingleElementIterable(this)
     */
    public Iterable<PawnLongJumpVector> units(int ignored) {
        return units();
    }

    /**
     * @return new SingleElementIterable(this)
     */
    public Iterable<PawnLongJumpVector> units() {
        return new SingleElementIterable<PawnLongJumpVector>(this);
    }

    /**
     * @param ignored is basically ignored
     * @return Collections.emptyList(), as there are no moats in the way
     */
    public Iterable<@NotNull Color> moats(Pos ignored) {
        return moats();
    }

    /**
     * @return Collections.emptyList(), as there are no moats in the way
     */
    public Iterable<@NotNull Color> moats() {
        return Collections.emptyList();
    }

    @Override
    public List<Pos> emptiesFrom(Pos from) throws VectorAdditionFailedException {
        Pos[] list = {enPassantField(from), addTo(from)};
        return Arrays.asList(list);
    }
}
