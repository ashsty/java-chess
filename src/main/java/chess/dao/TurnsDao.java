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
        try (final var connection = dbConnector.getConnection()) {
            final var statement = connection.prepareStatement("SELECT * FROM turns");
            final var resultSet = statement.executeQuery();

            return convertToParsingFormat(resultSet);
        } catch (SQLException e) {
            throw new DBException("조회 실패", e);
        }
    }

    public void save(TurnDto turnDto) {
        try (final var connection = dbConnector.getConnection()) {
            final var statement = connection.prepareStatement("INSERT INTO turns VALUES (?)");
            statement.setString(1, turnDto.team().name());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DBException("저장 실패", e);
        }
    }

    public void update(TurnDto turnDto) {
        try (final var connection = dbConnector.getConnection()) {
            final var statement = connection.prepareStatement(
                    "UPDATE turns SET current_team = ? WHERE current_team = ?");
            statement.setString(1, turnDto.team().name());
            statement.setString(2, turnDto.team().toggleTeam().name());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DBException("업데이트 실패", e);
        }
    }

    public void delete() {
        try (final var connection = dbConnector.getConnection()) {
            final var statement = connection.prepareStatement("DELETE FROM turns");
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DBException("삭제 실패", e);
        }
    }

    private static Optional<TurnDto> convertToParsingFormat(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            var team = Team.convertToTeam(resultSet.getString("current_team"));
            TurnDto turns = new TurnDto(team);
            return Optional.of(turns);
        }
        return Optional.empty();
    }
}
