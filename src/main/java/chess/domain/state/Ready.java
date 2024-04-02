package chess.domain.state;

import chess.dao.PiecesDao;
import chess.dao.TurnsDao;
import chess.db.DBConnector;
import chess.domain.board.ChessBoard;
import chess.domain.piece.Team;
import chess.service.ChessService;

import java.util.List;

public class Ready implements GameState {
    private static final String START_COMMAND = "start";
    private static final String MOVE_COMMAND = "move";
    private static final String END_COMMAND = "end";

    private ChessBoard chessBoard = new ChessBoard();
    private final ChessService chessService = new ChessService(
            new PiecesDao(DBConnector.getProductionDB()), new TurnsDao(DBConnector.getProductionDB()));;
    private Team team = Team.WHITE;

    public Ready(){
        initializeGameSetting();
    }

    @Override
    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    @Override
    public Team findWinner() {
        return Team.NONE;
    }
    @Override
    public GameState play(List<String> inputCommand) {
        String command = inputCommand.get(0);
        if (command.equals(START_COMMAND)) {
            return new Progress(chessBoard, chessService, team);
        }
        if (command.equals(MOVE_COMMAND)) {
            throw new UnsupportedOperationException("게임이 시작되지 않았습니다.");
        }
        if (command.equals(END_COMMAND)) {
            throw new UnsupportedOperationException("게임이 시작되지 않았습니다.");
        }
        throw new IllegalArgumentException("올바르지 않은 command입니다.");
    }

    @Override
    public boolean isEnd() {
        return false;
    }

    private void initializeGameSetting() {
        if (chessService.hasNoLastGame()) {
            chessBoard.initialBoard();
            chessService.saveChessBoard(chessBoard);
            chessService.saveTurn(Team.WHITE);
            return;
        }
        chessBoard = chessService.loadChessBoard();
        team = chessService.loadTurn();
    }
}
