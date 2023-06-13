package hoy.project.service;

import hoy.project.domain.Account;
import hoy.project.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {

    private final AccountRepository AccountRepository;

    @Transactional
    @Override
    public Account createAccount(Account Account) {
        return AccountRepository.save(Account);
    }

    @Override
    public Account login(String userId, String password) {
        Account findAccount = AccountRepository.findByUserId(userId);

        if (findAccount == null) {
            throw new IllegalArgumentException("존재하지 않는 아이디 입니다.");
        }

        if (!checkPassword(findAccount.getPassword(), password)) {
            throw new IllegalArgumentException("비밀번호를 확인해주세요.");
        }

        return findAccount;
    }

    @Override
    public boolean checkUserIdExist(String userId) {
        return AccountRepository.existByUserId(userId);
    }


    private boolean checkPassword(String loginPassword, String password) {
        if (!loginPassword.equals(password)) {
            return false;
        }
        return true;
    }


}
