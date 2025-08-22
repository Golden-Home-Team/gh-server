package kr.co.goldenhome;

import kr.co.goldenhome.entity.FacilityProfile;
import kr.co.goldenhome.repository.FacilityProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FacilityProfileApiImpl implements FacilityProfileApi {

    private final FacilityProfileRepository facilityProfileRepository;
    @Value("${aws.s3.base-url}")
    private String awsBaseUrl;

    @Override
    public void save(Long facilityId, String formattedImageName) {
        facilityProfileRepository.save(FacilityProfile.create(facilityId, awsBaseUrl + formattedImageName));
    }

    @Override
    public String get(Long facilityId) {
        Optional<FacilityProfile> optionalFacilityProfile = facilityProfileRepository.findById(facilityId);
        return optionalFacilityProfile.map(FacilityProfile::getImageUrl).orElse(null);
    }
}
