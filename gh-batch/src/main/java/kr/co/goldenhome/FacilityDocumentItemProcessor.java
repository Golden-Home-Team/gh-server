package kr.co.goldenhome;

import kr.co.goldenhome.entity.Facility;
import kr.co.goldenhome.entity.FacilityDocument;
import kr.co.goldenhome.entity.FacilityPhoto;
import kr.co.goldenhome.repository.FacilityPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FacilityDocumentItemProcessor implements ItemProcessor<Facility, FacilityDocument> {

    private final FacilityPhotoRepository facilityPhotoRepository;

    @Override
    public FacilityDocument process(Facility facility) throws Exception {
        FacilityDocument document = FacilityDocument.from(facility);
        List<FacilityPhoto> photos = facilityPhotoRepository.findByInstitutionSymbol(facility.getInstitutionSymbol());
        List<String> imageUrls = photos.stream()
                .map(FacilityPhoto::getImageUrl) // 각 FacilityPhoto 객체에서 imageUrl만 추출
                .toList();
        document.setImageUrls(imageUrls);
        return document;
    }
}
