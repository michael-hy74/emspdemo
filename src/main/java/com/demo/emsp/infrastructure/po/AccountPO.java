package com.demo.emsp.infrastructure.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("account")
public class AccountPO {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @TableField("service_id")
    private String serviceId;

    @TableField("fleet_solution")
    private String fleetSolution;

    @TableField("contract_id")
    private String contractId;

    @TableField("status")
    private String status;

    @TableField("created_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdDate;

    @TableField("last_updated")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime lastUpdated;
}
