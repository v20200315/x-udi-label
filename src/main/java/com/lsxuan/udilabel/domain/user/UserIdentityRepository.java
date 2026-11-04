package com.lsxuan.udilabel.domain.user;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserIdentityRepository {

    void save(UserIdentity userIdentity);

    Optional<UserIdentity> findById(UUID id);

    Optional<UserIdentity> findByUserId(String userId);

    List<UserIdentity> findAll();

    void update(UserIdentity userIdentity);
}