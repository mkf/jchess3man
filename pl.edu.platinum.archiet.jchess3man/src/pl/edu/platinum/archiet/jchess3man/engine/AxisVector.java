package pl.edu.platinum.archiet.jchess3man.engine;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Michał Krzysztof Feiler on 27.01.17.
 */
public abstract class AxisVector extends ContinuousVector {
    public final boolean direc;
    public AxisVector(int abs, boolean direc) {
        super(abs);
        this.direc=direc;
    }
    public AxisVector(int t) {
        super(Math.abs(t));
        this.direc=(t>0);
    }
    public int t() { return direc?abs:-abs; }
    public abstract AxisVector head([int _]);
    public abstract Vector tail([int _]);
    public class FileVector extends AxisVector {
        public FileVector(int abs, boolean direc) { super(abs,direc); }
        public FileVector(int t) { super(t); }
        public int file() { return t();}
        public int rank() { return 0;}
        public FileVector head([int _]) {
            if(abs>0) {
                if(abs>1) {
                    return new FileVector(1, direc);
                }
                return this;
            }
            throw new AssertionError(this);
        }
        public Vector tail([int _]) {
            if(abs>0) {
                if(abs>1) {
                    return new FileVector(abs-1,direc);
                }
                return new ZeroVector();
            }
            throw new AssertionError(this);
        }
        public Iterable<Color> moats(Pos from) {
            if(from.rank!=0) return Collections.emptyList();
            int left = from.file%8;
            int tm = direc ? 8-left:left;
            Color start = from.colorSegm();
            int moating = abs-tm;
            ArrayList<Color> moatsToReturn = new ArrayList<>();
            if(moating>0) {
                moatsToReturn.add(direc?start:start.previous());
                if(moating>8) {
                    moatsToReturn.add(start.next());
                    if(moating>16) {
                        moatsToReturn.add(direc?start.previous():start);
                    }
                }
            }
            return moatsToReturn;
        }

        @Override
        Pos addTo(Pos from) {
            return new Pos(from.rank,(from.file+this.file())%24);
        }
    }
}
