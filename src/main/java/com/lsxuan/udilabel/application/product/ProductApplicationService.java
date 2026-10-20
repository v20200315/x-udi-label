package com.lsxuan.udilabel.application.product;

import com.lsxuan.udilabel.domain.product.Product;
import com.lsxuan.udilabel.domain.product.ProductRepository;
import com.lsxuan.udilabel.infrastructure.security.CurrentUserContext;
import com.lsxuan.udilabel.interfaces.api.v1.product.dto.CreateProductRequest;
import com.lsxuan.udilabel.interfaces.api.v1.product.dto.ProductResponse;
import com.lsxuan.udilabel.interfaces.api.v1.product.dto.UpdateProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ProductApplicationService {

    private final ProductRepository productRepository;
    private final CurrentUserContext currentUserContext;

    public ProductApplicationService(ProductRepository productRepository, CurrentUserContext currentUserContext) {

        this.productRepository = productRepository;
        this.currentUserContext = currentUserContext;
    }

    @Transactional
    public ProductResponse create(CreateProductRequest request) {

        UUID userId = currentUserContext.userId();

        Product product = Product.create(request.indicatorDigit(), request.companyPrefix(), request.itemReference(),
            request.lotNumber(), request.name(), request.specification(), request.model(), request.productionDate(),
            request.expirationDate(), userId);
        productRepository.save(product);

        return ProductResponse.from(product);
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(UUID id) {

        Product product =
            productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product does not exist."));

        return ProductResponse.from(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {

        return productRepository.findAll().stream().map(ProductResponse::from).toList();
    }

    @Transactional
    public ProductResponse update(UUID id, UpdateProductRequest request) {

        UUID userId = currentUserContext.userId();

        Product product =
            productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product does not exist."));

        product.updateBasicInfo(request.indicatorDigit(), request.companyPrefix(), request.itemReference(),
            request.lotNumber(), request.name(), request.specification(), request.model(), request.productionDate(),
            request.expirationDate(), userId);
        productRepository.update(product);

        return ProductResponse.from(product);
    }

    @Transactional
    public void delete(UUID id) {

        Product product =
            productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product does not exist."));
        product.deactivate(currentUserContext.userId());

        productRepository.update(product);
    }

}

