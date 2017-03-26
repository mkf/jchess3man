package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public interface ImmutableBoard extends Board {
    default ImmutableBoard put(int rank, int file, @Nullable Fig fig) {
        return put(new Pos(rank, file), fig);
    }

    default ImmutableBoard put(@NotNull Pos pos, @Nullable Fig fig) {
        return put(pos.rank, pos.file, fig);
    }

    default ImmutableBoard clr(int rank, int file) {
        return put(rank, file, null);
    }

    default ImmutableBoard clr(@NotNull Pos pos) {
        return put(pos, null);
    }

    default ImmutableBoard move(@NotNull Pos from, @NotNull Pos to) {
        return put(to, get(from)).clr(from);
    }

    /**
     * @return not really a copy by default but [this]
     */
    @NotNull
    @Override
    default ImmutableBoard copy() {
        return this;
    }
}
