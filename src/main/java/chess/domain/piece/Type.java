package chess.domain.piece;

import java.util.Arrays;

public enum Type {
    KING(0),
    QUEEN(9),
    BISHOP(3),
    KNIGHT(2.5),
    ROOK(5),
    PAWN(1),
    EMPTY(0),
    ;

    private final double score;

    Type(double score) {
        this.score = score;
    }

    public static double calculateScore(Type typeInput) {
        return Arrays.stream(Type.values())
                .filter(type -> type.equals(typeInput))
                .findFirst()
                .orElseThrow()
                .score;
    }
}
