package kr.co.goldenhome.dto;

import java.time.LocalDateTime;
import java.util.List;

public record SliceResponse<T>(
        List<T> content,
        boolean hasNext,
        int numberOfElements,
        LocalDateTime cursor
) { }
