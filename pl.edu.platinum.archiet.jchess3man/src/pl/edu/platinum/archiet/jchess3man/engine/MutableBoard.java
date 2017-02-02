package pl.edu.platinum.archiet.jchess3man.engine;

public interface MutableBoard extends Board {
    void put(int rank, int file, Fig fig);

    default void put(Pos pos, Fig fig) {
        put(pos.rank, pos.file, fig);
    }

    default void clr(int rank, int file) {
        put(rank, file, null);
    }

    default void clr(Pos pos) {
        put(pos, null);
    }

    default void move(Pos from, Pos to) {
        put(to, get(from));
        clr(from);
    }
}
