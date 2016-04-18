package com.shop.springframework.domain.account;

import org.springframework.data.repository.Repository;

public interface AccountRepository extends Repository<Account,Long> {

    Account save(Account account);

    Account findByEmail(String email);
}
