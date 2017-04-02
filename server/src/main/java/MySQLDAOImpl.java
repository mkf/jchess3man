import org.intellij.lang.annotations.Language;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

/**
 * Created by Michał Krzysztof Feiler on 01.04.17.
 */
public class MySQLDAOImpl implements DAO {
    private Sql2o sql2o;

    public MySQLDAOImpl(String userName, String password) {
        this.sql2o = new Sql2o("jdbc:mysql://localhost:3306/archiet",
                userName, password);
    }

    @Override
    public Long saveGameplay(GameplayData gp) {
        @Language("MySQL") String sql =
                "INSERT INTO `3mangp` (state, white, gray, black, created) VALUES " +
                        "(:state, :whitePlayer, :grayPlayer, :blackPlayer, :created)";
        try (Connection con = sql2o.open()) {
            return (Long) con.createQuery(sql, true)
                    .bind(gp).executeUpdate().getKey();
        }
    }

    @Override
    public GameplayData loadGameplay(long key) {
        @Language("MySQL") String sql =
                "SELECT state, white, gray, black, created " +
                        "FROM 3mangp WHERE id = :id";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("id", key)
                    .executeAndFetchFirst(GameplayData.class);
        }
    }

    @Override
    public Long saveStateData(StateData sd) {
        @Language("MySQL") String sql =
                "INSERT INTO `3manst` " +
                        "(board, moats, movesnext, " +
                        "castling, enpassant, " +
                        "halfmoveclock, fullmovenumber, " +
                        "alive" +
                        ") VALUES (" +
                        "unhex(:hexBoard), :moats, :movesNext," +
                        ":castling, unhex(:enPassant), " +
                        ":halfMoveClock, :fullMoveNumber, " +
                        ":aliveColors" +
                        ") ON DUPLICATE KEY UPDATE id=last_insert_id(id)";
        try (Connection con = sql2o.open()) {
            return (Long) con.createQuery(sql, true)
                    .bind(sd).executeUpdate().getKey();
        }
    }

    @Override
    public StateData loadStateData(long key) {
        @Language("MySQL") String sql =
                "SELECT board, moats+0, movesnext, castling+0, enpassant," +
                        "halfmoveclock, fullmovenumber, alive+0 " +
                        "FROM `3manst` WHERE id=:id";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("id", key)
                    .executeAndFetchFirst(StateData.class);
        }
    }

    @Override
    public Long saveMoveData(MoveData md) {
        return null; //TODO
    }

    @Override
    public MoveData loadMoveData(long key) {
        return null; //TODO
    }
}
