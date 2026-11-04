package com.lsxuan.udilabel.infrastructure.persistence.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lsxuan.udilabel.domain.user.UserStatus;
import com.lsxuan.udilabel.infrastructure.persistence.typehandler.UUIDTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@TableName(value = "application_user", autoResultMap = true)
public class UserDO {

    @TableField(typeHandler = UUIDTypeHandler.class)
    private UUID id;
    /**
     * 用户邮箱，同时作为登录用户名。
     */
    private String email;
    /**
     * 用户显示名称。
     */
    private String displayName;
    /**
     * 用户状态：1-启用，0-停用。
     */
    private UserStatus status;
    private LocalDateTime createdAt;
    @TableField(typeHandler = UUIDTypeHandler.class)
    private UUID createdBy;
    private LocalDateTime updatedAt;
    @TableField(typeHandler = UUIDTypeHandler.class)
    private UUID updatedBy;
}