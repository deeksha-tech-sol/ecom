package com.ecom.house.service;

import com.ecom.house.dao.AccountRepository;
import com.ecom.house.entity.Account;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OrderService orderService;

    public ResponseEntity<Account> createAccount(Account account) {
        if(account != null && StringUtils.isNoneBlank(account.getName(), account.getEmail())){
            account.setCreatedDate(new Date());
            account.setIsBusinessAccount(false);
            return new ResponseEntity<>(accountRepository.save(account), HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> findAccountByAccountId(Long accountId) {
        return accountRepository.findById(accountId);
    }

    public ResponseEntity<String> upgradeAccount(Long accountId) {
        Optional<Account> accountOptional = findAccountByAccountId(accountId);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            if (!account.getIsBusinessAccount()) {
                // If last year orders count greater than or equal to 10, upgrade
                if (orderService.getCountOfLastYearOrders(accountId) >= 10) {
                    account.setIsBusinessAccount(true);
                    accountRepository.save(account);
                    return new ResponseEntity<>("Upgrade successful", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("There should be at least 10 orders in last year for upgrade", HttpStatus.BAD_REQUEST);
                }
            }else {
                return new ResponseEntity<>("Already a business account", HttpStatus.OK);
            }
        }else {
            return new ResponseEntity<>("No such account", HttpStatus.NOT_FOUND);
        }
    }
}
