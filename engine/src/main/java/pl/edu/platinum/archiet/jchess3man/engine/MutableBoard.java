package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface MutableBoard extends Board {
    default void put(int rank, int file, Fig fig) {
        put(new Pos(rank, file), fig);
    }

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

    @NotNull
    default MutableBoard copy() {
        return mutableCopy();
    }

    default void fill(List<List<? extends Fig>> listOfRanksOfFiles) {
        for (Pos pos : new AllPosIterable())
            put(pos, listOfRanksOfFiles.get(pos.rank).get(pos.file));
    }

    default void fill(Fig[][] arrayOfRanksOfFiles) {
        for (Pos pos : new AllPosIterable())
            put(pos, arrayOfRanksOfFiles[pos.rank][pos.file]);
    }

    default void fill(Fig[] arrayOfSquares) {
        for (int i = 0; i < 24 * 6; i++) {
            put(i / 24, i % 24, arrayOfSquares[i]);
        }
    }

    default void fill(Map<Pos, Fig> mapOfFigs) {
        for (Pos pos : new AllPosIterable()) {
            if (mapOfFigs.containsKey(pos))
                put(pos, mapOfFigs.get(pos));
            else clr(pos);
        }
    }

    default void fill(Board source) {
        for (Pos pos : new AllPosIterable()) {
            if (source.isEmpty(pos)) clr(pos);
            put(pos, source.get(pos));
        }
    }

    void clearAll();
}
