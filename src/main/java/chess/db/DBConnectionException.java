package chess.db;

import java.sql.SQLException;

public class DBConnectionException extends IllegalStateException {
    private static final String MESSAGE_PREFIX = "[ERROR]";

    public DBConnectionException(String message, SQLException e) {
        super(MESSAGE_PREFIX + message, e);
    }
}
