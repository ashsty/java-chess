package chess.db;

import java.sql.SQLException;

public class DBException extends IllegalStateException {
    private static final String MESSAGE_PREFIX = "[ERROR]";

    public DBException(String message, SQLException e) {
        super(MESSAGE_PREFIX + message, e);
    }
}
