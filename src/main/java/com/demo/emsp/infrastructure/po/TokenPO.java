package com.demo.emsp.infrastructure.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("token")
public class TokenPO {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @TableField("type")
    private String tokenType;

    @TableField("status")
    private String tokenStatus;

    @TableField("value")
    private String value;

    @TableField("account_id")
    private String accountId;

    @TableField("created_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdDate;

    @TableField("assigned_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime assignedDate;

    @TableField("last_updated")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime lastUpdated;

}
