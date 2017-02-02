package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;

import static pl.edu.platinum.archiet.jchess3man.engine.helpers.BooleanHelpers.beq;

/**
 * Created by Michał Krzysztof Feiler on 24.01.17.
 */
public class Pos {
    //public final byte rank;
    //public final byte file;
    public final int rank;
    public final int file;

    //public Pos(byte rank, byte file) {
    public Pos(int rank, int file) {
        this.rank = rank;
        this.file = file;
    }

    public Pos(short rank, short file) {
        this((int) rank, (int) file);
    }

    public Pos(byte rank, byte file) {
        this((int) rank, (int) file);
    }

    public Pos(Color color, int rank, int colorFile) {
        this(rank, (color.board() << 3) + colorFile);
    }

    public static final Pos zero = new Pos(0, 0);

    public Pos addVec(Vector vec) throws VectorAdditionFailedException {
        return vec.addTo(this);
    }

    public String toString() {
        return "[" + rank + "," + file + "]";
    }

    public Color colorSegm() {
        return Color.colors[file / 8];
    }

    public Pos next() {
        return (rank == 5 && file == 23) ? null : new Pos(file == 23 ? rank + 1 : rank, file == 23 ? 0 : file + 1);
    }

    public boolean sameRank(Pos ano) {
        return rank == ano.rank;
    }

    public boolean sameFile(Pos ano) {
        return file == ano.file;
    }

    public boolean equals(Pos ano) {
        return sameFile(ano) && sameRank(ano);
    }

    public int toInt() {
        return rank * 24 + file;
    }

    @Override
    public int hashCode() {
        assert (file < 24);
        //return rank<<5 | file;
        return toInt();
    }

    public boolean equals(Object obj) {
        return obj instanceof Pos && equals((Pos) obj);
    }

    public boolean isAdjacentFile(Pos ano) {
        return file + 12 % 24 == ano.file;
    }

    public boolean isSameOrAdjacentFile(Pos ano) {
        return file + 12 % 24 == ano.file % 12;
    }

    public CanIDiagonal canIDiagonalTo(Pos ano) {
        return new CanIDiagonal(this, ano);
    }

    public boolean diagonalSomehow(Pos ano) {
        return canIDiagonalTo(ano).toBool();
    }

    public KnightVector knightVectorTo(Pos ano)
            throws CannotConstructVectorException {
        return KnightVector.knightVector(this, ano);
    }

    public RankVector rankVectorTo(Pos ano) throws CannotConstructVectorException {
        if (isSameOrAdjacentFile(ano))
            return new RankVector(
                    sameFile(ano) ? ano.rank - rank : 11 - (rank + ano.rank));
        throw new CannotConstructVectorException(this, ano);
    }

    public FileVector fileVectorTo(Pos ano)
            throws CannotConstructVectorException {
        if (sameRank(ano)) {
            return new FileVector(wrappedFileVector(file, ano.file));
        }
        throw new CannotConstructVectorException(this, ano);
    }

    /**
     * @noinspection SameParameterValue
     */
    public FileVector fileVectorTo(Pos ano, boolean wLong)
            throws CannotConstructVectorException {
        if (sameRank(ano)) {
            return new FileVector(wrappedFileVector(file, ano.file, wLong));
        }
        throw new CannotConstructVectorException(this, ano);
    }

    public FileVector fileVectorLongTo(Pos ano)
            throws CannotConstructVectorException {
        return fileVectorTo(ano, true);
    }

    public ArrayList<AxisVector> axisVectorsTo(Pos ano) {
        ArrayList<AxisVector> arr = new ArrayList<>();
        try {
            arr.add(rankVectorTo(ano));
        } catch (CannotConstructVectorException ignored) {
        }
        try {
            arr.add(fileVectorTo(ano));
        } catch (CannotConstructVectorException ignored) {
        }
        try {
            arr.add(fileVectorLongTo(ano));
        } catch (CannotConstructVectorException ignored) {
        }
        return arr;
    }

