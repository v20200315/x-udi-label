package com.lsxuan.udilabel.infrastructure.persistence.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lsxuan.udilabel.domain.user.User;
import com.lsxuan.udilabel.domain.user.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserMapper userMapper;

    public UserRepositoryImpl(UserMapper userMapper) {

        this.userMapper = userMapper;
    }

    @Override
    public void save(User user) {

        userMapper.insert(toDO(user));
    }

    @Override
    public Optional<User> findById(UUID id) {

        UserDO userDO = userMapper.selectById(id);
        return Optional.ofNullable(userDO).map(this::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {

        LambdaQueryWrapper<UserDO> qw = new LambdaQueryWrapper<>();
        qw.eq(UserDO::getEmail, email);
        UserDO userDO = userMapper.selectOne(qw);
        return Optional.ofNullable(userDO).map(this::toDomain);
    }

    @Override
    public List<User> findAll() {

        return userMapper.selectList(null).stream().map(this::toDomain).toList();
    }

    @Override
    public void update(User user) {

        userMapper.updateById(toDO(user));
    }

    @Override
    public void delete(UUID id) {

        userMapper.delete(new LambdaQueryWrapper<UserDO>().eq(UserDO::getId, id));
    }

    @Override
    public IPage<User> findPage(int pageNum, int pageSize) {

        LambdaQueryWrapper<UserDO> qw = new LambdaQueryWrapper<>();
        qw.orderByDesc(UserDO::getUpdatedAt);

        Page<UserDO> page = new Page<>(pageNum, pageSize);
        IPage<UserDO> iPage = userMapper.selectPage(page, qw);
        return iPage.convert(this::toDomain);
    }

    private UserDO toDO(User user) {

        UserDO userDO = new UserDO();
        userDO.setId(user.getId());
        userDO.setEmail(user.getEmail());
        userDO.setDisplayName(user.getDisplayName());
        userDO.setStatus(user.getStatus());
        userDO.setCreatedAt(user.getCreatedAt());
        userDO.setCreatedBy(user.getCreatedBy());
        userDO.setUpdatedAt(user.getUpdatedAt());
        userDO.setUpdatedBy(user.getUpdatedBy());
        return userDO;
    }

    private User toDomain(UserDO userDO) {

        return User.reconstitute(userDO.getId(), userDO.getEmail(), userDO.getDisplayName(), userDO.getStatus(),
            userDO.getCreatedAt(), userDO.getCreatedBy(), userDO.getUpdatedAt(), userDO.getUpdatedBy());
    }
}