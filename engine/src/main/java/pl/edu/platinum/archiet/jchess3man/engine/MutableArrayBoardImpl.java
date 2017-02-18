package pl.edu.platinum.archiet.jchess3man.engine;

/**
 * Created by Michał Krzysztof Feiler on 02.02.17.
 */
public class MutableArrayBoardImpl implements MutableBoard {
    private final byte[][] b = new byte[6][24];

    @Override
    public void clr(int rank, int file) {
        put(rank, file, 0);
    }

    @Override
    public void put(int rank, int file, Fig fig) {
        if (fig == null) clr(rank, file);
        else put(rank, file, fig.sevenBit());
    }

    public void put(int rank, int file, int fig) {
        put(rank, file, (byte) fig);
    }

    public void put(int rank, int file, byte fig) {
        b[rank][file] = fig;
    }

    @Override
    public MutableArrayBoardImpl mutableCopy() {
        MutableArrayBoardImpl n = new MutableArrayBoardImpl();
        n.fill(this);
        return n;
    }

    public byte getByte(int rank, int file) {
        return b[rank][file];
    }

    @Override
    public Fig get(int rank, int file) {
        return Fig.fromSevenBit(getByte(rank, file));
    }

    @Override
    public void clearAll() {
        fill(new MutableHashMapBoardImpl());
    }
}
