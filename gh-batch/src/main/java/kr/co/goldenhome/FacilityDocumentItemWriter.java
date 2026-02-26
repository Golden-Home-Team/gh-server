package kr.co.goldenhome;

import kr.co.goldenhome.entity.FacilityDocument;
//import kr.co.goldenhome.repository.FacilityDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FacilityDocumentItemWriter implements ItemWriter<FacilityDocument> {

//    private final FacilityDocumentRepository facilityDocumentRepository;

    @Override
    public void write(Chunk<? extends FacilityDocument> chunk) throws Exception {
//        facilityDocumentRepository.saveAll(chunk.getItems());
    }
}
