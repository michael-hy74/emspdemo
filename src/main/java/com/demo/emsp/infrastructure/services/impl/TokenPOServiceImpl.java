package com.demo.emsp.infrastructure.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.emsp.infrastructure.mapper.TokenPOMapper;
import com.demo.emsp.infrastructure.po.TokenPO;
import com.demo.emsp.infrastructure.services.TokenPOService;
import org.springframework.stereotype.Service;

@Service
public class TokenPOServiceImpl extends ServiceImpl<TokenPOMapper, TokenPO> implements TokenPOService {
}
