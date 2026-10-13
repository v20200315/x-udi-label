package com.lsxuan.udilabel.domain.product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    void save(Product product);

    Optional<Product> findById(UUID id);

    List<Product> findAll();

    void update(Product product);

    void delete(UUID id);

}
