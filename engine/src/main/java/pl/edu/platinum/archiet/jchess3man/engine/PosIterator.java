package pl.edu.platinum.archiet.jchess3man.engine;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Michał Krzysztof Feiler on 02.02.17.
 */
public class PosIterator implements Iterator<Pos> {
    private Pos posAlready = null;

    public PosIterator() {
    }

    public PosIterator(Pos wasAlready) {
        setPosAlready(wasAlready);
    }

    public PosIterator(int rankCompletedAlready) {
        completeRank(rankCompletedAlready);
    }

    public PosIterator(int rankPosAlready, int filePosAlready) {
        setPosAlready(rankPosAlready, filePosAlready);
    }

    public Pos getPosAlready() {
        return posAlready;
    }

    public void completeRank(int rankCompletedAlready) {
        setPosAlready(rankCompletedAlready, 23);
    }

    public void setPosAlready(Pos posAlready) {
        this.posAlready = posAlready;
    }

    public void setPosAlready(int rankPosAlready, int filePosAlready) {
        setPosAlready(new Pos(rankPosAlready, filePosAlready));
    }

    @Override
    public boolean hasNext() {
        return posAlready == null ||
                !(posAlready.rank == 5 && posAlready.file == 23);
    }

    @Override
    public Pos next() throws NoSuchElementException {
        if (posAlready == null) posAlready = new Pos(0, 0);
        if (posAlready.file != 23)
            posAlready = new Pos(posAlready.rank, posAlready.file + 1);
        if (posAlready.rank == 5) throw new NoSuchElementException();
        posAlready = new Pos(posAlready.rank + 1, 0);
        return posAlready;
    }
}
