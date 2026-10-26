package com.lsxuan.udilabel.application.label;

import com.lsxuan.udilabel.domain.label.CodeType;
import com.lsxuan.udilabel.domain.label.LabelCodeContentFactory;
import com.lsxuan.udilabel.domain.label.LabelRepository;
import com.lsxuan.udilabel.domain.product.Product;
import com.lsxuan.udilabel.domain.product.ProductRepository;
import com.lsxuan.udilabel.infrastructure.codesymbol.CodeSymbolImageGenerator;
import com.lsxuan.udilabel.infrastructure.security.CurrentUserContext;
import com.lsxuan.udilabel.infrastructure.storage.FileStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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

    @Transactional(readOnly = true)
    public byte[] generate(UUID productId, String templateCode) {

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product does not exist."));

        CodeType codeType = resolveCodeType(templateCode);
        String codeContent = labelCodeContentFactory.generate(codeType, product);

        return codeSymbolImageGenerator.generate(codeType, codeContent);
    }

    public byte[] generatePdf() {
        try {
            ClassPathResource resource = new ClassPathResource("mock/mock.pdf");

            try (var inputStream = resource.getInputStream()) {
                return inputStream.readAllBytes();
            }
        } catch (IOException e) {
            throw new IllegalStateException("生成标签 PDF 失败", e);
        }
    }

    private CodeType resolveCodeType(String templateCode) {

        return CodeType.GS1_DATA_MATRIX;
    }

}
