package kr.co.goldenhome;

import org.springframework.stereotype.Component;

@Component
public class ElderlyFacilityApiTestImpl implements ElderlyFacilityApi {
    @Override
    public boolean existsById(Long id) {
        return false;
    }
}
