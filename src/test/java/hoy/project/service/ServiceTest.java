package hoy.project.service;

import hoy.project.domain.Account;
import hoy.project.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
public class ServiceTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    EntityManager em;

    Account testAccount;

    @BeforeEach
    void setup() {
        testAccount = accountRepository.save(new Account("testid1", "!testpassword1", "test@gmail.com"));
    }


}
