package chess.dao;

import chess.db.DBConnector;
import chess.db.DBException;
import chess.domain.piece.Team;
import chess.domain.piece.Type;
import chess.domain.position.File;
import chess.domain.position.Rank;
import chess.dto.PieceDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PiecesDao {
    private final DBConnector dbConnector;

    public PiecesDao(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    public List<PieceDto> findAll() {
        return executeQuery("SELECT * FROM pieces", "전체 조회 실패");
    }

    public List<PieceDto> findByPosition(PieceDto pieceDto) {
        return executeQuery("SELECT * FROM pieces WHERE board_file = ? AND board_rank = ?",
                "조회 실패",
                pieceDto.file().name(), pieceDto.rank().name());
    }

    public void save(PieceDto pieceDto) {
        executeUpdate("INSERT INTO pieces VALUES (?, ?, ?, ?)",
                "저장 실패",
                pieceDto.file().name(), pieceDto.rank().name(),
                pieceDto.team().name(), pieceDto.type().name());
    }

    public void update(PieceDto pieceDto) {
        executeUpdate("UPDATE pieces SET piece_team = ?, piece_type = ? WHERE board_file = ? AND board_rank = ?",
                "업데이트 실패",
                pieceDto.team().name(), pieceDto.type().name(),
                pieceDto.file().name(), pieceDto.rank().name());
    }

    public void delete(PieceDto pieceDto) {
        executeUpdate("DELETE FROM pieces WHERE board_file = ? AND board_rank = ?",
                "삭제 실패",
                pieceDto.file().name(), pieceDto.rank().name());
    }

    public void deleteAll() {
        executeUpdate("DELETE FROM pieces", "전체 삭제 실패");
    }

    private static void convertToParsingFormat(ResultSet resultSet, ArrayList<PieceDto> pieces) throws SQLException {
        while (resultSet.next()) {
            var file = File.convertToFile(resultSet.getString("board_file"));
            var rank = Rank.convertToRank(resultSet.getString("board_rank"));
            var team = Team.convertToTeam(resultSet.getString("piece_team"));
            var type = Type.convertToType(resultSet.getString("piece_type"));

            pieces.add(new PieceDto(file, rank, team, type));
        }
    }

    private List<PieceDto> executeQuery(String query, String errorMessage, Object... parameters) {
        try (final var connection = dbConnector.getConnection()) {
            final var statement = connection.prepareStatement(query);
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }
            final var resultSet = statement.executeQuery();
            final var pieces = new ArrayList<PieceDto>();

            convertToParsingFormat(resultSet, pieces);
            return pieces;
        } catch (SQLException e) {
            throw new DBException(errorMessage, e);
        }
    }

    private void executeUpdate(String query, String errorMessage, Object... parameters) {
        try (final var connection = dbConnector.getConnection()) {
            final var statement = connection.prepareStatement(query);
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(errorMessage, e);
        }
    }
}
