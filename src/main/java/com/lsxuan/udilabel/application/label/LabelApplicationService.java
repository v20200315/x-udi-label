package com.lsxuan.udilabel.application.label;

import com.lsxuan.udilabel.domain.label.Label;
import com.lsxuan.udilabel.domain.label.LabelCodeContentFactory;
import com.lsxuan.udilabel.domain.label.LabelRepository;
import com.lsxuan.udilabel.domain.product.Product;
import com.lsxuan.udilabel.domain.product.ProductRepository;
import com.lsxuan.udilabel.infrastructure.codesymbol.CodeSymbolImageGenerator;
import com.lsxuan.udilabel.infrastructure.security.CurrentUserContext;
import com.lsxuan.udilabel.infrastructure.storage.FileStorage;
import com.lsxuan.udilabel.interfaces.api.v1.label.dto.CreateLabelRequest;
import com.lsxuan.udilabel.interfaces.api.v1.label.dto.LabelResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class LabelApplicationService {

    private final LabelRepository labelRepository;
    private final ProductRepository productRepository;
    private final LabelCodeContentFactory labelCodeContentFactory;
    private final CodeSymbolImageGenerator codeSymbolImageGenerator;
    private final FileStorage fileStorage;
    private final CurrentUserContext currentUserContext;

    public LabelApplicationService(LabelRepository labelRepository, ProductRepository productRepository,
        LabelCodeContentFactory labelCodeContentFactory,
        @Qualifier("zxingCodeSymbolImageGenerator") CodeSymbolImageGenerator codeSymbolImageGenerator,
        FileStorage fileStorage, CurrentUserContext currentUserContext) {

        this.labelRepository = labelRepository;
        this.productRepository = productRepository;
        this.labelCodeContentFactory = labelCodeContentFactory;
        this.codeSymbolImageGenerator = codeSymbolImageGenerator;
        this.fileStorage = fileStorage;
        this.currentUserContext = currentUserContext;
    }

    @Transactional
    public LabelResponse create(CreateLabelRequest request) {

        Product product = productRepository.findById(request.productId())
            .orElseThrow(() -> new ProductNotFoundException("Product not found: " + request.productId()));

        String codeContent = labelCodeContentFactory.generate(request.codeType(), product);

        byte[] imageBytes = codeSymbolImageGenerator.generate(request.codeType(), codeContent);

        String imageUrl = fileStorage.upload(imageBytes, "labels/" + UUID.randomUUID() + ".svg");

        UUID userId = currentUserContext.userId();
        String udiDi =
            product.getIndicatorDigit() + product.getCompanyPrefix() + product.getItemReference() + product.getCheckDigit();
        Label label =
            Label.create(request.productId(), request.name(), product.getName(), udiDi, request.codeType(), codeContent,
                imageUrl, userId);
        labelRepository.save(label);

        return LabelResponse.from(label);
    }
}
