package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static pl.edu.platinum.archiet.jchess3man.engine.CastlingVector.kfm;

/**
 * Created by Michał Krzysztof Feiler on 18.02.17.
 */
public class NewGameBoardImpl implements Board {
    public static final NewGameBoardImpl c = new NewGameBoardImpl();

    @Override
    public Board copy() {
        return c;
    }

    @Override
    public MutableBoard mutableCopy() {
        MutableBoard mut = new MutableHashMapBoardImpl();
        mut.fill(c);
        return mut;
    }

    @Override
    public @Nullable Fig get(int rank, int file) {
        if (rank == 1) return new Fig.Pawn(Color.fromSegm(file / 8));
        if (rank == 0) {
            final Color theColor = Color.fromSegm(file / 8);
            switch (file % 8) {
                case 0:
                case 7:
                    return new Fig.Rook(theColor);
                case 1:
                case 6:
                    return new Fig.Knight(theColor);
                case 2:
                case 5:
                    return new Fig.Bishop(theColor);
                case 3:
                    return new Fig.Queen(theColor);
                case 4:
                    return new Fig.King(theColor);
                default:
                    throw new IllegalArgumentException(file + " ");
            }
        }
        return null;
    }

    @Override
    public boolean isEmpty(int rank, int file) {
        return rank > 2;
    }

    @Override
    @NotNull
    @Contract(pure = true, value = "null -> fail")
    public Pos _whereIsKing(Color who) {
        return new Pos(0, kfm + (who.segm() << 3));
    }
}
