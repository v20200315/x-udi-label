package com.lsxuan.udilabel.domain.product;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Product {

    private UUID id;
    private String udiDi;
    private String lotNumber;
    private String name;
    private String specification;
    private String model;
    private LocalDate productionDate;
    private LocalDate expirationDate;
    private ProductStatus status;
    private LocalDateTime createdAt;
    private UUID createdBy;
    private LocalDateTime updatedAt;
    private UUID updatedBy;

    protected Product() {
    }

    public static Product create(String udiDi, String lotNumber, String name, String specification, String model,
        LocalDate productionDate, LocalDate expirationDate, UUID operatorId) {
        validateUdiDi(udiDi);
        validateLotNumber(lotNumber);
        validateName(name);
        validateProductionDate(productionDate);
        validateExpirationDate(expirationDate);
        validateOperatorId(operatorId);

        Product product = new Product();

        LocalDateTime now = LocalDateTime.now();

        product.id = UuidCreator.getTimeOrderedEpoch();
        product.udiDi = udiDi;
        product.lotNumber = lotNumber;
        product.name = name;
        product.specification = specification;
        product.model = model;
        product.productionDate = productionDate;
        product.expirationDate = expirationDate;
        product.status = ProductStatus.ACTIVE;
        product.createdAt = now;
        product.createdBy = operatorId;
        product.updatedAt = now;
        product.updatedBy = operatorId;

        return product;
    }

    public static Product reconstitute(UUID id, String udiDi, String lotNumber, String name, String specification,
        String model, LocalDate productionDate, LocalDate expirationDate, ProductStatus status, LocalDateTime createdAt,
        UUID createdBy, LocalDateTime updatedAt, UUID updatedBy) {

        Product product = new Product();

        product.id = id;
        product.udiDi = udiDi;
        product.lotNumber = lotNumber;
        product.name = name;
        product.specification = specification;
        product.model = model;
        product.productionDate = productionDate;
        product.expirationDate = expirationDate;
        product.status = status;
        product.createdAt = createdAt;
        product.createdBy = createdBy;
        product.updatedAt = updatedAt;
        product.updatedBy = updatedBy;

        return product;
    }

    public void updateBasicInfo(String udiDi, String lotNumber, String name, String specification, String model,
        LocalDate productionDate, LocalDate expirationDate, UUID operatorId) {
        if (this.status == ProductStatus.INACTIVE) {
            throw new IllegalStateException("Deactivated product cannot be modified.");
        }

        validateUdiDi(udiDi);
        validateLotNumber(lotNumber);
        validateName(name);
        validateProductionDate(productionDate);
        validateExpirationDate(expirationDate);
        validateOperatorId(operatorId);

        this.udiDi = udiDi;
        this.lotNumber = lotNumber;
        this.name = name;
        this.specification = specification;
        this.model = model;
        this.productionDate = productionDate;
        this.expirationDate = expirationDate;
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

    private static void validateUdiDi(String udiDi) {

        if (udiDi == null || udiDi.isBlank()) {
            throw new IllegalArgumentException("udiDi cannot be empty.");
        }
    }

    private static void validateLotNumber(String lotNumber) {

        if (lotNumber == null || lotNumber.isBlank()) {
            throw new IllegalArgumentException("lotNumber cannot be empty.");
        }
    }

    private static void validateName(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
    }

    private static void validateProductionDate(LocalDate productionDate) {

        if (productionDate == null) {
            throw new IllegalArgumentException("productionDate cannot be empty.");
        }
    }

    private static void validateExpirationDate(LocalDate expirationDate) {

        if (expirationDate == null) {
            throw new IllegalArgumentException("expirationDate cannot be empty.");
        }
    }

    private static void validateOperatorId(UUID operatorId) {

        if (operatorId == null) {
            throw new IllegalArgumentException("operatorId cannot be empty.");
        }
    }

}
