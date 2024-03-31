package chess.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    private static final String SERVER = "localhost:13306"; // MySQL 서버 주소
    private static final String OPTION = "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USERNAME = "root"; //  MySQL 서버 아이디
    private static final String PASSWORD = "root"; // MySQL 서버 비밀번호
    private static final DBConnector PRODUCTION_DATABASE = new DBConnector("chess"); // MySQL DATABASE 이름
    private static final DBConnector TEST_DATABASE = new DBConnector("chess_test"); // MySQL TEST DATABASE 이름

    private final String database;

    public DBConnector(String database) {
        this.database = database;
    }

    public Connection getConnection() {
        // 드라이버 연결
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
