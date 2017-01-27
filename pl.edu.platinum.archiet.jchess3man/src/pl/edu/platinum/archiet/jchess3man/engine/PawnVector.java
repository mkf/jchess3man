package pl.edu.platinum.archiet.jchess3man.engine;

import java.util.Collections;
import java.util.List;

/**
 * Created by Michał Krzysztof Feiler on 25.01.17.
 */
public interface PawnVector {
    public boolean reqpc();
    public boolean reqProm(int rank);

    class PawnLongJumpVector extends JumpVector implements PawnVector {
        public PawnLongJumpVector() {}
        public int rank() {return 2;}
        public int file() {return 0;}
        public boolean reqpc() {return false;}
        public boolean reqProm(int _) {return false;}
        Pos addTo(Pos from) {
            assert(from.rank==1);
            return new Pos(3,from.file);
        }
        public Pos enPassantField(Pos from) {
            assert(from.rank==1);
            return new Pos(2, from.file);
        }
        Iterable<Vector> units(int _) {
            return Collections.emptyList();
        }
        Iterable<Color> moats(Pos _) {
            return Collections.emptyList();
        }

        @Override
        public Iterable<Pos> emptiesFrom(Pos from) {
            return new List<Pos>{enPassantField(from),addTo(from)}
        }
    }

    //class PawnWalkVector extends RankVector implements PawnVector {}
}
