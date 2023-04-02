package hoy.project.service;

import hoy.project.domain.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class AccountServiceImplTest {


    @Autowired
    AccountService AccountService;

    @Autowired
    EntityManager em;


    @BeforeEach
    void init() {
        em.persist(Account.createAccount("test", "1234", "test@test.org"));
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("회원 생성 성공 테스트")
    void createAccountTest() {
        Account account = AccountService.createAccount(Account.createAccount("test123", "test123", "test@test.org"));

        Account findAccount = em.find(Account.class, account.getId());

        assertThat(account.getId()).isEqualTo(findAccount.getId());
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void loginTest() {
        Account loginAccount = AccountService.login("test", "1234");
        Account findAccount = em.createQuery("select m from Account m where m.userId = 'test'", Account.class).getSingleResult();

        assertThat(loginAccount.getId()).isEqualTo(findAccount.getId());
    }

    @Test
    @DisplayName("로그인 실패 틀린 아이디 테스트")
    void loginWrongLoginIdTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            AccountService.login("wrongid", "0000");
        });
    }

    @Test
    @DisplayName("로그인 실패 틀린 비밀번호 테스트")
    void loginWrongLoginPasswordTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            AccountService.login("test", "0000");
        });
    }


}