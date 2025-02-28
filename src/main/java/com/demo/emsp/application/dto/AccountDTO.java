package com.demo.emsp.application.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AccountDTO {
    private String id;
    private String serviceId;
    private String fleetSolution;
    private String accountStatus;
    private LocalDateTime createdDate;
    private LocalDateTime lastUpdated;
    private List<TokenDTO> tokens;

}