    public FileVector kingFileVectorTo(Pos ano) throws CannotConstructVectorException {
        FileVector tryin = new FileVector(1, true);
        if (tryin.addTo(this).equals(ano)) return tryin;
        tryin = new FileVector(1, false);
        if (tryin.addTo(this).equals(ano)) return tryin;
        throw new CannotConstructVectorException(this, ano);
    }

    public RankVector kingRankVectorTo(Pos ano) throws CannotConstructVectorException {
        RankVector tryin = new RankVector(1, true);
        if (tryin.addTo(this).equals(ano)) return tryin;
        tryin = new RankVector(1, false);
        if (tryin.addTo(this).equals(ano)) return tryin;
        throw new CannotConstructVectorException(this, ano);
    }

    public AxisVector kingAxisVectorTo(Pos ano) throws CannotConstructVectorException {
        try {
            return kingFileVectorTo(ano);
        } catch (CannotConstructVectorException ignored) {
            return kingRankVectorTo(ano);
        }
    }

    public DiagonalVector kingDiagonalVectorTo(Pos ano)
            throws CannotConstructVectorException {
        if (rank == 5 && ano.rank == 5) {
            SolelyThruCenterDiagonalVector theSolely =
                    new SolelyThruCenterDiagonalVector(true);
            try {
                if (theSolely.addTo(this).equals(ano))
                    return theSolely;
            } catch (VectorAdditionFailedException ignored) {
            }
            theSolely = new SolelyThruCenterDiagonalVector(false);
            try {
                if (theSolely.addTo(this).equals(ano))
                    return theSolely;
            } catch (VectorAdditionFailedException ignored) {
            }
            throw new CannotConstructVectorException(this, ano);
        }
        DirectDiagonalVector theDirect;
        theDirect = new DirectDiagonalVector(1, 1);
        if (theDirect.addTo(this).equals(ano))
            return theDirect;
        theDirect = new DirectDiagonalVector(-1, -1);
        if (theDirect.addTo(this).equals(ano))
            return theDirect;
        theDirect = new DirectDiagonalVector(1, -1);
        if (theDirect.addTo(this).equals(ano))
            return theDirect;
        theDirect = new DirectDiagonalVector(-1, 1);
        if (theDirect.addTo(this).equals(ano))
            return theDirect;
        throw new CannotConstructVectorException(this, ano);
    }

    public ContinuousVector kingContinuousVectorTo(Pos ano)
            throws CannotConstructVectorException {
        try {
            return kingAxisVectorTo(ano);
        } catch (CannotConstructVectorException ignored) {
            return kingDiagonalVectorTo(ano);
        }
    }

    public CastlingVector castlingVectorTo(Pos ano) throws CannotConstructVectorException {
        return CastlingVector.castlingVector(this, ano);
    }

    public Vector kingVectorTo(Pos ano) throws CannotConstructVectorException {
        try {
            return kingContinuousVectorTo(ano);
        } catch (CannotConstructVectorException ignored) {
            return castlingVectorTo(ano);
        }
    }

    public DiagonalVector shorterDiagonalVectorTo(Pos ano) throws CannotConstructVectorException {
        //return shorterDiagonalVectorTo(ano, null, null, null);
        if (rank != ano.rank) {
            boolean inward = ano.rank > rank;
            int shorttd = (!inward ? rank - ano.rank : ano.rank - rank);
            if (ano.file == (file + shorttd) % 24)
                return new DirectDiagonalVector(shorttd, inward, true);
            if (ano.file == (file - shorttd) % 24)
                return new DirectDiagonalVector(shorttd, inward, false);
        } else {
            int rankSum = ano.rank + rank;
            if (ano.file == (file + rankSum) % 24)
                return new LongDiagonalVector(rankSum, false);
            if (ano.file == (file - rankSum) % 24)
                return new LongDiagonalVector(rankSum, true);
        }
        throw new CannotConstructVectorException(this, ano);
    }

