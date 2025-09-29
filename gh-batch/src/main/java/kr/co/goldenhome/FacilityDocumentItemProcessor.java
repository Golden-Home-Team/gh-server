package kr.co.goldenhome;

import kr.co.goldenhome.entity.Facility;
import kr.co.goldenhome.entity.FacilityDocument;
import kr.co.goldenhome.entity.FacilityGrade;
import kr.co.goldenhome.entity.FacilityPhoto;
import kr.co.goldenhome.repository.FacilityGradeRepository;
import kr.co.goldenhome.repository.FacilityPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FacilityDocumentItemProcessor implements ItemProcessor<Facility, FacilityDocument> {

    private final FacilityPhotoRepository facilityPhotoRepository;
    private final FacilityGradeRepository facilityGradeRepository;

    @Override
    public FacilityDocument process(Facility facility) throws Exception {
        FacilityDocument document = FacilityDocument.from(facility);
        List<FacilityPhoto> photos = facilityPhotoRepository.findByInstitutionSymbol(facility.getInstitutionSymbol());
        List<String> imageUrls = photos.stream()
                .map(FacilityPhoto::getImageUrl)
                .toList();
        document.setImageUrls(imageUrls);
        FacilityGrade facilityGrade = facilityGradeRepository.findTopByInstitutionSymbolOrderByEvaluationDateDesc(facility.getInstitutionSymbol());
        if (facilityGrade != null) document.setGrade(facilityGrade.getGrade());
        if (facility.getLatitude() != null && facility.getLongitude() != null) {
            document.setLocation(new GeoPoint(facility.getLatitude(), facility.getLongitude()));
        }
        return document;

    }
}
