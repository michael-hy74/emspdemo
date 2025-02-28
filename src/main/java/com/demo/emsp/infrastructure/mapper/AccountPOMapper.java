package com.demo.emsp.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.emsp.infrastructure.po.AccountPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountPOMapper extends BaseMapper<AccountPO> {
}
