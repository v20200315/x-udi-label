package com.lsxuan.udilabel.domain.user;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    void save(User user);

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    void update(User user);

    void delete(UUID id);

    IPage<User> findPage(int pageNum, int pageSize);
}