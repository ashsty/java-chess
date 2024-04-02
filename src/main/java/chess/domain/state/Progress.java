package chess.domain.state;

import chess.domain.board.ChessBoard;
import chess.domain.piece.Team;
import chess.domain.position.Position;
import chess.service.ChessService;

import java.util.List;

public class Progress implements GameState {
    private static final String START_COMMAND = "start";
    private static final String MOVE_COMMAND = "move";
    private static final String END_COMMAND = "end";

    private final ChessBoard chessBoard;
    private final ChessService chessService;
    private final Team team;

    public Progress(ChessBoard chessBoard, ChessService chessService, Team team) {
        this.chessBoard = chessBoard;
        this.chessService = chessService;
        this.team = team;
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
            throw new UnsupportedOperationException("이미 시작한 게임은 다시 시작할 수 없습니다.");
        }
        if (command.equals(MOVE_COMMAND)) {
            Position source = Position.from(inputCommand.get(1));
            Position target = Position.from(inputCommand.get(2));
            return findKingHasCaught(target, source);
        }
        if (command.equals(END_COMMAND)) {
            chessService.clearGame();
            return new End(Team.NONE);
        }
        throw new IllegalArgumentException("올바르지 않은 command입니다.");
    }

    private GameState findKingHasCaught(Position target, Position source) {
        boolean attackKing = chessBoard.winByAttackingKing(target);
        chessBoard.move(source, target, team);
        if (attackKing) {
            chessService.clearGame();
            return new End(team);
        }
        chessService.movePiece(chessBoard, source, target);
        chessService.toggleTurn(team.toggleTeam());
        return new Progress(chessBoard, chessService, team.toggleTeam());
    }

    @Override
    public boolean isEnd() {
        return false;
    }
}
