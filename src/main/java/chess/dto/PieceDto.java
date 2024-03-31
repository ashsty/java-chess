package chess.dto;

import chess.domain.piece.Team;
import chess.domain.piece.Type;
import chess.domain.position.File;
import chess.domain.position.Rank;

public record PieceDto(File file, Rank rank, Team team, Type type) {
}
