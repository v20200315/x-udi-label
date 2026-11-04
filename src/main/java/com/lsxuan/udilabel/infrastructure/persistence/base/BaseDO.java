package com.lsxuan.udilabel.infrastructure.persistence.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.lsxuan.udilabel.infrastructure.persistence.typehandler.UUIDTypeHandler;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public abstract class BaseDO {

    /**
     * 创建时间。
     */
    private LocalDateTime createdAt;

    /**
     * 创建人ID。
     */
    @TableField(typeHandler = UUIDTypeHandler.class)
    private UUID createdBy;

    /**
     * 更新时间。
     */
    private LocalDateTime updatedAt;

    /**
     * 更新人ID。
     */
    @TableField(typeHandler = UUIDTypeHandler.class)
    private UUID updatedBy;
}