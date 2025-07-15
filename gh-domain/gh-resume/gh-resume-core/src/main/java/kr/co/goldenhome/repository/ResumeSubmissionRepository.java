package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.ResumeSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResumeSubmissionRepository extends JpaRepository<ResumeSubmission, Long> {

    @Query(
            value = "select * " +
                    "from resume_submissions " +
                    "where resume_submissions.user_id = :userId " +
                    "limit :limit",
            nativeQuery = true
    )
    List<ResumeSubmission> findAllInfiniteScroll(@Param("userId") Long userId, @Param("limit") Long limit);

    @Query(
            value = "select * " +
                    "from resume_submissions " +
                    "where resume_submissions.user_id = :userId and resume_submissions.id > :lastId " +
                    "limit :limit",
            nativeQuery = true
    )
    List<ResumeSubmission> findAllInfiniteScroll(@Param("userId") Long userId, @Param("lastId") Long lastId, @Param("limit") Long pageSize);
}
