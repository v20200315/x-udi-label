package com.lsxuan.udilabel.infrastructure.persistence.product;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lsxuan.udilabel.domain.product.ProductStatus;
import com.lsxuan.udilabel.infrastructure.persistence.base.BaseDO;
import com.lsxuan.udilabel.infrastructure.persistence.typehandler.UUIDTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "x_product", autoResultMap = true)
public class ProductDO extends BaseDO {

    @TableField(typeHandler = UUIDTypeHandler.class)
    private UUID id;
    /**
     * 包装指示符 Indicator Digit，0~9
     */
    private String indicatorDigit;
    /**
     * 厂商识别码 GS1 Company Prefix，7~10 位，长度由 GS1 分配决定
     */
    private String companyPrefix;
    /**
     * 商品项目代码 Item Reference，位数 = 12 - companyPrefix.length()
     */
    private String itemReference;
    /**
     * 校验位 Check Digit，Mod10 算出
     */
    private String checkDigit;
    /**
     * 批号（Lot Number）。 用于标识产品的生产批次，可用于追溯产品生产及流通信息。
     */
    private String lotNumber;
    /**
     * 产品名称。
     */
    private String name;
    /**
     * 产品规格。
     */
    private String specification;
    /**
     * 产品型号。
     */
    private String model;
    /**
     * 生产日期。
     */
    private LocalDate productionDate;
    /**
     * 有效期截止日期。
     */
    private LocalDate expirationDate;
    /**
     * 产品状态。
     */
    private ProductStatus status;
}