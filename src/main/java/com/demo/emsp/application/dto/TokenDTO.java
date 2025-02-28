package com.demo.emsp.application.dto;

import com.demo.emsp.domain.annotation.ValidTokenStatus;
import com.demo.emsp.domain.annotation.ValidTokenType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class TokenDTO {

    private String id;

    @ValidTokenType(message = "Invalid token type")
    private String tokenType;

    @ValidTokenStatus(message = "Invalid token status")
    private String tokenStatus;

    @NotBlank(message = "serviceId cannot be blank")
    private String contractId;

    @NotBlank(message = "serviceId cannot be blank")
    private String accountId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime assignedDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdated;
}
