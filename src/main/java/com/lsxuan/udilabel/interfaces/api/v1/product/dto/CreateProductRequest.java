package com.lsxuan.udilabel.interfaces.api.v1.product.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record CreateProductRequest(@NotBlank(message = "请选择包装指示符") String indicatorDigit,
                                   @NotBlank(message = "请输入厂商识别码") @Pattern(regexp = "^69\\d{5,8}$",
                                       message = "厂商识别码必须以69开头，且为7至10位纯数字") String companyPrefix,
                                   @NotBlank(message = "请输入商品项目代码") @Pattern(regexp = "^\\d+$",
                                       message = "商品项目代码必须为纯数字") String itemReference,
                                   @NotBlank(message = "请输入批号") @Pattern(
                                       regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]+$",
                                       message = "批号必须由英文字母和数字组成，且至少包含一个字母和一个数字") String lotNumber,
                                   @NotBlank(message = "请输入产品名称") String name,
                                   @NotBlank(message = "请输入产品规格") String specification,
                                   @NotBlank(message = "请输入产品型号") String model,
                                   @NotNull(message = "请选择生产日期") @DateTimeFormat(
                                       iso = DateTimeFormat.ISO.DATE) LocalDate productionDate,
                                   @NotNull(message = "请选择有效期截止日期") @DateTimeFormat(
                                       iso = DateTimeFormat.ISO.DATE) LocalDate expirationDate) {

    @AssertTrue
    public boolean isValidItemReferenceLength() {

        if (companyPrefix == null || itemReference == null || companyPrefix.isBlank() || itemReference.isBlank()) {
            return true;
        }
        return companyPrefix.length() + itemReference.length() == 12;
    }

    @AssertTrue
    public boolean isValidDateRange() {
        if (productionDate == null || expirationDate == null) {
            return true;
        }

        return !expirationDate.isBefore(productionDate);
    }
}