import pl.edu.platinum.archiet.jchess3man.engine.Color;
import pl.edu.platinum.archiet.jchess3man.engine.GameState;

import java.util.ArrayList;

/**
 * Created by Michał Krzysztof Feiler on 18.03.17.
 */
public class Gameplay {
    public final Player white;
    public final Player gray;
    public final Player black;
    public final GameState state;

    public Gameplay(Player white, Player gray, Player black, GameState state) {
        this.white = white;
        this.gray = gray;
        this.black = black;
        this.state = state;
    }

    public Gameplay(Gameplay prev, GameState newState) {
        this(prev.white, prev.gray, prev.black, newState);
    }

    public Player getPlayer(Color which) {
        switch (which) {
            case White:
                return white;
            case Gray:
                return gray;
            case Black:
                return black;
        }
        throw new IllegalArgumentException(which.toString());
    }

    boolean giveResult() {
        ArrayList<Color> listEm = state.alivePlayers.listEm();
        int count = listEm.size();
        switch (count) {
            case 0:
                white.youDrew(state);
                gray.youDrew(state);
                black.youDrew(state);
                return true;
        }
        for (final Color color : Color.colors)
            if (!state.alivePlayers.get(color))
                getPlayer(color).youLost(state);
        switch (count) {
            case 1:
                getPlayer(listEm.get(0)).youWon(state);
                return true;
        }
        return false;
    }
}
