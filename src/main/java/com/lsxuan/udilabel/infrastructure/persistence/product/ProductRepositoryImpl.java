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

    private final ProductMapper productMapper;

    public ProductRepositoryImpl(ProductMapper productMapper) {

        this.productMapper = productMapper;
    }

    @Override
    public void save(Product product) {

        productMapper.insert(toDO(product));
    }

    @Override
    public Optional<Product> findById(UUID id) {

        ProductDO productDO = productMapper.selectById(id);
        return Optional.ofNullable(productDO).map(this::toDomain);
    }

    @Override
    public List<Product> findAll() {

        return productMapper.selectList(new LambdaQueryWrapper<ProductDO>()).stream().map(this::toDomain).toList();
    }

    @Override
    public void update(Product product) {

        productMapper.updateById(toDO(product));
    }

    @Override
    public void delete(UUID id) {

        productMapper.delete(new LambdaQueryWrapper<ProductDO>().eq(ProductDO::getId, id));
    }

    @Override
    public IPage<Product> findPage(int pageNum, int pageSize) {

        LambdaQueryWrapper<ProductDO> qw = new LambdaQueryWrapper<>();
        qw.orderByDesc(ProductDO::getUpdatedAt);

        Page<ProductDO> page = new Page<>(pageNum, pageSize);
        IPage<ProductDO> iPage = productMapper.selectPage(page, qw);
        return iPage.convert(this::toDomain);
    }

    private ProductDO toDO(Product product) {

        ProductDO productDO = new ProductDO();
        productDO.setId(product.getId());
        productDO.setIndicatorDigit(product.getIndicatorDigit());
        productDO.setCompanyPrefix(product.getCompanyPrefix());
        productDO.setItemReference(product.getItemReference());
        productDO.setCheckDigit(product.getCheckDigit());
        productDO.setLotNumber(product.getLotNumber());
        productDO.setName(product.getName());
        productDO.setSpecification(product.getSpecification());
        productDO.setModel(product.getModel());
        productDO.setProductionDate(product.getProductionDate());
        productDO.setExpirationDate(product.getExpirationDate());
        productDO.setStatus(product.getStatus());
        productDO.setCreatedAt(product.getCreatedAt());
        productDO.setCreatedBy(product.getCreatedBy());
        productDO.setUpdatedAt(product.getUpdatedAt());
        productDO.setUpdatedBy(product.getUpdatedBy());

        return productDO;
    }

    private Product toDomain(ProductDO productDO) {

        return Product.reconstitute(productDO.getId(), productDO.getIndicatorDigit(), productDO.getCompanyPrefix(),
            productDO.getItemReference(), productDO.getCheckDigit(), productDO.getLotNumber(), productDO.getName(),
            productDO.getSpecification(), productDO.getModel(), productDO.getProductionDate(),
            productDO.getExpirationDate(), productDO.getStatus(), productDO.getCreatedAt(), productDO.getCreatedBy(),
            productDO.getUpdatedAt(), productDO.getUpdatedBy());
    }
}
