package com.lsxuan.udilabel.interfaces.api.v1.product.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateProductRequest(@NotBlank String name, @NotBlank String udiDi) {
}