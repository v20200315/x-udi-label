package com.lsxuan.udilabel.interfaces.api.v1.product;

import com.lsxuan.udilabel.application.product.ProductApplicationService;
import com.lsxuan.udilabel.interfaces.api.v1.product.dto.CreateProductRequest;
import com.lsxuan.udilabel.interfaces.api.v1.product.dto.ProductResponse;
import com.lsxuan.udilabel.interfaces.api.v1.product.dto.UpdateProductRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductApplicationService productApplicationService;

    public ProductController(ProductApplicationService productApplicationService) {

        this.productApplicationService = productApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@Valid @RequestBody CreateProductRequest request) {

        return productApplicationService.create(request);
    }

    @GetMapping("/{id}")
    public ProductResponse findById(@PathVariable UUID id) {

        return productApplicationService.findById(id);
    }

    @GetMapping
    public List<ProductResponse> findAll() {

        return productApplicationService.findAll();
    }

    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable UUID id, @Valid @RequestBody UpdateProductRequest request) {

        return productApplicationService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {

        productApplicationService.delete(id);
    }
}
