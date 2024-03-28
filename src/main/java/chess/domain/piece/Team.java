package chess.domain.piece;

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
}
