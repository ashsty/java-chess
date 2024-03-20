package chess.domain.piece;

import static chess.domain.piece.Type.ROOK;

import chess.domain.position.Position;
import java.util.ArrayList;
import java.util.List;

public class Rook implements Piece {
    private final Color color;

    public Rook(Color color) {
        this.color = color;
    }

    @Override
    public String getType() {
        return ROOK.name();
    }

    @Override
    public boolean isBlack() {
        return color == Color.BLACK;
    }

    @Override
    public boolean canMove(Position source, Position target, Color color) {
        if (this.color == color) {
            return false;
        }
        int rankDiff = source.calculateRankDifference(target);
        int fileDiff = source.calculateFileDifference(target);
        return Math.abs(rankDiff) == 0 || Math.abs(fileDiff) == 0;
    }

    @Override
    public List<Position> searchPath(Position source, Position target) {
        int rankDiff = source.calculateRankDifference(target);
        int fileDiff = source.calculateFileDifference(target);

        if (rankDiff > 0) {
            int rankUnit = rankDiff / Math.abs(rankDiff);

            List<Position> path = new ArrayList<>();
            for (int i = rankDiff; i != 1; i--) {
                source = source.move(rankUnit, 0);
                path.add(source);
            }
            return path;
        }

        int fileUnit = fileDiff / Math.abs(fileDiff);

        List<Position> path = new ArrayList<>();
        for (int i = rankDiff; i != 1; i--) {
            source = source.move(0, fileUnit);
            path.add(source);
        }
        return path;

    }
}
