import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Timestamp;

/**
 * Created by Michał Krzysztof Feiler on 01.04.17.
 */
public class GameplayData {
    private Long id;
    private Long state;
    private @NotNull Players players = new Players();
    private Timestamp created;

    public static class Players {
        public Long white;
        public Long gray;
        public Long black;

        public Players copy() {
            Players c = new Players();
            c.white = white;
            c.gray = gray;
            c.black = black;
            return c;
        }
    }

    public Players getPlayers() {
        return players.copy();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
    }

    public Long getWhitePlayer() {
        return players.white;
    }

    public void setWhitePlayer(Long whitePlayer) {
        this.players.white = whitePlayer;
    }

    public Long getGrayPlayer() {
        return players.gray;
    }

    public void setGrayPlayer(Long grayPlayer) {
        this.players.gray = grayPlayer;
    }

    public Long getBlackPlayer() {
        return players.black;
    }

    public void setBlackPlayer(Long blackPlayer) {
        this.players.black = blackPlayer;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }
}
