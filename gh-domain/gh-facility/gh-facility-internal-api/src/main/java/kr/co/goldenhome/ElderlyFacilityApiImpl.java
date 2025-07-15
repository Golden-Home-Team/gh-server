package kr.co.goldenhome;

import kr.co.goldenhome.repository.ElderlyFacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ElderlyFacilityApiImpl implements ElderlyFacilityApi {

    private final ElderlyFacilityRepository elderlyFacilityRepository;

    @Override
    public boolean existsById(Long id) {
        return elderlyFacilityRepository.existsById(id);
    }
}
