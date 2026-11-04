package com.lsxuan.udilabel.infrastructure.persistence.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lsxuan.udilabel.domain.user.UserIdentity;
import com.lsxuan.udilabel.domain.user.UserIdentityRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserIdentityRepositoryImpl implements UserIdentityRepository {

    private final UserIdentityMapper userIdentityMapper;

    public UserIdentityRepositoryImpl(UserIdentityMapper userIdentityMapper) {

        this.userIdentityMapper = userIdentityMapper;
    }

    @Override
    public void save(UserIdentity userIdentity) {

        userIdentityMapper.insert(toDO(userIdentity));
    }

    @Override
    public Optional<UserIdentity> findById(UUID id) {

        UserIdentityDO userIdentityDO = userIdentityMapper.selectById(id);
        return Optional.ofNullable(userIdentityDO).map(this::toDomain);
    }

    @Override
    public Optional<UserIdentity> findByUserId(String userId) {

        LambdaQueryWrapper<UserIdentityDO> qw = new LambdaQueryWrapper<>();
        qw.eq(UserIdentityDO::getUserId, userId);
        UserIdentityDO userIdentityDO = userIdentityMapper.selectOne(qw);
        return Optional.ofNullable(userIdentityDO).map(this::toDomain);
    }

    @Override
    public Optional<UserIdentity> findByIssuerAndSubject(String issuer, String subject) {

        LambdaQueryWrapper<UserIdentityDO> qw = new LambdaQueryWrapper<>();
        qw.eq(UserIdentityDO::getIssuer, issuer);
        qw.eq(UserIdentityDO::getSubject, subject);
        UserIdentityDO userIdentityDO = userIdentityMapper.selectOne(qw);
        return Optional.ofNullable(userIdentityDO).map(this::toDomain);
    }

    @Override
    public List<UserIdentity> findAll() {

        return userIdentityMapper.selectList(null).stream().map(this::toDomain).toList();
    }

    @Override
    public void update(UserIdentity userIdentity) {

        userIdentityMapper.updateById(toDO(userIdentity));
    }

    private UserIdentityDO toDO(UserIdentity userIdentity) {

        UserIdentityDO userIdentityDO = new UserIdentityDO();
        userIdentityDO.setId(userIdentity.getId());
        userIdentityDO.setUserId(userIdentity.getUserId());
        userIdentityDO.setIssuer(userIdentity.getIssuer());
        userIdentityDO.setSubject(userIdentity.getSubject());
        userIdentityDO.setCreatedAt(userIdentity.getCreatedAt());
        userIdentityDO.setCreatedBy(userIdentity.getCreatedBy());
        userIdentityDO.setUpdatedAt(userIdentity.getUpdatedAt());
        userIdentityDO.setUpdatedBy(userIdentity.getUpdatedBy());
        return userIdentityDO;
    }

    private UserIdentity toDomain(UserIdentityDO userIdentityDO) {

        return UserIdentity.reconstitute(userIdentityDO.getId(), userIdentityDO.getUserId(), userIdentityDO.getIssuer(),
            userIdentityDO.getSubject(), userIdentityDO.getCreatedAt(), userIdentityDO.getCreatedBy(),
            userIdentityDO.getUpdatedAt(), userIdentityDO.getUpdatedBy());
    }
}