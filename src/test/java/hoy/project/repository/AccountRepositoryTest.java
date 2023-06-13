package hoy.project.repository;

import hoy.project.domain.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AccountRepositoryTest {

    @Autowired
    AccountRepository AccountRepository;

    @Autowired
    EntityManager em;




    @Test
    @DisplayName("Account 생성 테스트")
    @Transactional
    void createAccount() {

        Account AccountA = Account.builder()
                .userId("account1")
                .password("1234")
                .email("account@naver.com")
                .build();

        AccountRepository.save(AccountA);
        em.flush();
        em.clear();

        Account findAccount = AccountRepository.findByUserId("account1");

        assertThat(AccountA.getId()).isEqualTo(findAccount.getId());
    }

    @Test
    @DisplayName("중복 확인 존재하지않는 아이디 성공 테스트")
    @Transactional
    void existLoginId() {
        Account accountA = Account.builder()
                .userId("account1")
                .password("1234")
                .email("account@naver.com")
                .build();

        AccountRepository.save(accountA);
        em.flush();
        em.clear();

        boolean exist = AccountRepository.existByUserId("account1");
        assertThat(exist).isTrue();
    }


}