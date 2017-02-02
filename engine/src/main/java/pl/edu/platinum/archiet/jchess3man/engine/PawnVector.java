package pl.edu.platinum.archiet.jchess3man.engine;

/**
 * Created by Michał Krzysztof Feiler on 25.01.17.
 */
public interface PawnVector extends Vector {
    boolean reqpc();

    boolean reqProm(int rank);

}
