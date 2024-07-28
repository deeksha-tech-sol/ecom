package com.ecom.house.controller;

import com.ecom.house.service.AccountService;
import com.ecom.house.entity.Account;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    @Operation(summary = "Create an account", description = "Creates a new account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created successfully"),
            @ApiResponse(responseCode = "400", description = "One of the account parameters is null or empty"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        return accountService.createAccount(account);
    }

    @GetMapping
    @Operation(summary = "Get all accounts", description = "Returns list of all the accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @Operation(summary = "Upgrade to business account", description = "Upgrades to business account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "No account found for given accountId"),
            @ApiResponse(responseCode = "400", description = "Requirement not met for upgrade"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{accountId}/upgrade")
    public ResponseEntity<String> upgradeAccount(@PathVariable Long accountId) {
        return accountService.upgradeAccount(accountId);
    }

}
