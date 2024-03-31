package chess.domain.piece;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum Team {
    BLACK,
    WHITE,
    NONE;

    public static Team toggleTeam(Team team) {
        if (team == BLACK) {
            return WHITE;
        }
        return BLACK;
    }

    public static Team convertToTeam(String teamSymbol) {
        return Arrays.stream(Team.values())
                .filter(team -> team.name().equals(teamSymbol))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("일치하는 Team 값이 없습니다."));
    }
}
