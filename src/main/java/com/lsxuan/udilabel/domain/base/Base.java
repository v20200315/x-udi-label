package com.lsxuan.udilabel.domain.base;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 领域基础类。
 * 包含通用审计信息。
 */
@Getter
public abstract class Base {

    /**
     * 创建时间。
     */
    protected LocalDateTime createdAt;

    /**
     * 创建人ID。
     */
    protected UUID createdBy;

    /**
     * 更新时间。
     */
    protected LocalDateTime updatedAt;

    /**
     * 更新人ID。
     */
    protected UUID updatedBy;

    protected Base() {
    }

    protected Base(LocalDateTime createdAt, UUID createdBy, LocalDateTime updatedAt, UUID updatedBy) {

        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }
}