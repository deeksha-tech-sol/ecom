package com.ecom.house.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class SubAccount {

    @Id
    private Long subAccountId;
    private Long businessOwnerAccountId;
    private SubAccountStatus status;
    private Date lastModifiedDate;
}
