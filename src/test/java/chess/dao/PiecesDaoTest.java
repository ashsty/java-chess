package chess.dao;

import chess.db.DBConnector;
import chess.domain.piece.Team;
import chess.domain.piece.Type;
import chess.domain.position.File;
import chess.domain.position.Rank;
import chess.dto.PieceDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PiecesDaoTest {
    private final DBConnector dbConnector = DBConnector.getTestDB();
    private final PiecesDao piecesDao = new PiecesDao(dbConnector);

    @BeforeEach
    void setUp() {
        try (Connection connection = dbConnector.getConnection()) {
            PreparedStatement resetTable = connection.prepareStatement("TRUNCATE TABLE pieces");
            resetTable.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("저장한 현재 Piece의 위치와 Piece 정보를 조회한다.")
    @Test
    void save_findAll() {
        // given
        PieceDto test = new PieceDto(File.A, Rank.ONE, Team.BLACK, Type.PAWN);

        // when
        piecesDao.save(test);

        // then
        assertThat(piecesDao.findAll()).isEqualTo(List.of(test));
    }

    @DisplayName("저장한 Piece의 위치와 정보를 변경한다.")
    @Test
    void update() {
        // given
        PieceDto test = new PieceDto(File.A, Rank.ONE, Team.BLACK, Type.PAWN);
        PieceDto update = new PieceDto(File.A, Rank.ONE, Team.WHITE, Type.KING);
        piecesDao.save(test);

        // when
        piecesDao.update(update);

        // then
        assertThat(piecesDao.findAll()).isEqualTo(List.of(update));
    }

    @DisplayName("저장한 Piece의 위치와 정보를 삭제한다.")
    @Test
    void delete() {
        // given
        PieceDto test = new PieceDto(File.A, Rank.ONE, Team.BLACK, Type.PAWN);
        PieceDto test2 = new PieceDto(File.A, Rank.TWO, Team.BLACK, Type.KING);
        piecesDao.save(test);
        piecesDao.save(test2);

        // when
        piecesDao.delete(test);

        // then
        assertThat(piecesDao.findAll()).isEqualTo(List.of(test2));
    }

    @DisplayName("저장한 Piece의 위치와 정보를 모두 삭제한다.")
    @Test
    void deleteAll() {
        // given
        PieceDto test = new PieceDto(File.A, Rank.ONE, Team.BLACK, Type.PAWN);
        PieceDto test2 = new PieceDto(File.A, Rank.TWO, Team.BLACK, Type.KING);
        piecesDao.save(test);
        piecesDao.save(test2);

        // when
        piecesDao.deleteAll();

        // then
        assertThat(piecesDao.findAll()).isEmpty();
    }
}
