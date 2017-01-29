package pl.edu.platinum.archiet.jchess3man.engine;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Michał Krzysztof Feiler on 29.01.17.
 */
public class PawnLongJumpVector extends JumpVector implements PawnVector {
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

    Pos addTo(Pos from) {
        assert (from.rank == 1);
        return new Pos(3, from.file);
    }

    public Pos enPassantField(Pos from) {
        assert (from.rank == 1);
        return new Pos(2, from.file);
    }

    public Iterable<PawnLongJumpVector> units(int ignored) {
        return Collections.emptyList();
    }

    public Iterable<Color> moats(Pos ignored) {
        return Collections.emptyList();
    }

    @Override
    public List<Pos> emptiesFrom(Pos from) {
        Pos[] list = {enPassantField(from), addTo(from)};
        return Arrays.asList(list);
    }
}
