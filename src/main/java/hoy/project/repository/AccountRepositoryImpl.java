package hoy.project.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import static hoy.project.domain.QAccount.account;


public class AccountRepositoryImpl implements AccountRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public AccountRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public boolean existByUserId(String userId) {
        return queryFactory.selectFrom(account)
                .where(account.userId.eq(userId))
                .fetchOne() != null;
    }

}
