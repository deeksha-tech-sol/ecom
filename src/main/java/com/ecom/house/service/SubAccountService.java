package com.ecom.house.service;

import com.ecom.house.dao.SubAccountRepository;
import com.ecom.house.entity.Account;
import com.ecom.house.entity.SubAccount;
import com.ecom.house.entity.SubAccountStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SubAccountService {

    @Autowired
    AccountService accountService;

    @Autowired
    SubAccountRepository subAccountRepository;

    public ResponseEntity<List<SubAccount>> createSubAccount(List<Long> subAccountIds, Long businessAccountId) {
        Optional<Account> businessAccount = accountService.findAccountByAccountId(businessAccountId);
        List<SubAccount> subAccountList = new ArrayList<>();
        //You can send invite only if business account
        if (businessAccount.isPresent() && businessAccount.get().getIsBusinessAccount()) {
            subAccountIds.forEach(subAccountId -> {
                Optional<Account> account = accountService.findAccountByAccountId(subAccountId);
                Optional<SubAccount> subAccountOptional = subAccountRepository.findById(subAccountId);
                if (account.isPresent() && (subAccountOptional.isEmpty() || subAccountOptional.get().getStatus().equals(SubAccountStatus.INACTIVE))) {
                    //Send Email Invite and then save the status
                    SubAccount subAccount = new SubAccount();
                    subAccount.setSubAccountId(subAccountId);
                    subAccount.setBusinessOwnerAccountId(businessAccountId);
                    subAccount.setStatus(SubAccountStatus.INVITE_SENT);
                    subAccount.setLastModifiedDate(new Date());
                    subAccountList.add(subAccountRepository.save(subAccount));
                }
            });
            return new ResponseEntity<>(subAccountList, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<SubAccount>> getSubAccounts(Long businessAccountId) {
        List<SubAccount> subAccountList = subAccountRepository.findAllByBusinessOwnerAccountId(businessAccountId);
        if(subAccountList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(subAccountList, HttpStatus.OK);
    }

    public ResponseEntity<String> acceptInvite(Long businessAccountId, Long subAccountId) {
        Optional<SubAccount> subAccountOptional = subAccountRepository.findById(subAccountId);
        if (subAccountOptional.isPresent()) {
            SubAccount subAccount = subAccountOptional.get();
            if (subAccount.getBusinessOwnerAccountId().equals(businessAccountId) &&
                    subAccount.getStatus().name().equalsIgnoreCase(SubAccountStatus.INVITE_SENT.name())) {
                subAccount.setStatus(SubAccountStatus.ACTIVE);
                subAccount.setLastModifiedDate(new Date());
                subAccountRepository.save(subAccount);
                return new ResponseEntity<>("Account is successfully linked to business account.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No active invitation found.", HttpStatus.BAD_REQUEST);
            }
        }else {
            return new ResponseEntity<>("No SubAccount found.", HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity<String> unlinkAccount(Long businessAccountId, Long subAccountId) {
        Optional<SubAccount> subAccountOptional = subAccountRepository.findById(subAccountId);
        if (subAccountOptional.isPresent()) {
            SubAccount subAccount = subAccountOptional.get();
            if (subAccount.getBusinessOwnerAccountId().equals(businessAccountId) &&
                    subAccount.getStatus().name().equalsIgnoreCase(SubAccountStatus.ACTIVE.name())) {
                subAccount.setStatus(SubAccountStatus.INACTIVE);
                subAccount.setLastModifiedDate(new Date());
                subAccountRepository.save(subAccount);
                return new ResponseEntity<>("Account is unlinked from business account.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No active account linked with business.", HttpStatus.BAD_REQUEST);
            }
        }else {
            return new ResponseEntity<>("No SubAccount mapping found.", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> declineInvite(Long businessAccountId, Long subAccountId) {
        Optional<SubAccount> subAccountOptional = subAccountRepository.findById(subAccountId);
        if (subAccountOptional.isPresent()) {
            SubAccount subAccount = subAccountOptional.get();
            if (subAccount.getBusinessOwnerAccountId().equals(businessAccountId) &&
                    subAccount.getStatus().name().equalsIgnoreCase(SubAccountStatus.INVITE_SENT.name())) {
                subAccount.setStatus(SubAccountStatus.INVITE_DECLINED);
                subAccount.setLastModifiedDate(new Date());
                subAccountRepository.save(subAccount);
                return new ResponseEntity<>("Invite declined from business account.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No invite found.", HttpStatus.BAD_REQUEST);
            }
        }else {
            return new ResponseEntity<>("No SubAccount mapping found.", HttpStatus.BAD_REQUEST);
        }
    }
}
