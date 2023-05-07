package hoy.project.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import static hoy.project.domain.QAccount.*;


public class AccountRepositoryImpl implements AccountRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public AccountRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    /**
     * @return Account가 존재하는 경우 true를 반환
     *         만약 존재하지 않는 경우 false를 반환
     */
    @Override
    public boolean existByUserId(String userId) {
        return queryFactory.selectFrom(account)
                .where(account.userId.eq(userId))
                .fetchOne() != null;
    }

}
