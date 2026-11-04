package com.lsxuan.udilabel.application.user;

import com.lsxuan.udilabel.domain.base.SystemUser;
import com.lsxuan.udilabel.domain.user.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserApplicationService {

    private final UserRepository userRepository;
    private final UserIdentityRepository userIdentityRepository;

    public UserApplicationService(UserRepository userRepository, UserIdentityRepository userIdentityRepository) {
        this.userRepository = userRepository;
        this.userIdentityRepository = userIdentityRepository;
    }

    @Transactional
    public UUID getOrCreateUser(ExternalIdentityInfo identityInfo) {
        validateIdentityInfo(identityInfo);

        // 1. Find existing external identity.
        UserIdentity identity =
            userIdentityRepository.findByIssuerAndSubject(identityInfo.issuer(), identityInfo.subject()).orElse(null);

        // 2. Existing identity: return the associated user.
        if (identity != null) {
            User user = userRepository.findById(identity.getUserId())
                .orElseThrow(() -> new IllegalStateException("The user associated with this identity does not exist."));

            validateUserStatus(user);

            return user.getId();
        }

        // 3. New identity must have a verified email.
        if (!identityInfo.emailVerified()) {
            throw new IllegalStateException("Email verification is required before user provisioning.");
        }

        // 4. Create internal user.
        User user = User.create(identityInfo.email(), identityInfo.displayName(), SystemUser.ID);

        userRepository.save(user);

        // 5. Create external identity mapping.
        UserIdentity newIdentity =
            UserIdentity.create(user.getId(), identityInfo.issuer(), identityInfo.subject(), SystemUser.ID);

        userIdentityRepository.save(newIdentity);

        // 6. Return internal user ID.
        return user.getId();
    }

    private void validateIdentityInfo(ExternalIdentityInfo identityInfo) {

        if (identityInfo == null) {
            throw new IllegalArgumentException("External identity information must not be null.");
        }

        if (isBlank(identityInfo.issuer())) {
            throw new IllegalArgumentException("Issuer must not be blank.");
        }

        if (isBlank(identityInfo.subject())) {
            throw new IllegalArgumentException("Subject must not be blank.");
        }

        if (isBlank(identityInfo.email())) {
            throw new IllegalArgumentException("Email must not be blank.");
        }

        if (isBlank(identityInfo.displayName())) {
            throw new IllegalArgumentException("Display name must not be blank.");
        }
    }

    private void validateUserStatus(User user) {

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalStateException("This user account is inactive.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}