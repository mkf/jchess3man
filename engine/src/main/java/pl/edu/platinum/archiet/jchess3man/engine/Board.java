package pl.edu.platinum.archiet.jchess3man.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by Michał Krzysztof Feiler on 02.02.17.
 */
public interface Board {
    default List<List<Fig>> toListOfRanksOfFiles() {
        ArrayList<List<Fig>> ret = new ArrayList<>();
        for (int i = 0; i < 6; i++) ret.set(i, toListOfSquaresInRank(i));
        return ret;
    }

    default List<Fig> toListOfSquaresInRank(int rank) {
        ArrayList<Fig> ret = new ArrayList<>(24);
        for (int i = 0; i < 24; i++) ret.set(i, get(rank, i));
        return ret;
    }

    default List<Fig> toListOfSquares() {
        ArrayList<Fig> ret = new ArrayList<>();
        for (int i = 0; i < 24 * 6; i++)
            ret.set(i, get(i / 24, i % 24));
        return ret;
    }

    default Map<Pos, Fig> toMapOfFigs() {
        HashMap<Pos, Fig> ret = new HashMap<>(144);
        AllPosIterable allPosIterable = new AllPosIterable();
        for (Pos pos : allPosIterable) {
            if (!isEmpty(pos)) ret.put(pos, get(pos));
        }
        return ret;
    }

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
        return isEmpty(pos.rank, pos.file);
    }

    default Pos whereIsKing(Color who) {
        final Fig.King suchKing = new Fig.King(who);
        for (final Pos pos : new AllPosIterable())
            if (suchKing.equals(get(pos))) return pos;
        return null;
    }

    default boolean isThereAThreat(
            Pos to,
            Pos from,
            PlayersAlive playersAlive,
            EnPassantStore enPassantStore
    ) {
        return isThereAThreat(to, from, playersAlive, enPassantStore, get(from));
    }

    default boolean isThereAThreat(
            Pos to,
            Pos from,
            PlayersAlive playersAlive,
            EnPassantStore enPassantStore,
            Fig fig
    ) {
        return isThereAThreat(to, from, playersAlive, enPassantStore,
                fig.vecs(from, to));
    }

    default boolean isThereAThreat(
            Pos to,
            Pos from,
            PlayersAlive playersAlive,
            EnPassantStore enPassantStore,
            Iterable<? extends Vector> vecs
    ) {
        return isThereAThreat(to, from, playersAlive, enPassantStore,
                StreamSupport
                        .stream(vecs.spliterator(), false)
        );
    }

    default boolean isThereAThreat(
            Pos to,
            Pos from,
            PlayersAlive playersAlive,
            EnPassantStore enPassantStore,
            Stream<? extends Vector> vecs
    ) {
        //Stream
        return false;
    }
}

