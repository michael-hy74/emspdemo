package com.demo.emsp.application.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TokenDTO {
    private String id;
    private String tokenType;
    private String tokenStatus;
    private String contractId;
    private String accountId;
    private LocalDateTime createdDate;
    private LocalDateTime assignedDate;
    private LocalDateTime lastUpdated;
}
