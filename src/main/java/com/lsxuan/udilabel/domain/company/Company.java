package com.lsxuan.udilabel.domain.company;

import com.github.f4b6a3.uuid.UuidCreator;
import com.lsxuan.udilabel.domain.base.Base;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Company extends Base {

    private UUID id;
    private UUID userId;
    private String companyName;
    private String companyPrefix;

    protected Company() {

    }

    public static Company create(UUID userId, String companyName, String companyPrefix, UUID operatorId) {

        validateUserId(userId);
        validateCompanyName(companyName);
        validateCompanyPrefix(companyPrefix);
        validateOperatorId(operatorId);

        Company company = new Company();

        LocalDateTime now = LocalDateTime.now();

        company.id = UuidCreator.getTimeOrderedEpoch();
        company.userId = userId;
        company.companyName = companyName;
        company.companyPrefix = companyPrefix;
        company.createdAt = now;
        company.createdBy = operatorId;
        company.updatedAt = now;
        company.updatedBy = operatorId;

        return company;
    }

    public static Company reconstitute(UUID id, UUID userId, String companyName, String companyPrefix,
        LocalDateTime createdAt, UUID createdBy, LocalDateTime updatedAt, UUID updatedBy) {

        Company company = new Company();

        company.id = id;
        company.userId = userId;
        company.companyName = companyName;
        company.companyPrefix = companyPrefix;
        company.createdAt = createdAt;
        company.createdBy = createdBy;
        company.updatedAt = updatedAt;
        company.updatedBy = updatedBy;

        return company;
    }

    public void updateBasicInfo(String companyName, String companyPrefix, UUID operatorId) {

        validateCompanyName(companyName);
        validateCompanyPrefix(companyPrefix);
        validateOperatorId(operatorId);

        this.companyName = companyName;
        this.companyPrefix = companyPrefix;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = operatorId;
    }

    private static void validateUserId(UUID userId) {

        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be empty.");
        }
    }

    private static void validateCompanyName(String companyName) {

        if (companyName == null || companyName.isBlank()) {
            throw new IllegalArgumentException("CompanyName cannot be empty.");
        }
    }

    private static void validateCompanyPrefix(String companyPrefix) {

        if (companyPrefix == null || companyPrefix.isBlank()) {
            throw new IllegalArgumentException("CompanyPrefix cannot be empty.");
        }

        if (!companyPrefix.matches("69\\d{5,8}")) {
            throw new IllegalArgumentException("CompanyPrefix must start with 69 and be 7 to 10 digits long.");
        }
    }

    private static void validateOperatorId(UUID operatorId) {

        if (operatorId == null) {
            throw new IllegalArgumentException("OperatorId cannot be empty.");
        }
    }
}
