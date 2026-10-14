package com.lsxuan.udilabel.domain.product;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Product {

    private UUID id;
    private String name;
    private String udiDi;
    private ProductStatus status;
    private LocalDateTime createdAt;
    private UUID createdBy;
    private LocalDateTime updatedAt;
    private UUID updatedBy;

    protected Product() {
    }

    public static Product create(String name, String udiDi, UUID operatorId) {
        validateName(name);
        validateUdiDi(udiDi);
        validateOperatorId(operatorId);

        Product product = new Product();

        LocalDateTime now = LocalDateTime.now();

        product.id = UuidCreator.getTimeOrderedEpoch();
        product.name = name;
        product.udiDi = udiDi;
        product.status = ProductStatus.ACTIVE;
        product.createdAt = now;
        product.createdBy = operatorId;
        product.updatedAt = now;
        product.updatedBy = operatorId;

        return product;
    }

    public static Product reconstitute(UUID id, String name, String udiDi, ProductStatus status,
        LocalDateTime createdAt, UUID createdBy, LocalDateTime updatedAt, UUID updatedBy) {

        Product product = new Product();

        product.id = id;
        product.name = name;
        product.udiDi = udiDi;
        product.status = status;
        product.createdAt = createdAt;
        product.createdBy = createdBy;
        product.updatedAt = updatedAt;
        product.updatedBy = updatedBy;

        return product;
    }

    public void updateBasicInfo(String name, String udiDi, UUID operatorId) {
        if (this.status == ProductStatus.INACTIVE) {
            throw new IllegalStateException("Deactivated product cannot be modified.");
        }

        validateName(name);
        validateOperatorId(operatorId);

        this.name = name;
        this.udiDi = udiDi;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = operatorId;
    }

    public void deactivate(UUID operatorId) {

        validateOperatorId(operatorId);

        if (this.status == ProductStatus.INACTIVE) {
            return;
        }

        this.status = ProductStatus.INACTIVE;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = operatorId;
    }

    public void activate(UUID operatorId) {

        validateOperatorId(operatorId);

        if (this.status == ProductStatus.ACTIVE) {
            return;
        }

        this.status = ProductStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = operatorId;
    }

    private static void validateName(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
    }

    private static void validateUdiDi(String udiDi) {

        if (udiDi == null || udiDi.isBlank()) {
            throw new IllegalArgumentException("udiDi cannot be empty.");
        }
    }

    private static void validateOperatorId(UUID operatorId) {

        if (operatorId == null) {
            throw new IllegalArgumentException("operatorId cannot be empty.");
        }
    }

}
