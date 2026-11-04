package com.lsxuan.udilabel.infrastructure.persistence.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lsxuan.udilabel.infrastructure.persistence.base.BaseDO;
import com.lsxuan.udilabel.infrastructure.persistence.typehandler.UUIDTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "x_user_identity", autoResultMap = true)
public class UserIdentityDO extends BaseDO {

    @TableField(typeHandler = UUIDTypeHandler.class)
    private UUID id;
    @TableField(typeHandler = UUIDTypeHandler.class)
    private UUID userId;
    /**
     * 身份提供商的唯一标识，通常为 OIDC Issuer URL。
     */
    private String issuer;
    /**
     * 用户在身份提供商中的唯一标识（OIDC Subject）。
     */
    private String subject;
}