    public DiagonalVector shorterDiagonalVectorTo(
            Pos ano, Boolean positiveSgn, Boolean wShort, Boolean wLong)
            throws CannotConstructVectorException {
        if (!beq(wShort, false) &&
                rank != ano.rank) {
            boolean inward = ano.rank > rank;
            int shorttd = (!inward ? rank - ano.rank : ano.rank - rank);
            if (!beq(positiveSgn, false) &&
                    ano.file == (file + shorttd) % 24)
                return new DirectDiagonalVector(shorttd, inward, true);
            if (!beq(positiveSgn, true) &&
                    ano.file == (file - shorttd) % 24)
                return new DirectDiagonalVector(shorttd, inward, false);
        } else if (!beq(wLong, false)) {
            int rankSum = ano.rank + rank;
            if (!beq(positiveSgn, true) &&
                    ano.file == (file + rankSum) % 24)
                return new LongDiagonalVector(rankSum, false);
            if (!beq(positiveSgn, false) &&
                    ano.file == (file - rankSum) % 24)
                return new LongDiagonalVector(rankSum, true);
        }
        throw new CannotConstructVectorException(this, ano);
    }

    public LongDiagonalVector longerDiagonalVectorTo(Pos ano, DiagonalVector shorter) throws CannotConstructVectorException {
        if (shorter instanceof DirectDiagonalVector)
            return longerDiagonalVectorTo(ano, (DirectDiagonalVector) shorter);
        throw new CannotConstructVectorException(this, ano);
    }

    public LongDiagonalVector longerDiagonalVectorTo(Pos ano, DirectDiagonalVector shorter) throws CannotConstructVectorException {
        int ranksum = ano.rank + rank;
        if (ano.file == (shorter.plusFile ? file - ranksum : file + ranksum) % 24)
            return new LongDiagonalVector(ranksum, shorter.plusFile);
        throw new CannotConstructVectorException(this, ano);
    }

    public ArrayList<DiagonalVector> diagonalVectorsTo(Pos ano) {
        ArrayList<DiagonalVector> ret = new ArrayList<>();
        try {
            DiagonalVector shorter = shorterDiagonalVectorTo(ano);
            ret.add(shorter);
            ret.add(longerDiagonalVectorTo(ano, shorter));
        } catch (CannotConstructVectorException ignored) {
        }
        return ret;
    }

    public ArrayList<ContinuousVector> continuousVectorsTo(Pos ano) {
        ArrayList<ContinuousVector> ret = new ArrayList<>();
        ret.addAll(axisVectorsTo(ano));
        ret.addAll(diagonalVectorsTo(ano));
        return ret;
    }

    public PawnWalkVector pawnWalkVectorTo(Pos ano) throws CannotConstructVectorException {
        PawnWalkVector tryin = new PawnWalkVector(true);
        if (tryin.addTo(this).equals(ano)) return tryin;
        tryin = new PawnWalkVector(false);
        if (tryin.addTo(this).equals(ano)) return tryin;
        throw new CannotConstructVectorException(this, ano);
    }

    public PawnLongJumpVector pawnLongJumpVectorTo(Pos ano) throws CannotConstructVectorException {
        if (PawnLongJumpVector.willDo(this, ano))
            return new PawnLongJumpVector();
        throw new CannotConstructVectorException(this, ano);
    }

    public PawnCapVector pawnCapVectorTo(Pos ano) throws CannotConstructVectorException {
        return PawnCapVector.pawnCapVector(this, ano);
    }

    public PawnVector pawnVectorTo(Pos ano) throws CannotConstructVectorException {
        try {
            return pawnLongJumpVectorTo(ano);
        } catch (CannotConstructVectorException ignored) {
            try {
                return pawnWalkVectorTo(ano);
            } catch (CannotConstructVectorException ignoredAsWell) {
                return pawnCapVectorTo(ano);
            }
        }
    }

    @Contract(pure = true)
    private static int wrappedFileVector(int from, int to) {
        return wrappedFileVector(from, to, false);
    }

    @Contract(pure = true)
    private static int wrappedFileVector(int from, int to, boolean wLong) {
        int diff = to - from;
        int sgn = diff < 0 ? -1 : 1;
        return ((diff * sgn > 12) == wLong) ? diff : (diff - 24 * sgn);
    }
}
