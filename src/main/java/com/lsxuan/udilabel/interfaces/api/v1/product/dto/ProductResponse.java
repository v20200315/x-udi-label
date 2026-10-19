package com.lsxuan.udilabel.interfaces.api.v1.product.dto;

import com.lsxuan.udilabel.domain.product.Product;
import com.lsxuan.udilabel.domain.product.ProductStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponse(UUID id, String udiDi, String lotNumber, String name, String specification, String model,
                              LocalDate productionDate, LocalDate expirationDate, ProductStatus status,
                              LocalDateTime createdAt, UUID createdBy, LocalDateTime updatedAt, UUID updatedBy) {

    public static ProductResponse from(Product product) {

        return new ProductResponse(product.getId(), product.getUdiDi(), product.getLotNumber(), product.getName(),
            product.getSpecification(), product.getModel(), product.getProductionDate(), product.getExpirationDate(),
            product.getStatus(), product.getCreatedAt(), product.getCreatedBy(), product.getUpdatedAt(),
            product.getUpdatedBy());
    }
}