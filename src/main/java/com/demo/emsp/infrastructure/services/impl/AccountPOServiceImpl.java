package com.demo.emsp.infrastructure.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.emsp.infrastructure.mapper.AccountPOMapper;
import com.demo.emsp.infrastructure.po.AccountPO;
import com.demo.emsp.infrastructure.services.AccountPOService;
import org.springframework.stereotype.Service;

@Service
public class AccountPOServiceImpl extends ServiceImpl<AccountPOMapper, AccountPO> implements AccountPOService {
}
