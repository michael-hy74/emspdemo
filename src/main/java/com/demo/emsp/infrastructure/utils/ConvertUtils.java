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
import java.util.Optional;

@Slf4j
public class ConvertUtils {

    public static Account accountDtoToDomain(AccountDTO dto) {
        Account domain = new Account();
        domain.setId(dto.getId());
        domain.setServiceId(Optional.ofNullable(dto.getServiceId()).map(ServiceId::new).orElse(null));
        domain.setFleetSolution(Optional.ofNullable(dto.getFleetSolution()).map(FleetSolution::new).orElse(null));
        domain.setAccountStatus(AccountStatus.fromString(dto.getAccountStatus()));
        domain.setCreatedDate(dto.getCreatedDate());
        domain.setLastUpdated(dto.getLastUpdated());
        return domain;
    }

    public static AccountDTO accountDomainToDto(Account domain) {
        AccountDTO dto = new AccountDTO();
        dto.setId(domain.getId());
        dto.setServiceId(Optional.ofNullable(domain.getServiceId())
                .map(ServiceId::getValue).orElse(""));
        dto.setFleetSolution(Optional.ofNullable(domain.getFleetSolution())
                .map(FleetSolution::getValue).orElse(""));
        dto.setAccountStatus(domain.getAccountStatus().toString());
        dto.setCreatedDate(domain.getCreatedDate());
        dto.setLastUpdated(domain.getLastUpdated());
        return dto;
    }

    public static AccountPO accountDomainToPo(Account domain) {
        AccountPO po = new AccountPO();
        po.setId(domain.getId());
        po.setServiceId(Optional.ofNullable(domain.getServiceId())
                .map(ServiceId::getValue).orElse(""));
        po.setFleetSolution(Optional.ofNullable(domain.getFleetSolution())
                .map(FleetSolution::getValue).orElse(""));
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
        domain.setContractId(Optional.ofNullable(dto.getContractId())
                .map(contractId -> new ContractId(dto.getContractId(), TokenType.fromString(dto.getTokenType())))
                .orElse(null));
        domain.setAccountId(Optional.ofNullable(dto.getAccountId()).map(AccountId::new).orElse(null));
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
        po.setContractId(Optional.ofNullable(domain.getContractId())
                .map(ContractId::getValue).orElse(""));
        po.setAccountId(Optional.ofNullable(domain.getAccountId())
                .map(AccountId::getValue).orElse(""));
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
