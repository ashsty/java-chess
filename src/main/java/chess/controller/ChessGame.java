package chess.controller;

import chess.domain.board.ChessBoard;
import chess.domain.board.ScoreBoard;
import chess.domain.piece.Team;
import chess.domain.state.GameState;
import chess.domain.state.Progress;
import chess.domain.state.Ready;
import chess.dto.ChessBoardDto;
import chess.view.InputView;
import chess.view.OutputView;

import java.util.List;
import java.util.function.Supplier;

public class ChessGame {
    private static final ScoreBoard scoreBoard = new ScoreBoard();
    private final InputView inputView = new InputView();
    private final OutputView outputView = new OutputView();

    public void run() {
        ChessBoard chessBoard = generateChessBoard();
        GameState gameState = new Ready(chessBoard);
        outputView.printStartMessage();

        Team winner = playGame(gameState, chessBoard);
        outputView.printResultMessage(winner);
    }

    private ChessBoard generateChessBoard() {
        ChessBoard chessBoard = new ChessBoard();
        chessBoard.initialBoard();
        return chessBoard;
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

        if (command.get(0).equals("status") && gameState.getClass().equals(Progress.class)) {
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
