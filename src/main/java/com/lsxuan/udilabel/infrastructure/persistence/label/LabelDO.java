package com.lsxuan.udilabel.infrastructure.persistence.label;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lsxuan.udilabel.domain.label.CodeType;
import com.lsxuan.udilabel.infrastructure.persistence.typehandler.UUIDTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@TableName(value = "label", autoResultMap = true)
public class LabelDO {

    @TableField(typeHandler = UUIDTypeHandler.class)
    private UUID id;
    @TableField(typeHandler = UUIDTypeHandler.class)
    private UUID productId;
    private String name;
    private String productName;
    private String udiDi;
    private CodeType codeType;
    private String codeContent;
    private String imageUrl;
    private LocalDateTime createdAt;
    @TableField(typeHandler = UUIDTypeHandler.class)
    private UUID createdBy;
    private LocalDateTime updatedAt;
    @TableField(typeHandler = UUIDTypeHandler.class)
    private UUID updatedBy;
}
