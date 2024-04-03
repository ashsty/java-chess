package chess.dao;

import chess.db.DBConnector;
import chess.db.DBException;
import chess.domain.piece.Team;
import chess.dto.TurnDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class TurnsDao {
    private final DBConnector dbConnector;

    public TurnsDao(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    public Optional<TurnDto> find() {
        return executeQuery("SELECT * FROM turns", "조회 실패");
    }

    public void save(TurnDto turnDto) {
        executeUpdate("INSERT INTO turns VALUES (?)", "저장 실패", turnDto.team().name());
    }

    public void update(TurnDto turnDto) {
        executeUpdate("UPDATE turns SET current_team = ? WHERE current_team = ?",
                "업데이트 실패",
                turnDto.team().name(), turnDto.team().toggleTeam().name());
    }

    public void delete() {
        executeUpdate("DELETE FROM turns", "삭제 실패");
    }

    private static Optional<TurnDto> convertToParsingFormat(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            var team = Team.convertToTeam(resultSet.getString("current_team"));
            TurnDto turns = new TurnDto(team);
            return Optional.of(turns);
        }
        return Optional.empty();
    }

    private Optional<TurnDto> executeQuery(String query, String errorMessage, Object... parameters) {
        try (final var connection = dbConnector.getConnection()) {
            final var statement = connection.prepareStatement(query);
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }
            final var resultSet = statement.executeQuery();

            return convertToParsingFormat(resultSet);
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
