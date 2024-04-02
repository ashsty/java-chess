package chess.domain.piece;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum Team {
    BLACK,
    WHITE,
    NONE;

    public Team toggleTeam() {
        if (this == NONE) {
            throw new IllegalArgumentException("NONE은 플레이 팀이 아닙니다.");
        }
        if (this == BLACK) {
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
