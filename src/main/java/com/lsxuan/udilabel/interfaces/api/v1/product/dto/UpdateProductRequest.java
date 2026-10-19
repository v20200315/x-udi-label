package com.lsxuan.udilabel.interfaces.api.v1.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateProductRequest(@NotBlank String udiDi, @NotBlank String lotNumber, @NotBlank String name,
                                   String specification, String model, @NotNull LocalDate productionDate,
                                   @NotNull LocalDate expirationDate) {
}