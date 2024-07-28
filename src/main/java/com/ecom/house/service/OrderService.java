package com.ecom.house.service;

import com.ecom.house.dao.SubAccountRepository;
import com.ecom.house.dao.AccountRepository;
import com.ecom.house.dao.OrderRepository;
import com.ecom.house.entity.Account;
import com.ecom.house.entity.Order;
import com.ecom.house.entity.SubAccount;
import com.ecom.house.entity.SubAccountStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SubAccountRepository accountSubRepository;

    public Order addOrder(Long accountId, Order order) {
        Optional<Account> account = accountRepository.findById(accountId);
        account.ifPresent(acct -> {
            order.setAccountId(acct.getAccountId());
            order.setOrderDate(new Date());
        });
        return account.isPresent() ? orderRepository.save(order) : null;
    }

    public List<Order> getOrders(Long accountId) {
        return orderRepository.findAllByAccountId(accountId);
    }

    public int getCountOfLastYearOrders(Long accountId) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        Date oneYearAgo = calendar.getTime();
        return orderRepository.findCountOfOrdersLastYear(accountId, oneYearAgo);
    }

    public ResponseEntity<List<Order>> getOrdersForSubAccount(Long businessAccountId, Long subAccountId) {
        Optional<SubAccount> subAccountOptional = accountSubRepository.findByBusinessOwnerAccountIdAndSubAccountId(businessAccountId, subAccountId);
        if (subAccountOptional.isPresent() && subAccountOptional.get().getStatus().name().equalsIgnoreCase(SubAccountStatus.ACTIVE.name())) {
            return new ResponseEntity<>(orderRepository.findAllByAccountIdAndOrderDateGreaterThan(subAccountId, subAccountOptional.get().getLastModifiedDate()), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
