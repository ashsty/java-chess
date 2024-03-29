package chess.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;

class DBConnectorTest {
    private final DBConnector dbConnector = DBConnector.getTestDB();
    @DisplayName("연결 여부를 확인한다.")
    @Test
    void getConnection() {
        assertAll(
                () -> assertThatCode(dbConnector::getConnection).doesNotThrowAnyException(),
                () -> assertThat(dbConnector.getConnection()).isNotNull()
        );
    }
}
