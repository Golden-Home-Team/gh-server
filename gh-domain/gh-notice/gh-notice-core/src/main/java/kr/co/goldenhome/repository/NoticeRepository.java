package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query(
            value = "select * " +
                    "from notices " +
                    "order by created_at desc " +
                    "limit :limit",
            nativeQuery = true
    )
    List<Notice> findAllInfiniteScroll(@Param("limit") Long limit);

    @Query(
            value = "select * " +
                    "from notices " +
                    "where notices.id < :lastId " +
                    "order by created_at desc " +
                    "limit :limit",
            nativeQuery = true
    )
    List<Notice> findAllInfiniteScroll(@Param("limit") Long limit, @Param("lastId") Long lastId);
}
