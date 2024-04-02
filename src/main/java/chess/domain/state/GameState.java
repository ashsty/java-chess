package chess.domain.state;

import chess.domain.board.ChessBoard;
import chess.domain.piece.Team;

import java.util.List;

public interface GameState {
    Team findWinner();
    GameState play(List<String> inputCommand);

    boolean isEnd();
    ChessBoard getChessBoard();
}
