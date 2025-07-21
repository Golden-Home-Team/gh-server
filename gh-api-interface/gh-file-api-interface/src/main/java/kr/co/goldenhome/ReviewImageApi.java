package kr.co.goldenhome;

import java.util.List;

public interface ReviewImageApi {
    void saveAll(Long reviewId, List<String> formattedImageNames);
}
