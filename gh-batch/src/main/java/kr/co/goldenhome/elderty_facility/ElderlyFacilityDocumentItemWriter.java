package kr.co.goldenhome.elderty_facility;

import kr.co.goldenhome.entity.ElderlyFacilityDocument;
import kr.co.goldenhome.repository.ElderlyFacilityDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ElderlyFacilityDocumentItemWriter implements ItemWriter<ElderlyFacilityDocument> {

    private final ElderlyFacilityDocumentRepository documentRepository;

    @Override
    public void write(Chunk<? extends ElderlyFacilityDocument> chunk) throws Exception {
        documentRepository.saveAll(chunk.getItems());
    }
}
