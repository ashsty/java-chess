package chess.domain.board;

import chess.domain.piece.*;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static chess.domain.piece.Team.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ScoreBoardTest {
    /*.KR.....
    P.PB....
    .P..Q...
    ........
    .....nq.
    .....p.p
    .....pp.
    ....rk..*/

    Map<Position, Piece> board = Map.ofEntries(
            Map.entry(Position.of(File.B, Rank.EIGHT), new King(BLACK)),
            Map.entry(Position.of(File.C, Rank.EIGHT), new Rook(BLACK)),
            Map.entry(Position.of(File.A, Rank.SEVEN), new Pawn(BLACK)),
            Map.entry(Position.of(File.C, Rank.SEVEN), new Pawn(BLACK)),
            Map.entry(Position.of(File.D, Rank.SEVEN), new Bishop(BLACK)),
            Map.entry(Position.of(File.B, Rank.SIX), new Pawn(BLACK)),
            Map.entry(Position.of(File.E, Rank.SIX), new Queen(BLACK)),
            Map.entry(Position.of(File.F, Rank.FOUR), new Knight(WHITE)),
            Map.entry(Position.of(File.G, Rank.FOUR), new Queen(WHITE)),
            Map.entry(Position.of(File.F, Rank.THREE), new Pawn(WHITE)),
            Map.entry(Position.of(File.H, Rank.THREE), new Pawn(WHITE)),
            Map.entry(Position.of(File.F, Rank.TWO), new Pawn(WHITE)),
            Map.entry(Position.of(File.G, Rank.TWO), new Pawn(WHITE)),
            Map.entry(Position.of(File.E, Rank.ONE), new Rook(WHITE)),
            Map.entry(Position.of(File.F, Rank.ONE), new King(WHITE))
    );

    @DisplayName("체스 게임의 점수를 계산한다.")
    @Test
    void calculateScore() {
        // given
        ChessBoard chessBoard = new ChessBoard(board);
        ScoreBoard scoreBoard = new ScoreBoard();

        // when
        double whiteScore = scoreBoard.calculateScore(chessBoard, WHITE);
        double blackScore = scoreBoard.calculateScore(chessBoard, BLACK);

        // then
        assertAll(
                () -> assertThat(whiteScore).isEqualTo(19.5),
                () -> assertThat(blackScore).isEqualTo(20.0)
        );
    }

    @DisplayName("Black 팀이 이기는 경우를 반환한다.")
    @Test
    void findWinner_BLACK() {
        // given
        ChessBoard chessBoard = new ChessBoard(board);
        ScoreBoard scoreBoard = new ScoreBoard();

        double blackScore = scoreBoard.calculateScore(chessBoard, BLACK);
        double whiteScore = scoreBoard.calculateScore(chessBoard, WHITE);

        // when
        Team winner = scoreBoard.findWinner(blackScore, whiteScore);

        // then
        assertThat(winner).isEqualTo(BLACK);
    }

    /*.KR.....
    P.PB....
    .P..n...
    ........
    ......q.
    .....p.p
    .....pp.
    ....rk..*/

    @DisplayName("White 팀이 이기는 경우를 반환한다.")
    @Test
    void findWinner_WHITE() {
        // given
        Map<Position, Piece> copyBoard = new HashMap<>(board);

        ChessBoard chessBoard = new ChessBoard(copyBoard);
        ScoreBoard scoreBoard = new ScoreBoard();

        chessBoard.move(Position.of(File.F, Rank.FOUR), Position.of(File.E, Rank.SIX));

        double blackScore = scoreBoard.calculateScore(chessBoard, BLACK);
        double whiteScore = scoreBoard.calculateScore(chessBoard, WHITE);

        // when
        Team winner = scoreBoard.findWinner(blackScore, whiteScore);

        // then
        assertThat(winner).isEqualTo(WHITE);
    }

    @DisplayName("무승부인 경우를 반환한다.")
    @Test
    void findWinner_NONE() {
        // given
        ChessBoard chessBoard_default = new ChessBoard();
        chessBoard_default.initialBoard();
        ScoreBoard scoreBoard = new ScoreBoard();

        double blackScore = scoreBoard.calculateScore(chessBoard_default, BLACK);
        double whiteScore = scoreBoard.calculateScore(chessBoard_default, WHITE);

        // when
        Team winner = scoreBoard.findWinner(blackScore, whiteScore);

        // then
        assertThat(winner).isEqualTo(NONE);
    }
}
