package hoy.project.service;

import hoy.project.domain.Account;

public interface AccountService {

    Account createAccount(Account Account);

    Account login(String userId,String password);

    boolean checkUserIdExist(String loginId);

}
