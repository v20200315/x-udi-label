package com.lsxuan.udilabel.application.label;

import com.lsxuan.udilabel.domain.label.Label;
import com.lsxuan.udilabel.domain.label.LabelRepository;
import com.lsxuan.udilabel.domain.product.Product;
import com.lsxuan.udilabel.domain.product.ProductRepository;
import com.lsxuan.udilabel.infrastructure.security.CurrentUserContext;
import com.lsxuan.udilabel.interfaces.api.v1.label.dto.CreateLabelRequest;
import com.lsxuan.udilabel.interfaces.api.v1.label.dto.LabelResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class LabelApplicationService {

    private final LabelRepository labelRepository;
    private final ProductRepository productRepository;
    private final CurrentUserContext currentUserContext;

    public LabelApplicationService(LabelRepository labelRepository, ProductRepository productRepository,
        CurrentUserContext currentUserContext) {

        this.labelRepository = labelRepository;
        this.productRepository = productRepository;
        this.currentUserContext = currentUserContext;
    }

    @Transactional
    public LabelResponse create(CreateLabelRequest request) {

        Product product = productRepository.findById(request.productId())
            .orElseThrow(() -> new ProductNotFoundException("Product not found: " + request.productId()));

        UUID userId = currentUserContext.userId();
        Label label =
            Label.create(request.productId(), request.name(), product.getName(), product.getUdiDi(), request.codeType(),
                userId);
        labelRepository.save(label);

        return LabelResponse.from(label);
    }
}
