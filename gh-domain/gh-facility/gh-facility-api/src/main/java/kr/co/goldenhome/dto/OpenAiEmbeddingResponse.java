package kr.co.goldenhome.dto;

import java.util.List;

public record OpenAiEmbeddingResponse(List<EmbeddingData> data) {
    public record EmbeddingData(List<Float> embedding, int index) {}
}
