package chess.domain.board;

import chess.domain.piece.*;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import chess.dto.ChessBoardDto;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ChessBoard {
    private static final Piece EMPTY = new Empty();
    private final Map<Position, Piece> chessBoard;

    public ChessBoard(Map<Position, Piece> defaultChessBoard) {
        chessBoard = defaultChessBoard;
    }

    public ChessBoard() {
        chessBoard = new LinkedHashMap<>();
    }

    public void initialBoard() {
        initializeBlackPieces();
        initializeWhitePieces();
    }

    public ChessBoardDto convertToDto() {
        return new ChessBoardDto(chessBoard);
    }

    public Map<Position, Type> findRemainingPieces(Team team) {
        return chessBoard.entrySet().stream()
                .filter(entry -> entry.getValue().isSameTeam(team))
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().identifyType()));
    }

    public Team winByAttackingKing(Position target) {
        Team winningTeam = Team.NONE;

        if (chessBoard.containsKey(target) && chessBoard.get(target).identifyType() == Type.KING) {
            return confirmKingColor(chessBoard.get(target));
        }
        return winningTeam;
    }

    private Team confirmKingColor(Piece king) {
        if (king.isBlack()) {
            return Team.toggleTeam(Team.BLACK);
        }
        return Team.toggleTeam(Team.WHITE);
    }

    public void move(Position source, Position target) {
        if (!canMove(source, target)) {
            throw new IllegalArgumentException("올바르지 않은 이동입니다.");
        }

        Piece sourcePiece = chessBoard.get(source);
        chessBoard.put(target, sourcePiece);
        chessBoard.remove(source);
    }

    private boolean canMove(Position source, Position target) {
        if (!chessBoard.containsKey(source)) {
            throw new IllegalArgumentException("이동할 수 있는 말이 없습니다.");
        }

        Piece sourcePiece = chessBoard.get(source);
        Piece targetPiece = EMPTY;

        if (chessBoard.containsKey(target)) {
            targetPiece = chessBoard.get(target);
        }

        if (sourcePiece.canMove(source, target, targetPiece)) {
            return sourcePiece.searchPath(source, target).stream().noneMatch(chessBoard::containsKey);
        }
        return false;
    }

    private void initializeBlackPieces() {
        initializeEdgeRank(Rank.EIGHT, Team.BLACK);
        initializePawnRank(Rank.SEVEN, Team.BLACK);
    }

    private void initializeWhitePieces() {
        initializePawnRank(Rank.TWO, Team.WHITE);
        initializeEdgeRank(Rank.ONE, Team.WHITE);
    }

    private void initializePawnRank(Rank rank, Team team) {
        for (File file : File.values()) {
            chessBoard.put(Position.of(file, rank), new Pawn(team));
        }
    }

    private void initializeEdgeRank(Rank rank, Team team) {
        chessBoard.put(Position.of(File.A, rank), new Rook(team));
        chessBoard.put(Position.of(File.B, rank), new Knight(team));
        chessBoard.put(Position.of(File.C, rank), new Bishop(team));
        chessBoard.put(Position.of(File.D, rank), new Queen(team));
        chessBoard.put(Position.of(File.E, rank), new King(team));
        chessBoard.put(Position.of(File.F, rank), new Bishop(team));
        chessBoard.put(Position.of(File.G, rank), new Knight(team));
        chessBoard.put(Position.of(File.H, rank), new Rook(team));
    }
}
