package pl.edu.platinum.archiet.jchess3man.engine;

/**
 * Created by Michał Krzysztof Feiler on 28.01.17.
 */
public class VectorAdditionFailedException extends Exception {
    public final Pos from;
    public final Vector vector;

    public VectorAdditionFailedException(Pos from, Vector vector) {
        this.from = from;
        this.vector = vector;
    }

    public static class SameSquare extends VectorAdditionFailedException {
        public SameSquare(Pos from, Vector vector) {
            super(from, vector);
        }
    }
}
