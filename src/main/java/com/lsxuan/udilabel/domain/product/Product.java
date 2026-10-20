package com.lsxuan.udilabel.domain.product;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Product {

    private UUID id;
    private String indicatorDigit;
    private String companyPrefix;
    private String itemReference;
    private String checkDigit;
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

    public static Product create(String indicatorDigit, String companyPrefix, String itemReference, String lotNumber,
        String name, String specification, String model, LocalDate productionDate, LocalDate expirationDate,
        UUID operatorId) {
        validateIndicatorDigit(indicatorDigit);
        validateCompanyPrefixAndItemReference(companyPrefix, itemReference);
        validateLotNumber(lotNumber);
        validateName(name);
        validateProductionDate(productionDate);
        validateExpirationDate(expirationDate);
        validateOperatorId(operatorId);

        Product product = new Product();

        LocalDateTime now = LocalDateTime.now();

        product.id = UuidCreator.getTimeOrderedEpoch();
        product.indicatorDigit = indicatorDigit;
        product.companyPrefix = companyPrefix;
        product.itemReference = itemReference;
        product.checkDigit = calculateCheckDigit(indicatorDigit, companyPrefix, itemReference);
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

    public static Product reconstitute(UUID id, String indicatorDigit, String companyPrefix, String itemReference,
        String checkDigit, String lotNumber, String name, String specification, String model, LocalDate productionDate,
        LocalDate expirationDate, ProductStatus status, LocalDateTime createdAt, UUID createdBy,
        LocalDateTime updatedAt, UUID updatedBy) {

        Product product = new Product();

        product.id = id;
        product.indicatorDigit = indicatorDigit;
        product.companyPrefix = companyPrefix;
        product.itemReference = itemReference;
        product.checkDigit = checkDigit;
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

    public void updateBasicInfo(String indicatorDigit, String companyPrefix, String itemReference, String lotNumber,
        String name, String specification, String model, LocalDate productionDate, LocalDate expirationDate,
        UUID operatorId) {

        if (this.status == ProductStatus.INACTIVE) {
            throw new IllegalStateException("Deactivated product cannot be modified.");
        }

        validateIndicatorDigit(indicatorDigit);
        validateCompanyPrefixAndItemReference(companyPrefix, itemReference);
        validateLotNumber(lotNumber);
        validateName(name);
        validateProductionDate(productionDate);
        validateExpirationDate(expirationDate);
        validateOperatorId(operatorId);

        this.indicatorDigit = indicatorDigit;
        this.companyPrefix = companyPrefix;
        this.itemReference = itemReference;
        this.checkDigit = calculateCheckDigit(indicatorDigit, companyPrefix, itemReference);
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

    private static void validateIndicatorDigit(String indicatorDigit) {

        if (indicatorDigit == null || !indicatorDigit.matches("[0-9]")) {
            throw new IllegalArgumentException("indicatorDigit must be a single digit from 0 to 9.");
        }
    }

    private static void validateCompanyPrefixAndItemReference(String companyPrefix, String itemReference) {

        if (companyPrefix == null || !companyPrefix.matches("[0-9]+")) {
            throw new IllegalArgumentException("companyPrefix must contain only digits.");
        }

        if (itemReference == null || !itemReference.matches("[0-9]+")) {
            throw new IllegalArgumentException("itemReference must contain only digits.");
        }

        if (companyPrefix.length() + itemReference.length() != 12) {
            throw new IllegalArgumentException(
                "companyPrefix and itemReference must contain exactly 12 digits in total.");
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

    private static String calculateCheckDigit(String indicatorDigit, String companyPrefix, String itemReference) {

        String digits = indicatorDigit + companyPrefix + itemReference;

        int sum = 0;
        int weight = 3;

        for (int i = digits.length() - 1; i >= 0; i--) {
            sum += Character.digit(digits.charAt(i), 10) * weight;
            weight = (weight == 3) ? 1 : 3;
        }

        return String.valueOf((10 - sum % 10) % 10);
    }

}
