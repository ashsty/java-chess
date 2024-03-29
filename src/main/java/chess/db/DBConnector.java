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
            throw new DBConnectionException(e.getMessage(), e);
        }
    }

    public static DBConnector getProductionDB() {
        return PRODUCTION_DATABASE;
    }

    public static DBConnector getTestDB() {
        return TEST_DATABASE;
    }

    /*public List<User> findAll() {

        try (final var connection = getConnection()) {
            final var statement = connection.prepareStatement("SELECT * FROM user");
            final var resultSet = statement.executeQuery();

            final var users = new ArrayList<User>();
            while (resultSet.next()) {
                var userId = resultSet.getString("user_id");
                var name = resultSet.getString("name");

                users.add(new User(userId, name));
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findById(final String userId) {

        try (final var connection = getConnection()) {
            final var statement = connection.prepareStatement("SELECT * FROM user WHERE user_id = ?");
            statement.setString(1, userId);
            final var resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new User(resultSet.getString("user_id"), resultSet.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void save(String userId, String name) {
        try (final var connection = getConnection()) {
            final var statement = connection.prepareStatement("INSERT INTO user VALUES (?, ?)");
            statement.setString(1, userId);
            statement.setString(2, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(String userId, String name) {
        try (final var connection = getConnection()) {
            final var statement = connection.prepareStatement("UPDATE user SET name = ? WHERE user_id = ?");
            statement.setString(1, name);
            statement.setString(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String userId) {
        try (final var connection = getConnection()) {
            final var statement = connection.prepareStatement("DELETE FROM user WHERE user_id = ?");
            statement.setString(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }*/
}
