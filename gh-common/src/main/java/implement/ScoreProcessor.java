package implement;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ScoreProcessor {

    public static int process(Integer score) {
        if (score == null || score > 5) return 5;
        else if (score < 0) return 1;
        else return score;
    }
}
