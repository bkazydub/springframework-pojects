package com.shop.springframework.domain.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

@Repository
@Transactional(readOnly = true)
public class AccountRepositoryImpl implements AccountRepository {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    @Transactional
    public Account save(Account account) {
        account.setPassword(encoder.encode(account.getPassword()));
        em.persist(account);
        return account;
    }

    @Override
    public Account findByEmail(String email) {
        try {
            return em.createNamedQuery(Account.FIND_BY_EMAIL, Account.class)
                    .setParameter("email", email).getSingleResult();
        } catch (PersistenceException e) {
            return null;
        }
    }
}
