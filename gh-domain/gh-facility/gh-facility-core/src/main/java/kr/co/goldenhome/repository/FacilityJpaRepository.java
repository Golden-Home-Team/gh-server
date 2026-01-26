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
                    "WHERE MATCH(name, address) AGAINST(:keyword IN NATURAL LANGUAGE MODE)",
            nativeQuery = true
    )
    List<Facility> searchByFullTextFallback(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM facilities f " +
            "WHERE REPLACE(f.name, ' ', '') LIKE CONCAT('%', :keyword, '%') " +
            "OR REPLACE(f.address, ' ', '') LIKE CONCAT('%', :keyword, '%')",
            nativeQuery = true)
    List<Facility> searchByLikeFallback(@Param("keyword") String keyword, Pageable pageable);
}
