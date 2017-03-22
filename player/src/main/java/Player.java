import pl.edu.platinum.archiet.jchess3man.engine.DescMove;
import pl.edu.platinum.archiet.jchess3man.engine.GameState;

import java.util.concurrent.Future;

/**
 * Created by Michał Krzysztof Feiler on 18.03.17.
 */
public interface Player {
    Future<DescMove> yourMove(GameState stateNow);

    void spectateChange(DescMove move, GameState stateAfter);

    void youLost(GameState state);

    void youWon(GameState state);

    void youDrew(GameState state);

    void hurryUp();

    String toString();
}
