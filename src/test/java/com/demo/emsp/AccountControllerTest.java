package com.demo.emsp;

import com.demo.emsp.application.controller.AccountController;
import com.demo.emsp.application.dto.AccountDTO;
import com.demo.emsp.application.services.AccountAppService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountAppService accountAppService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String baseUrl = "/api/accounts";

    @Test
    void createAccount_Success() throws Exception {

        AccountDTO inputDTO = new AccountDTO();
        inputDTO.setServiceId("service123");
        inputDTO.setFleetSolution("fleet456");
        inputDTO.setAccountStatus("CREATED");

        AccountDTO mockResponse = new AccountDTO();
        mockResponse.setServiceId("service123");
        mockResponse.setFleetSolution("fleet456");
        mockResponse.setAccountStatus("CREATED");

        Mockito.when(accountAppService.createAccount(inputDTO)).thenReturn(mockResponse);

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serviceId").value("service123"))
                .andExpect(jsonPath("$.fleetSolution").value("fleet456"))
                .andExpect(jsonPath("$.accountStatus").value("CREATED"));

        Mockito.verify(accountAppService).createAccount(inputDTO);
    }

}
