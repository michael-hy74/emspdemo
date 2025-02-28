package com.demo.emsp.infrastructure.utils;

import com.demo.emsp.application.dto.AccountDTO;
import com.demo.emsp.application.dto.TokenDTO;
import com.demo.emsp.domain.entity.Account;
import com.demo.emsp.domain.entity.Token;
import com.demo.emsp.domain.enums.AccountStatus;
import com.demo.emsp.domain.enums.TokenStatus;
import com.demo.emsp.domain.enums.TokenType;
import com.demo.emsp.domain.values.*;
import com.demo.emsp.infrastructure.po.AccountPO;
import com.demo.emsp.infrastructure.po.TokenPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ConvertUtils {

    public static Account accountDtoToDomain(AccountDTO dto) {
        Account domain = new Account();
        domain.setId(dto.getId());
        domain.setServiceId(new ServiceId(dto.getServiceId()));
        domain.setFleetSolution(new FleetSolution(dto.getFleetSolution()));
        domain.setAccountStatus(AccountStatus.fromString(dto.getAccountStatus()));
        domain.setCreatedDate(dto.getCreatedDate());
        domain.setLastUpdated(dto.getLastUpdated());
        if (null != dto.getTokens() && dto.getTokens().size() > 0) {
            List<Token> tokens = dto.getTokens().stream()
                    .map(ConvertUtils::tokenDtoToDomain)
                    .collect(Collectors.toList());
            domain.setTokens(tokens);
        }
        return domain;
    }

    public static AccountDTO accountDomainToDto(Account domain) {
        AccountDTO dto = new AccountDTO();
        dto.setId(domain.getId());
        dto.setServiceId(domain.getServiceId().getValue());
        dto.setFleetSolution(domain.getFleetSolution().getValue());
        dto.setAccountStatus(domain.getAccountStatus().toString());
        dto.setCreatedDate(domain.getCreatedDate());
        dto.setLastUpdated(domain.getLastUpdated());
        if (null != domain.getTokens() && domain.getTokens().size() > 0) {
            List<TokenDTO> tokenDTOS = domain.getTokens().stream()
                    .map(ConvertUtils::tokenDomainToDto)
                    .collect(Collectors.toList());
            dto.setTokens(tokenDTOS);
        }
        return dto;
    }

    public static AccountPO accountDomainToPo(Account domain) {
        AccountPO po = new AccountPO();
        po.setId(domain.getId());
        po.setServiceId(domain.getServiceId().getValue());
        po.setFleetSolution(domain.getFleetSolution().getValue());
        po.setStatus(domain.getAccountStatus().toString());
        po.setCreatedDate(domain.getCreatedDate());
        po.setLastUpdated(domain.getLastUpdated());
        return po;
    }

    public static Account accountPOToDomain(AccountPO po) {
        Account domain = new Account();
        domain.setId(po.getId());
        domain.setServiceId(new ServiceId(po.getServiceId()));
        domain.setFleetSolution(new FleetSolution(po.getFleetSolution()));
        domain.setAccountStatus(AccountStatus.fromString(po.getStatus()));
        domain.setCreatedDate(po.getCreatedDate());
        domain.setLastUpdated(po.getLastUpdated());
        return domain;
    }

    public static Token tokenDtoToDomain(TokenDTO dto) {
        Token domain = new Token();
        domain.setId(dto.getId());
        domain.setTokenType(TokenType.fromString(dto.getTokenType()));
        domain.setTokenStatus(TokenStatus.fromString(dto.getTokenStatus()));
        domain.setContractId(new ContractId(dto.getContractId(), TokenType.fromString(dto.getTokenType())));
        domain.setAccountId(new AccountId(dto.getAccountId()));
        domain.setCreatedDate(dto.getCreatedDate());
        domain.setAssignedDate(dto.getAssignedDate());
        domain.setLastUpdated(dto.getLastUpdated());
        return domain;
    }

    public static TokenDTO tokenDomainToDto(Token domain) {
        TokenDTO dto = new TokenDTO();
        dto.setId(domain.getId());
        dto.setTokenType(domain.getTokenType().toString());
        dto.setTokenStatus(domain.getTokenStatus().toString());
        dto.setContractId(domain.getContractId().getValue());
        dto.setAccountId(domain.getAccountId().getValue());
        dto.setCreatedDate(domain.getCreatedDate());
        dto.setAssignedDate(domain.getAssignedDate());
        dto.setLastUpdated(domain.getLastUpdated());
        return dto;
    }

    public static TokenPO tokenDomainToPo(Token domain) {
        TokenPO po = new TokenPO();
        po.setId(domain.getId());
        po.setTokenType(domain.getTokenType().toString());
        po.setTokenStatus(domain.getTokenStatus().toString());
        po.setContractId(domain.getContractId().getValue());
        po.setAccountId(domain.getAccountId().getValue());
        po.setCreatedDate(domain.getCreatedDate());
        po.setAssignedDate(domain.getAssignedDate());
        po.setLastUpdated(domain.getLastUpdated());
        return po;
    }

    public static Token tokenPOToDomain(TokenPO po) {
        Token domain = new Token();
        domain.setId(po.getId());
        domain.setTokenType(TokenType.fromString(po.getTokenType()));
        domain.setTokenStatus(TokenStatus.fromString(po.getTokenStatus()));
        domain.setContractId(new ContractId(po.getContractId(), TokenType.fromString(po.getTokenType())));
        domain.setAccountId(new AccountId(po.getAccountId()));
        domain.setCreatedDate(po.getCreatedDate());
        domain.setAssignedDate(po.getAssignedDate());
        domain.setLastUpdated(po.getLastUpdated());
        return domain;
    }

    public static <F, T> List<T> listConvertToList(List<F> fromList, Class<T> tClass) {
        if (fromList == null || fromList.isEmpty()) {
            return null;
        }
        List<T> tList = new ArrayList<>();
        for (F f : fromList) {
            T t = objectConvertToModel(f, tClass);
            tList.add(t);
        }
        return tList;
    }

    public static <F, T> T objectConvertToModel(F entity, Class<T> modelClass) {
        log.debug("entityConvertToModel : Get Entity Properties And Set to Model");
        Object model = null;
        if (entity == null || modelClass == null) {
            return null;
        }

        try {
            model = modelClass.newInstance();
        } catch (InstantiationException e) {
            log.error("entityConvertToModel : Instantiation Exception", e);
        } catch (IllegalAccessException e) {
            log.error("entityConvertToModel : Security Exception", e);
        }
        BeanUtils.copyProperties(entity, model);
        return (T) model;
    }
}
