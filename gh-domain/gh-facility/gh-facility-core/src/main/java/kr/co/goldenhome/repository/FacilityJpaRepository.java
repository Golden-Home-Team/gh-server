package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.Facility;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FacilityJpaRepository extends JpaRepository<Facility, Long> {
    List<Facility> findByIdIn(List<Long> facilityIds);
    @Query(
            value = "SELECT * " +
                    "FROM facilities " +
                    "WHERE MATCH(name, address) AGAINST(:keyword IN BOOLEAN MODE)",
            countQuery = "SELECT count(*) " +
                         "FROM facilities " +
                         "WHERE MATCH(name, address) AGAINST(:keyword IN BOOLEAN MODE)",
            nativeQuery = true
    )
    List<Facility> searchByFullTextFallback(@Param("keyword") String keyword, Pageable pageable);
}
