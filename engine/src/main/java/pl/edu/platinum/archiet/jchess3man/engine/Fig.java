package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.Nullable;
import pl.edu.platinum.archiet.jchess3man.engine.helpers.SingleElementIterable;

import java.util.Collections;

import static pl.edu.platinum.archiet.jchess3man.engine.helpers.BooleanHelpers.beq;

/**
 * Created by Michał Krzysztof Feiler on 02.02.17.
 */
public abstract class Fig extends Piece implements VecsInterface {
    Fig(FigType type, Color color) {
        super(type, color);
    }

    //public abstract Vector vec(Pos from, Pos to) throws CannotConstructVectorException;
    public interface VecInterface extends VecsInterface {
        Vector vec(Pos from, Pos to) throws CannotConstructVectorException;

        default Iterable<? extends Vector> vecs(Pos from, Pos to) {
            try {
                return new SingleElementIterable<>(vec(from, to));
            } catch (CannotConstructVectorException e) {
                return Collections.emptyList();
            }
        }
    }

    /**
     * @noinspection SameReturnValue
     */
    public Boolean pawnCenter() {
        return null;
    }

    public int sevenBitInt() {
        return ((beq(pawnCenter(), true)
                ? 1 << 6 :
                0) | color.index << 3 | type.index);
    }

    public byte sevenBit() {
        return (byte) sevenBitInt();
    }

    @Override
    public int hashCode() {
        return toInt();
    }

    public int toInt() {
        return sevenBitInt();
    }

    public byte toByte() {
        return sevenBit();
    }

    @Override
    public String toString() {
        char ourChar = toChar();
        return new String(new char[]{' ', ourChar});
    }

    public static Fig fromSevenBit(byte sb) {
        return fromSevenBit((int) sb);
    }
    public static Fig fromSevenBit(int sb) {
        if (sb < 0) throw new IllegalArgumentException(Integer.toString(sb));
        return subClass(sb & 7, Color.byIndex((sb >> 3) & 3), (sb >> 6 != 0));
    }

    public static Fig subClass(byte type, Color color, Boolean pawnCenter) {
        return subClass(FigType.fromIndex(type), color, pawnCenter);
    }

    public static Fig subClass(int type, Color color, Boolean pawnCenter) {
        return subClass((byte) type, color, pawnCenter);
    }

    public static Fig subClass(FigType type, Color color) {
        return subClass(type, color, null);
    }

    @Nullable
    public static Fig subClass(FigType type, Color color, Boolean pawnCenter) {
        switch (type) {
            case Rook:
                return new Fig.Rook(color);
            case Knight:
                return new Fig.Knight(color);
            case Bishop:
                return new Fig.Bishop(color);
            case King:
                return new Fig.King(color);
            case Pawn:
                return new Fig.Pawn(color, pawnCenter);
            case Queen:
                return new Fig.Queen(color);
            case ZeroFigType:
                return null;
        }
        throw new AssertionError("" + type + color + pawnCenter);
    }

    public static class Rook extends Fig {
        public Rook(Color color) {
            super(FigType.Rook, color);
        }

        @Override
        public Iterable<AxisVector> vecs(Pos from, Pos to) {
            return vectors(from, to);
        }

        public static Iterable<AxisVector> vectors(Pos from, Pos to) {
            return from.axisVectorsTo(to);
        }
    }

    public static class Knight extends Fig implements VecInterface {
        public Knight(Color color) {
            super(FigType.Knight, color);
        }

        public static KnightVector vector(Pos from, Pos to) throws CannotConstructVectorException {
            return from.knightVectorTo(to);
        }

        @Override
        public KnightVector vec(Pos from, Pos to) throws CannotConstructVectorException {
            return vector(from, to);
        }
    }

    public static class Bishop extends Fig {
        public Bishop(Color color) {
            super(FigType.Bishop, color);
        }

        public static Iterable<? extends DiagonalVector> vectors(Pos from, Pos to) {
            return from.diagonalVectorsTo(to);
        }

        @Override
        public Iterable<? extends DiagonalVector> vecs(Pos from, Pos to) {
            return vectors(from, to);
        }
    }

    public static class Queen extends Fig {
        public Queen(Color color) {
            super(FigType.Queen, color);
        }

        public static Iterable<? extends ContinuousVector> vectors(Pos from, Pos to) {
            return from.continuousVectorsTo(to);
        }

        @Override
        public Iterable<? extends ContinuousVector> vecs(Pos from, Pos to) {
            return vectors(from, to);
        }
    }

    public static class King extends Fig implements VecInterface {
        public King(Color color) {
            super(FigType.King, color);
        }

        public static Vector vector(Pos from, Pos to) throws CannotConstructVectorException {
            return from.kingVectorTo(to);
        }

        @Override
        public Vector vec(Pos from, Pos to) throws CannotConstructVectorException {// throws CannotConstructVectorException {
            return vector(from, to);
        }
    }

    public static class Pawn extends Fig implements VecInterface {
        public final boolean pawnCenter;

        public Pawn(Color color, boolean pawnCenter) {
            super(FigType.Pawn, color);
            this.pawnCenter = pawnCenter;
        }

        public Pawn(Color color) {
            this(color, false);
        }

        public static PawnVector vector(Pos from, Pos to) throws CannotConstructVectorException {
            return from.pawnVectorTo(to);
        }

        public static PawnVector vector(Pos from, Pos to, Boolean pawnCenter) throws CannotConstructVectorException {
            if (pawnCenter == null) return vector(from, to);
            return vector(from, to, pawnCenter.booleanValue());
        }

        public static PawnVector vector(Pos from, Pos to, boolean pawnCenter) throws CannotConstructVectorException {
            PawnVector tryin = vector(from, to);
            if (tryin.reqpc() != pawnCenter) throw new CannotConstructVectorException(from, to);
            return tryin;
        }

        @Override
        public PawnVector vec(Pos from, Pos to) throws CannotConstructVectorException {
            return vector(from, to, pawnCenter);
        }
    }
}

interface VecsInterface {
    Iterable<? extends Vector> vecs(Pos from, Pos to);
}
