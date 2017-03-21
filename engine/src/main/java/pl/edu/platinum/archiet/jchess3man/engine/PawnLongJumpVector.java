package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Michał Krzysztof Feiler on 29.01.17.
 */
public class PawnLongJumpVector implements JumpVector, PawnVector {
    public PawnLongJumpVector() {
    }

    public int rank() {
        return 2;
    }

    public int file() {
        return 0;
    }

    public boolean reqpc() {
        return false;
    }

    public boolean reqProm(int ignored) {
        return false;
    }

    public Pos addTo(Pos from) throws VectorAdditionFailedException {
        if (from.rank != 1) throw new VectorAdditionFailedException(from, this);
        return new Pos(3, from.file);
    }

    @Contract(pure = true)
    public static boolean willDo(Pos from, Pos to) {
        return from.rank == 1 && to.rank == 3 && from.file == to.file;
    }

    public Pos enPassantField(Pos from) {
        assert (from.rank == 1);
        return new Pos(2, from.file);
    }

    public Iterable<PawnLongJumpVector> units(int ignored) {
        return Collections.emptyList();
    }

    public Iterable<@NotNull Color> moats(Pos ignored) {
        return Collections.emptyList();
    }

    @Override
    public List<Pos> emptiesFrom(Pos from) throws VectorAdditionFailedException {
        Pos[] list = {enPassantField(from), addTo(from)};
        return Arrays.asList(list);
    }
}
