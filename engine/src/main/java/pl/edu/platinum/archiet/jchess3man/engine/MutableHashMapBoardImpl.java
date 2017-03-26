package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Michał Krzysztof Feiler on 02.02.17.
 */
public class MutableHashMapBoardImpl implements MutableBoard {
    private final HashMap<Pos, Fig> b = new HashMap<>();

    @Override
    public void clr(Pos pos) {
        b.remove(pos);
    }

    @Override
    public void put(Pos pos, Fig fig) {
        if (fig == null) clr(pos);
        else b.put(pos, fig);
    }

    public void put(Pos pos, int fig) {
        put(pos, (byte) fig);
    }

    public void put(Pos pos, byte fig) {
        if (fig == (byte) 0) clr(pos);
        put(pos, Fig.fromSevenBit(fig));
    }

    @NotNull
    @Override
    public MutableHashMapBoardImpl mutableCopy() {
        MutableHashMapBoardImpl n = new MutableHashMapBoardImpl();
        n.fill(this);
        return n;
    }

    @Override
    public @Nullable Fig get(@NotNull Pos pos) {
        return b.get(pos);
    }

    @Override
    public boolean isEmpty(@NotNull Pos pos) {
        return !b.containsKey(pos);
    }

    public Optional<Pos> whereIsSuchPiece(Piece suchPiece) {
        return b.entrySet().parallelStream()
                .filter((Map.Entry<Pos, Fig> entry) -> entry.getValue().equals(suchPiece))
                .map(Map.Entry::getKey).findAny();
    }

    @NotNull
    @Override
    public Optional<Pos> whereIsKing(@NotNull Color who) {
        return whereIsSuchPiece(new Fig.King(who));
    }

    @Override
    public @Nullable Pos _whereIsKing(@NotNull Color who) {
        Optional<Pos> posOptional = whereIsKing(who);
        return posOptional.isPresent() ? posOptional.get() : null;
    }

    @Override
    public void clearAll() {
        b.clear();
    }
}
