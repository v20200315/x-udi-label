package com.lsxuan.udilabel.infrastructure.persistence.product;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lsxuan.udilabel.domain.product.Product;
import com.lsxuan.udilabel.domain.product.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductMapper mapper;

    public ProductRepositoryImpl(ProductMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void save(Product product) {

        mapper.insert(toDO(product));
    }

    @Override
    public Optional<Product> findById(UUID id) {

        ProductDO data = mapper.selectById(id);

        if (data == null) {
            return Optional.empty();
        }

        return Optional.of(toDomain(data));
    }

    @Override
    public List<Product> findAll() {

        return mapper.selectList(new LambdaQueryWrapper<ProductDO>()).stream().map(this::toDomain).toList();
    }

    @Override
    public void update(Product product) {

        mapper.updateById(toDO(product));
    }

    @Override
    public void delete(UUID id) {

        mapper.delete(new LambdaQueryWrapper<ProductDO>().eq(ProductDO::getId, id));
    }

    @Override
    public IPage<Product> findPage(int pageNum, int pageSize) {

        LambdaQueryWrapper<ProductDO> qw = new LambdaQueryWrapper<>();
        qw.orderByDesc(ProductDO::getUpdatedAt);

        Page<ProductDO> page = new Page<>(pageNum, pageSize);
        IPage<ProductDO> iPage = mapper.selectPage(page, qw);
        return iPage.convert(this::toDomain);
    }

    private ProductDO toDO(Product product) {

        ProductDO data = new ProductDO();
        data.setId(product.getId());
        data.setIndicatorDigit(product.getIndicatorDigit());
        data.setCompanyPrefix(product.getCompanyPrefix());
        data.setItemReference(product.getItemReference());
        data.setCheckDigit(product.getCheckDigit());
        data.setLotNumber(product.getLotNumber());
        data.setName(product.getName());
        data.setSpecification(product.getSpecification());
        data.setModel(product.getModel());
        data.setProductionDate(product.getProductionDate());
        data.setExpirationDate(product.getExpirationDate());
        data.setStatus(product.getStatus());
        data.setCreatedAt(product.getCreatedAt());
        data.setCreatedBy(product.getCreatedBy());
        data.setUpdatedAt(product.getUpdatedAt());
        data.setUpdatedBy(product.getUpdatedBy());

        return data;
    }

    private Product toDomain(ProductDO data) {

        return Product.reconstitute(data.getId(), data.getIndicatorDigit(), data.getCompanyPrefix(),
            data.getItemReference(), data.getCheckDigit(), data.getLotNumber(), data.getName(), data.getSpecification(),
            data.getModel(), data.getProductionDate(), data.getExpirationDate(), data.getStatus(), data.getCreatedAt(),
            data.getCreatedBy(), data.getUpdatedAt(), data.getUpdatedBy());
    }
}
