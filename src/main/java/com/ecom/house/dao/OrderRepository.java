package com.ecom.house.dao;

import com.ecom.house.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByAccountId(Long accountId);

    @Query(
            value = "SELECT count(*) FROM Order o WHERE o.orderDate >= :oneYearAgo AND o.accountId =:accountId")
    int findCountOfOrdersLastYear(Long accountId, Date oneYearAgo);

    List<Order> findAllByAccountIdAndOrderDateGreaterThan(Long accountId, Date orderDate);
}
