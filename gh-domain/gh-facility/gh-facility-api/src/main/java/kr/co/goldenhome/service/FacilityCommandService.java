package kr.co.goldenhome.service;

import kr.co.goldenhome.FacilityProfileApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FacilityCommandService {

    private final FacilityProfileApi facilityProfileApi;

    public void uploadProfile(Long facilityId, String formattedImageName) {
        facilityProfileApi.save(facilityId, formattedImageName);
    }
}
