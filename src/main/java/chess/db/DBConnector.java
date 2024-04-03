package chess.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    private static final String SERVER = "localhost:13306";
    private static final String OPTION = "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final DBConnector PRODUCTION_DATABASE = new DBConnector("chess");
    private static final DBConnector TEST_DATABASE = new DBConnector("chess_test");

    private final String database;

    public DBConnector(String database) {
        this.database = database;
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://" + SERVER + "/" + database + OPTION, USERNAME, PASSWORD);
        } catch (final SQLException e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    public static DBConnector getProductionDB() {
        return PRODUCTION_DATABASE;
    }

    public static DBConnector getTestDB() {
        return TEST_DATABASE;
    }
}
