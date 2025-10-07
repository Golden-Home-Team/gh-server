package kr.co.goldenhome.dto;

import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;

public record SliceResponse<T>(
        List<T> content,
        boolean hasNext,
        int size,
        int numberOfElements,
        LocalDateTime cursor
) {
    public SliceResponse(Slice<T> slice, LocalDateTime cursor) {
        this(slice.getContent(), slice.hasNext(), slice.getSize(), slice.getNumberOfElements(), cursor);
    }
}
