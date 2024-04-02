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

    public void run() {
        outputView.printStartMessage();

        GameState gameState = new Ready();
        Team winner = playGame(gameState);

        outputView.printResultMessage(winner);
    }

    private Team playGame(GameState gameState) {
        while (!gameState.isEnd()) {
            GameState currentGameState = gameState;
            gameState = repeatUntilSuccess(() -> playEachTurn(currentGameState));

            printChessBoardInProgress(gameState);
        }
        return gameState.findWinner();
    }

    private GameState playEachTurn(GameState gameState) {

        List<String> command = inputView.readCommand();
        if (STATUS_COMMAND.equals(command.get(0)) && gameState instanceof Progress) {
            printChessStatus(gameState.getChessBoard());
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


    private void printChessBoardInProgress(GameState gameState) {
        ChessBoardDto chessBoardDto;
        if (!gameState.isEnd()) {
            chessBoardDto = gameState.getChessBoard().convertToDto();
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
