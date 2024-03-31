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
    DBConnector dbConnector;

    public PiecesDao(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    public List<PieceDto> findAll() {
        try (final var connection = dbConnector.getConnection()) {
            final var statement = connection.prepareStatement("SELECT * FROM pieces");
            final var resultSet = statement.executeQuery();
            final var pieces = new ArrayList<PieceDto>();

            convertToParsingFormat(resultSet, pieces);
            return pieces;
        } catch (SQLException e) {
            throw new DBException("전체 조회 실패", e);
        }
    }

    public void save(PieceDto pieceDto) {
        try (final var connection = dbConnector.getConnection()) {
            final var statement = connection.prepareStatement("INSERT INTO pieces VALUES (?, ?, ?, ?)");
            statement.setString(1, pieceDto.file().name());
            statement.setString(2, pieceDto.rank().name());
            statement.setString(3, pieceDto.team().name());
            statement.setString(4, pieceDto.type().name());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DBException("전체 저장 실패", e);
        }
    }

    public void update(PieceDto pieceDto) {
        try (final var connection = dbConnector.getConnection()) {
            final var statement = connection.prepareStatement(
                    "UPDATE pieces SET piece_team = ?, piece_type =? WHERE board_file = ? AND board_rank =?");
            statement.setString(1, pieceDto.team().name());
            statement.setString(2, pieceDto.type().name());
            statement.setString(3, pieceDto.file().name());
            statement.setString(4, pieceDto.rank().name());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DBException("업데이트 실패", e);
        }
    }

    public void delete(PieceDto pieceDto) {
        try (final var connection = dbConnector.getConnection()) {
            final var statement = connection.prepareStatement(
                    "DELETE FROM pieces WHERE board_file = ? AND board_rank =?");
            statement.setString(1, pieceDto.file().name());
            statement.setString(2, pieceDto.rank().name());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAll() {
        try (final var connection = dbConnector.getConnection()) {
            final var statement = connection.prepareStatement(
                    "DELETE FROM pieces");
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
}
