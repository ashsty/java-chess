package chess.domain.board;

import chess.domain.piece.Team;
import chess.domain.piece.Type;
import chess.domain.position.File;
import chess.domain.position.Position;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ScoreBoard {

    public static final int PAWN_HALF = 2;

    public ScoreBoard() {
    }

    public Team findWinner(double blackScore, double whiteScore) {
        if (blackScore > whiteScore) {
            return Team.BLACK;
        }
        if (blackScore < whiteScore) {
            return Team.WHITE;
        }
        return Team.NONE;
    }

    public double calculateScore(ChessBoard chessBoard, Team team) {
        Map<Position, Type> remaining = chessBoard.findRemainingPieces(team);
        double score = 0;

        for (Position position : remaining.keySet()) {
            score += Type.calculateScore(remaining.get(position));
        }

        double count = findSameFilePawn(remaining);
        return score - count / PAWN_HALF;
    }

    private double findSameFilePawn(Map<Position, Type> remaining) {
        Map<File, Integer> countPawn = new HashMap<>();

        remaining.forEach((position, type) -> Arrays.stream(File.values())
                .filter(file -> position.isFile(file) && type == Type.PAWN)
                .forEach(file -> countPawn.put(file, countPawn.getOrDefault(file, 0) + 1)));

        return countPawn.values().stream()
                .filter(count -> count > 1)
                .mapToDouble(v -> v)
                .sum();
    }
}
