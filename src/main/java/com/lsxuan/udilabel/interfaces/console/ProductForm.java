package com.lsxuan.udilabel.interfaces.console;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ProductForm {

    @NotBlank(message = "请选择包装指示符")
    @Pattern(regexp = "^\\d$", message = "包装指示符必须为一位数字")
    private String indicatorDigit;

    @NotBlank(message = "请输入厂商识别代码")
    @Pattern(regexp = "^69\\d{5,8}$", message = "厂商识别代码必须以69开头，且为7至10位纯数字")
    private String companyPrefix;

    @NotBlank(message = "请输入商品项目代码")
    @Pattern(regexp = "^\\d+$", message = "商品项目代码必须为纯数字")
    private String itemReference;

    private String checkDigit;

    @NotBlank(message = "请输入批号")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]+$",
        message = "批号必须由英文字母和数字组成，且至少包含一个字母和一个数字")
    @Size(max = 20, message = "批号不能超过20个字符")
    private String lotNumber;

    @NotBlank(message = "请输入产品名称")
    @Size(max = 50, message = "产品名称不能超过50个字符")
    private String name;

    @NotBlank(message = "请输入产品规格")
    @Size(max = 200, message = "产品规格不能超过200个字符")
    private String specification;

    @NotBlank(message = "请输入产品型号")
    @Size(max = 200, message = "产品型号不能超过200个字符")
    private String model;

    @NotNull(message = "请选择生产日期")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate productionDate;

    @NotNull(message = "请选择有效期截止日期")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate expirationDate;

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

    public static ProductForm empty() {
        return new ProductForm();
    }
}
