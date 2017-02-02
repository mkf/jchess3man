package pl.edu.platinum.archiet.jchess3man.engine;

import java.util.List;

/**
 * Created by Michał Krzysztof Feiler on 02.02.17.
 */
public interface Board {
    List<List<Fig>> toListOfRanksOfFiles();

    Board copy();

    //List<Fig> toListOfSquares();
    default Fig get(Pos pos) {
        return get(pos.rank, pos.file);
    }

    Fig get(int rank, int file);

    default boolean isEmpty(int rank, int file) {
        return get(rank, file) == null;
    }

    default boolean isEmpty(Pos pos) {
        return get(pos) == null;
    }
}

