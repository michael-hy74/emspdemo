package com.demo.emsp.application.dto;

import com.demo.emsp.domain.annotation.ValidAccountStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AccountDTO {
    private String id;

    @NotBlank(message = "serviceId cannot be blank")
    private String serviceId;

    @NotBlank(message = "fleetSolution cannot be blank")
    private String fleetSolution;

    private String contractId;

    @ValidAccountStatus(message = "Invalid account status")
    private String accountStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdated;

    private List<TokenDTO> tokens;

}
