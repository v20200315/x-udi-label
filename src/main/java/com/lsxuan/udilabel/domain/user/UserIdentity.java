package com.lsxuan.udilabel.domain.user;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class UserIdentity {

    private UUID id;
    private UUID userId;
    private String issuer;
    private String subject;
    private LocalDateTime createdAt;
    private UUID createdBy;
    private LocalDateTime updatedAt;
    private UUID updatedBy;

    protected UserIdentity() {

    }

    public static UserIdentity create(UUID userId, String issuer, String subject, UUID operatorId) {

        validateIssuer(issuer);
        validateSubject(subject);
        validateOperatorId(operatorId);

        UserIdentity userIdentity = new UserIdentity();

        LocalDateTime now = LocalDateTime.now();

        userIdentity.id = UuidCreator.getTimeOrderedEpoch();
        userIdentity.userId = userId;
        userIdentity.issuer = issuer;
        userIdentity.subject = subject;
        userIdentity.createdAt = now;
        userIdentity.createdBy = operatorId;
        userIdentity.updatedAt = now;
        userIdentity.updatedBy = operatorId;

        return userIdentity;
    }

    public static UserIdentity reconstitute(UUID id, UUID userId, String issuer, String subject,
        LocalDateTime createdAt, UUID createdBy, LocalDateTime updatedAt, UUID updatedBy) {

        UserIdentity userIdentity = new UserIdentity();

        userIdentity.id = id;
        userIdentity.userId = userId;
        userIdentity.issuer = issuer;
        userIdentity.subject = subject;
        userIdentity.createdAt = createdAt;
        userIdentity.createdBy = createdBy;
        userIdentity.updatedAt = updatedAt;
        userIdentity.updatedBy = updatedBy;

        return userIdentity;
    }

    private static void validateIssuer(String issuer) {

        if (issuer == null || issuer.isBlank()) {
            throw new IllegalArgumentException("Issuer cannot be empty.");
        }
    }

    private static void validateSubject(String subject) {

        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("Subject cannot be empty.");
        }
    }

    private static void validateOperatorId(UUID operatorId) {

        if (operatorId == null) {
            throw new IllegalArgumentException("operatorId cannot be empty.");
        }
    }
}
