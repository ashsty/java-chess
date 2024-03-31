package chess.domain.piece;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Function;

public enum Type {
    KING(0, King::new),
    QUEEN(9, Queen::new),
    BISHOP(3, Bishop::new),
    KNIGHT(2.5, Knight::new),
    ROOK(5, Rook::new),
    PAWN(1, Pawn::new),
    EMPTY(0, Empty::new),
    ;

    private final double score;
    private final Function<Team, Piece> operator;

    Type(double score, Function<Team, Piece> operator) {
        this.score = score;
        this.operator = operator;
    }

    public Piece generatePiece(Team team) {
        return operator.apply(team);
    }

    public static double calculateScore(Type typeInput) {
        return Arrays.stream(Type.values())
                .filter(type -> type.equals(typeInput))
                .findFirst()
                .orElseThrow()
                .score;
    }

    public static Type convertToType(String typeSymbol) {
        return Arrays.stream(Type.values())
                .filter(type -> type.name().equals(typeSymbol))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("일치하는 Type 값이 없습니다."));
    }
}
