package chess.service;

import chess.dao.PiecesDao;
import chess.domain.board.ChessBoard;
import chess.domain.piece.Piece;
import chess.domain.piece.Team;
import chess.domain.piece.Type;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import chess.dto.PieceDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChessService {
    private final PiecesDao piecesDao;

    public ChessService(PiecesDao piecesDao) {
        this.piecesDao = piecesDao;
    }

    public boolean hasNoLastGame() {
        return piecesDao.findAll().isEmpty();
    }

    public void saveEntireChessBoard(ChessBoard chessBoard) {
        Map<Position, Piece> currentChessBoard = chessBoard.getChessBoard();
        piecesDao.deleteAll();

        convertChessBoard(currentChessBoard);
    }

    public void movePiece(ChessBoard chessBoard, Position source, Position target) {
        Map<Position, Piece> currentChessBoard = chessBoard.getChessBoard();
        Piece targetPiece = currentChessBoard.get(target);

        PieceDto currentPiece =
                new PieceDto(target.getFile(), target.getRank(), targetPiece.getTeam(), targetPiece.identifyType());
        PieceDto previousPiece = new PieceDto(source.getFile(), source.getRank(), Team.NONE, Type.EMPTY);

        updatePiecePosition(currentPiece);
        piecesDao.delete(previousPiece);
    }

    public ChessBoard loadChessBoard() {
        List<PieceDto> pieces = piecesDao.findAll();

        Map<Position, Piece> chessBoardBackUp = new HashMap<>();
        for (PieceDto pieceDto : pieces) {
            Position position = Position.of(pieceDto.file(), pieceDto.rank());
            Piece piece = pieceDto.type().generatePiece(pieceDto.team());
            chessBoardBackUp.put(position, piece);
        }

        return new ChessBoard(chessBoardBackUp);
    }

    public void clearGame() {
        piecesDao.deleteAll();
    }

    private void convertChessBoard(Map<Position, Piece> currentChessBoard) {
        for (Position position : currentChessBoard.keySet()) {
            File file = position.getFile();
            Rank rank = position.getRank();
            Team team = currentChessBoard.get(position).getTeam();
            Type type = currentChessBoard.get(position).identifyType();

            PieceDto pieceDto = new PieceDto(file, rank, team, type);
            piecesDao.save(pieceDto);
        }
    }

    private void updatePiecePosition(PieceDto currentPiece) {
        if (!piecesDao.findByPosition(currentPiece).isEmpty()) {
            piecesDao.update(currentPiece);
        }

        if (piecesDao.findByPosition(currentPiece).isEmpty()) {
            piecesDao.save(currentPiece);
        }
    }
}
