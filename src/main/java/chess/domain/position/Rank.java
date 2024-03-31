package chess.domain.position;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public enum Rank {
    EIGHT(8),
    SEVEN(7),
    SIX(6),
    FIVE(5),
    FOUR(4),
    THREE(3),
    TWO(2),
    ONE(1);

    private static final Map<Integer, Rank> CACHE;

    static {
        CACHE = new HashMap<>();
        for (Rank rank : Rank.values()) {
            CACHE.put(rank.rankRow, rank);
        }
    }

    final int rankRow;

    Rank(int rankRow) {
        this.rankRow = rankRow;
    }

    public static Rank convertToRank(String rankSymbol) {
        return Arrays.stream(Rank.values())
                .filter(rank -> rank.name().equals(rankSymbol))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("일치하는 Rank 값이 없습니다."));
    }

    public int calculateDifference(Rank otherRank) {
        return otherRank.rankRow - this.rankRow;
    }

    public Rank move(int moveUnit) {
        int destination = rankRow + moveUnit;
        return CACHE.get(destination);
    }
}
