package kr.co.goldenhome.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserFcmTokenCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;



}
