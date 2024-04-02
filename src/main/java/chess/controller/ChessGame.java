package chess.controller;

import chess.dao.PiecesDao;
import chess.dao.TurnsDao;
import chess.db.DBConnector;
import chess.domain.board.ChessBoard;
import chess.domain.board.ScoreBoard;
import chess.domain.piece.Team;
import chess.domain.state.GameState;
import chess.domain.state.Progress;
import chess.domain.state.Ready;
import chess.dto.ChessBoardDto;
import chess.service.ChessService;
import chess.view.InputView;
import chess.view.OutputView;

import java.util.List;
import java.util.function.Supplier;

public class ChessGame {
    private static final String STATUS_COMMAND = "status";
    private static final ScoreBoard scoreBoard = new ScoreBoard();

    private final InputView inputView = new InputView();
    private final OutputView outputView = new OutputView();
    private final ChessService chessService = new ChessService(
            new PiecesDao(DBConnector.getProductionDB()), new TurnsDao(DBConnector.getProductionDB()));

    public void run() {
        ChessBoard chessBoard = setChessBoard();
        GameState gameState = setGameState(chessBoard);
        outputView.printStartMessage();

        Team winner = playGame(gameState, chessBoard);
        outputView.printResultMessage(winner);
    }

    private ChessBoard setChessBoard() {
        if (chessService.hasNoLastGame()) {
            ChessBoard chessBoard = new ChessBoard();
            chessBoard.initialBoard();
            chessService.saveChessBoard(chessBoard);
            return chessBoard;
        }
        return chessService.loadChessBoard();
    }

    private GameState setGameState(ChessBoard chessBoard) {
        if (chessService.hasNoLastGame()) {
            chessService.saveTurn(Team.WHITE);
            return new Ready(chessBoard, chessService);
        }
        return new Ready(chessBoard, chessService, chessService.loadTurn());
    }

    private Team playGame(GameState gameState, ChessBoard chessBoard) {
        while (!gameState.isEnd()) {
            GameState currentGameState = gameState;
            gameState = repeatUntilSuccess(() -> playEachTurn(currentGameState, chessBoard));

            printChessBoardInProgress(gameState, chessBoard);
        }
        return gameState.findWinner();
    }

    private GameState playEachTurn(GameState gameState, ChessBoard chessBoard) {
        List<String> command = inputView.readCommand();

        if (STATUS_COMMAND.equals(command.get(0)) && gameState.getClass().isInstance(Progress.class)) {
            printChessStatus(chessBoard);
            return gameState;
        }
        gameState = gameState.play(command);
        return gameState;
    }

    private void printChessStatus(ChessBoard chessBoard) {
        double blackScore = scoreBoard.calculateScore(chessBoard, Team.BLACK);
        double whiteScore = scoreBoard.calculateScore(chessBoard, Team.WHITE);
        Team winner = scoreBoard.findWinner(blackScore, whiteScore);

        outputView.printChessStatus(blackScore, whiteScore, winner);
    }


    private void printChessBoardInProgress(GameState gameState, ChessBoard chessBoard) {
        ChessBoardDto chessBoardDto;
        if (!gameState.isEnd()) {
            chessBoardDto = chessBoard.convertToDto();
            outputView.printChessBoard(chessBoardDto);
        }
    }

    private <T> T repeatUntilSuccess(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (IllegalArgumentException | UnsupportedOperationException e) {
            outputView.printErrorMessage(e.getMessage());
            return repeatUntilSuccess(supplier);
        }
    }
}
