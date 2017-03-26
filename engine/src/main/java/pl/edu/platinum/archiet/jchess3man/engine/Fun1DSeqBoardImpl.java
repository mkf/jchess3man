package pl.edu.platinum.archiet.jchess3man.engine;

import javaslang.collection.Array;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static javaslang.collection.Array.ofAll;

/**
 * Created by Michał Krzysztof Feiler on 25.03.17.
 * This is an immutable and persistent (i.e. functional)
 * implementation of (Immutable)Board, utilizing
 * one-dimensional javaslang.collection.Array.
 * It also has a boolean field describing whether we
 * ranks or files concatenated in it.
 */
public class Fun1DSeqBoardImpl implements ImmutableBoard {
    public final Array<@Nullable Fig> b;
    /**
     * whether ranks instead of files should be concatenated
     */
    public final boolean rnf;

    public int idx(int rank, int file) {
        return Board.idx(rnf, rank, file);
    }

    public Pos idx(int index) {
        return Board.idx(rnf, index);
    }

    public Fun1DSeqBoardImpl(Array<@Nullable Fig> b, boolean concatRanksNotFiles) {
        this.b = b;
        this.rnf = concatRanksNotFiles;
    }

    public Fun1DSeqBoardImpl(Board source, boolean concatRanksNotFiles) {
        //this.b = Array.ofAll(source.toListOfRanksOfFiles())
        //        .map(Array::ofAll);
        this.rnf = concatRanksNotFiles;
        this.b = Array.ofAll(rnf
                ? source.toListOfSquaresConcatRanks()
                : source.toListOfSquaresConcatFiles());
    }

    @Override
    public Fun1DSeqBoardImpl put(int rank, int file, @Nullable Fig fig) {
        return new Fun1DSeqBoardImpl(b.insert(idx(file, rank), fig), rnf);
    }

    /**
     * @return not really a copy, just new([this.b])
     */
    @NotNull
    @Override
    public Fun1DSeqBoardImpl copy() {
        return new Fun1DSeqBoardImpl(b, rnf);
    }

    public Fun1DSeqBoardImpl concatenatingRanksOrFiles(boolean ranksNotFiles) {
        if (rnf == ranksNotFiles) return this;
        return new Fun1DSeqBoardImpl(this, ranksNotFiles);
    }

    @Override
    public @Nullable Fig get(int rank, int file) {
        //return b.get(rank).get(file);
        return b.get(idx(rank, file));
    }

    @Override
    public boolean isEmpty(int rank, int file) {
        return get(rank, file) == null;
    }

    @NotNull
    @Override
    public Optional<Pos> whereIsKing(@NotNull Color who) {
        int idx = b.indexOf(new Fig.King(who));
        if (idx == -1) return Optional.empty();
        return Optional.of(idx(idx));
    }
}
