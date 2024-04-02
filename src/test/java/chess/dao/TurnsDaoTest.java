package chess.dao;

import chess.db.DBConnector;
import chess.db.DBException;
import chess.domain.piece.Team;
import chess.domain.piece.Type;
import chess.domain.position.File;
import chess.domain.position.Rank;
import chess.dto.PieceDto;
import chess.dto.TurnDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TurnsDaoTest
{
    private final DBConnector dbConnector = DBConnector.getTestDB();
    private final TurnsDao turnsDao = new TurnsDao(dbConnector);

    @BeforeEach
    void setUp() {
        try (Connection connection = dbConnector.getConnection()) {
            PreparedStatement resetTable = connection.prepareStatement("TRUNCATE TABLE turns");
            resetTable.executeUpdate();
        } catch (SQLException e) {
            throw new DBException("초기화 실패", e);
        }
    }

    @DisplayName("저장한 현재 turn을 조회한다.")
    @Test
    void save_find() {
        // given
        TurnDto test = new TurnDto(Team.BLACK);

        // when
        turnsDao.save(test);

        // then
        assertThat(turnsDao.find()).isEqualTo(Optional.of(test));
    }

    @DisplayName("저장한 turn 정보를 변경한다.")
    @Test
    void update() {
        // given
        TurnDto test = new TurnDto(Team.BLACK);
        TurnDto update = new TurnDto(Team.WHITE);
        turnsDao.save(test);

        // when
        turnsDao.update(update);

        // then
        assertThat(turnsDao.find()).isEqualTo(Optional.of(update));
    }

    @DisplayName("저장한 turn 정보를 삭제한다.")
    @Test
    void delete() {
        // given
        TurnDto test = new TurnDto(Team.BLACK);
        TurnDto test2 = new TurnDto(Team.WHITE);
        turnsDao.save(test);
        turnsDao.save(test2);

        // when
        turnsDao.delete();

        // then
        assertThat(turnsDao.find()).isEmpty();
    }
}
