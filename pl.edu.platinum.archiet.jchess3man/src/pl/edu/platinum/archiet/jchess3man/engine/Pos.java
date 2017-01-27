package pl.edu.platinum.archiet.jchess3man.engine;

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

    public Pos addVec(Vector vec) { return vec.addTo(this); }

    public String toString() {
        return "[" + rank + "," + file "]";
    }

    public Color colorSegm() {
        return Color.colors[file / 8];
    }

    public Pos next() {
        return (rank == 5 && file == 23) ? null : new Pos(file == 23 ? rank + 1 : rank, file == 23 ? 0 : file + 1);
    }

    public boolean sameRank(Pos ano) {return rank==ano.rank;}
    public boolean sameFile(Pos ano) {return file==ano.file;}
    public boolean equals(Pos ano) {
        return sameFile(ano)&&sameRank(ano);
    }
    public boolean equals(Object obj) {
        if(!(obj instanceof Pos)) return false;
        return equals((Pos) obj);
    }
    public boolean  isAdjacentFile(Pos ano) { return file+12%24==ano.file; }
    public boolean isSameOrAdjacentFile(Pos ano) { return file+12%24==ano.file%12; }
}
