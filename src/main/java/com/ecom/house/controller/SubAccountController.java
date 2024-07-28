package com.ecom.house.controller;

import com.ecom.house.entity.SubAccount;
import com.ecom.house.service.SubAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class SubAccountController {

    @Autowired
    private SubAccountService subAccountService;

    @PostMapping("/{businessAccountId}/invite/send")
    @Operation(summary = "Create subaccounts", description = "Sends invite to create subaccounts for given business account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Invite sent to Sub accounts successfully"),
            @ApiResponse(responseCode = "400", description = "No such business account"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<SubAccount>> createSubAccounts(@RequestBody List<Long> subAccountIds, @PathVariable Long businessAccountId) {
        return subAccountService.createSubAccount(subAccountIds, businessAccountId);
    }

    @GetMapping("/{businessAccountId}")
    @Operation(summary = "Get list of subaccounts", description = "Returns list of all subaccounts associated with given business account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<SubAccount>> getSubAccounts(@PathVariable Long businessAccountId) {
        return subAccountService.getSubAccounts(businessAccountId);
    }

    @PutMapping("/{businessAccountId}/{subAccountId}/invite/accept")
    @Operation(summary = "Accept the invite", description = "Subaccount accepts the invite sent by given business account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invite accepted successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> acceptInvite(@PathVariable Long businessAccountId, @PathVariable Long subAccountId){
        return subAccountService.acceptInvite(businessAccountId, subAccountId);
    }

    @PutMapping("/{businessAccountId}/{subAccountId}/invite/decline")
    @Operation(summary = "Decline the invite", description = "Sub account declines given business account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account mapping decline successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> declineInvite(@PathVariable Long businessAccountId, @PathVariable Long subAccountId){
        return subAccountService.declineInvite(businessAccountId, subAccountId);
    }

    @PutMapping("/{businessAccountId}/{subAccountId}/unlink")
    @Operation(summary = "Unlink the account", description = "Sub account unlinks from given business account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account Unlinked successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> unlinkAccount(@PathVariable Long businessAccountId, @PathVariable Long subAccountId){
        return subAccountService.unlinkAccount(businessAccountId, subAccountId);
    }
}
