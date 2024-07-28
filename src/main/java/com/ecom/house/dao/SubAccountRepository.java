package com.ecom.house.dao;

import com.ecom.house.entity.SubAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubAccountRepository extends JpaRepository<SubAccount, Long> {

    List<SubAccount> findAllByBusinessOwnerAccountId(Long businessAccountId);

    Optional<SubAccount> findByBusinessOwnerAccountIdAndSubAccountId(Long businessAccountId, Long subAccountId);
}
