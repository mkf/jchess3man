import pl.edu.platinum.archiet.jchess3man.engine.FromToPromMove;
import pl.edu.platinum.archiet.jchess3man.engine.GameState;
import pl.edu.platinum.archiet.jchess3man.engine.Move;

import java.util.concurrent.Future;

/**
 * Created by Michał Krzysztof Feiler on 18.03.17.
 */
public interface Player {
    Future<FromToPromMove> yourMove(GameState stateNow);

    void spectateChange(FromToPromMove move, GameState stateAfter);

    void youLost(GameState state);

    void youWon(GameState state);

    void youDrew(GameState state);

    void hurryUp();

    String toString();
}
