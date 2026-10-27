package com.lsxuan.udilabel.application.label;

import com.lsxuan.udilabel.domain.label.CodeType;
import com.lsxuan.udilabel.domain.label.LabelCodeContentFactory;
import com.lsxuan.udilabel.domain.label.LabelRepository;
import com.lsxuan.udilabel.domain.product.Product;
import com.lsxuan.udilabel.domain.product.ProductRepository;
import com.lsxuan.udilabel.infrastructure.codesymbol.CodeSymbolImageGenerator;
import com.lsxuan.udilabel.infrastructure.security.CurrentUserContext;
import com.lsxuan.udilabel.infrastructure.storage.FileStorage;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.svgsupport.BatikSVGDrawer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.UUID;

@Service
public class LabelApplicationService {

    private final TemplateEngine templateEngine;
    private final LabelRepository labelRepository;
    private final ProductRepository productRepository;
    private final LabelCodeContentFactory labelCodeContentFactory;
    private final CodeSymbolImageGenerator codeSymbolImageGenerator;
    private final FileStorage fileStorage;
    private final CurrentUserContext currentUserContext;

//    private static final String DISPLAY_TEXT = "(01)06979510980000(11)260304(10)ADF233";
    private static final String DISPLAY_TEXT = "(01)06979510980000";

    public LabelApplicationService(TemplateEngine templateEngine, LabelRepository labelRepository,
        ProductRepository productRepository, LabelCodeContentFactory labelCodeContentFactory,
        @Qualifier("zxingCodeSymbolImageGenerator") CodeSymbolImageGenerator codeSymbolImageGenerator,
        FileStorage fileStorage, CurrentUserContext currentUserContext) {

        this.templateEngine = templateEngine;
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

    public byte[] generatePdf(String productId, String templateCode) {
        try {
            // 1. 暂时使用固定的条码内容和展示字符串
            String displayText = DISPLAY_TEXT;

            // 2. 生成条码 SVG
            byte[] svgBytes = codeSymbolImageGenerator.generate(CodeType.GS1_128, displayText);

            String barcodeSvg = new String(svgBytes, StandardCharsets.UTF_8);

            // 关键：移除 SVG 的 XML 声明，避免嵌入 HTML 后导致 XML 解析失败
            barcodeSvg = barcodeSvg.replaceFirst("^\\s*<\\?xml[^?]*\\?>", "");

            // 3. 渲染 HTML 模板
            Context context = new Context(Locale.SIMPLIFIED_CHINESE);
            context.setVariable("barcodeSvg", barcodeSvg);
            context.setVariable("displayText", displayText);

            String html = templateEngine.process("pdf/barcode-label", context);

            // 4. HTML 转 PDF
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                PdfRendererBuilder builder = new PdfRendererBuilder();
                builder.useSVGDrawer(new BatikSVGDrawer());
                builder.withHtmlContent(html, null);
                builder.toStream(outputStream);
                builder.run();

                return outputStream.toByteArray();
            }
        } catch (Exception e) {
            throw new IllegalStateException("生成标签 PDF 失败", e);
        }
    }

    public byte[] generatePdf2(String productId, String templateCode) {
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
