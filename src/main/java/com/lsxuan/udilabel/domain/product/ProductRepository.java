package com.lsxuan.udilabel.domain.product;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    void save(Product product);

    Optional<Product> findById(UUID id);

    List<Product> findAll();

    void update(Product product);

    void delete(UUID id);

    IPage<Product> findPage(int pageNum, int pageSize);

}
