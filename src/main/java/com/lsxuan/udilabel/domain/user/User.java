package com.lsxuan.udilabel.domain.user;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class User {

    private UUID id;
    private String email;
    private String displayName;
    private UserStatus status;
    private LocalDateTime createdAt;
    private UUID createdBy;
    private LocalDateTime updatedAt;
    private UUID updatedBy;

    protected User() {

    }

    public static User create(String email, String displayName, UUID operatorId) {

        validateEmail(email);
        validateDisplayName(displayName);
        validateOperatorId(operatorId);

        User user = new User();

        LocalDateTime now = LocalDateTime.now();

        user.id = UuidCreator.getTimeOrderedEpoch();
        user.email = email;
        user.displayName = displayName;
        user.status = UserStatus.ACTIVE;
        user.createdAt = now;
        user.createdBy = operatorId;
        user.updatedAt = now;
        user.updatedBy = operatorId;

        return user;
    }

    public static User reconstitute(UUID id, String email, String displayName, UserStatus status,
        LocalDateTime createdAt, UUID createdBy, LocalDateTime updatedAt, UUID updatedBy) {

        User user = new User();

        user.id = id;
        user.email = email;
        user.displayName = displayName;
        user.status = status;
        user.createdAt = createdAt;
        user.createdBy = createdBy;
        user.updatedAt = updatedAt;
        user.updatedBy = updatedBy;

        return user;
    }

    public void updateBasicInfo(String email, String displayName, UUID operatorId) {

        if (this.status == UserStatus.INACTIVE) {
            throw new IllegalStateException("Deactivated user cannot be modified.");
        }

        validateEmail(email);
        validateDisplayName(displayName);
        validateOperatorId(operatorId);

        this.email = email;
        this.displayName = displayName;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = operatorId;
    }

    public void deactivate(UUID operatorId) {

        validateOperatorId(operatorId);

        if (this.status == UserStatus.INACTIVE) {
            return;
        }

        this.status = UserStatus.INACTIVE;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = operatorId;
    }

    public void activate(UUID operatorId) {

        validateOperatorId(operatorId);

        if (this.status == UserStatus.ACTIVE) {
            return;
        }

        this.status = UserStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = operatorId;
    }

    private static void validateEmail(String email) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }
    }

    private static void validateDisplayName(String displayName) {

        if (displayName == null || displayName.isBlank()) {
            throw new IllegalArgumentException("DisplayName cannot be empty.");
        }
    }

    private static void validateOperatorId(UUID operatorId) {

        if (operatorId == null) {
            throw new IllegalArgumentException("operatorId cannot be empty.");
        }
    }
}
