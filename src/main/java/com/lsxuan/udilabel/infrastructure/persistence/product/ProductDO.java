package com.lsxuan.udilabel.infrastructure.persistence.product;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lsxuan.udilabel.domain.product.ProductStatus;
import com.lsxuan.udilabel.infrastructure.persistence.typehandler.UUIDTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@TableName(value = "product", autoResultMap = true)
public class ProductDO {

    @TableField(typeHandler = UUIDTypeHandler.class)
    private UUID id;
    private String name;
    private String udiDi;
    private ProductStatus status;
    private LocalDateTime createdAt;
    @TableField(typeHandler = UUIDTypeHandler.class)
    private UUID createdBy;
    private LocalDateTime updatedAt;
    @TableField(typeHandler = UUIDTypeHandler.class)
    private UUID updatedBy;
}