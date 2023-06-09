package hoy.project.service;

import hoy.project.domain.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountServiceImplTest extends ServiceTest {

    @Test
    @DisplayName("회원 생성 성공 테스트")
    void createAccountTest() {
        Account account = accountService.createAccount(Account.createAccount("test123", "test123", "test@test.org"));

        Account findAccount = em.find(Account.class, account.getId());

        assertThat(account.getId()).isEqualTo(findAccount.getId());
    }

    @Test
    @DisplayName("로그인 성공 테 스트")
    void loginTest() {
        Account saveAccount = accountRepository.save(new Account("test", "1234", "test@test.com"));
        Account loginAccount = accountService.login("test", "1234");

        assertThat(loginAccount.getId()).isEqualTo(saveAccount.getId());
    }

    @Test
    @DisplayName("로그인 실패 틀린 아이디 테스트")
    void loginWrongLoginIdTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            accountService.login("wrongid", "0000");
        });
    }

    @Test
    @DisplayName("로그인 실패 틀린 비밀번호 테스트")
    void loginWrongLoginPasswordTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            accountService.login("test", "0000");
        });
    }


}