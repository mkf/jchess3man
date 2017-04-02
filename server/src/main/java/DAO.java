import pl.edu.platinum.archiet.jchess3man.engine.GameState;

/**
 * Created by Michał Krzysztof Feiler on 01.04.17.
 */
public interface DAO {
    Long saveGameplay(GameplayData gp);

    GameplayData loadGameplay(long key);

    Long saveStateData(StateData sd);

    StateData loadStateData(long key);

    Long saveMoveData(MoveData md);

    MoveData loadMoveData(long key);

    //TODO: add more
    default Long saveState(GameState sta) {
        StateData sd = new StateData();
        sd.setState(sta);
        return saveStateData(sd);
    }

    default GameState loadState(long key) {
        return loadStateData(key).getState();
    }
}
