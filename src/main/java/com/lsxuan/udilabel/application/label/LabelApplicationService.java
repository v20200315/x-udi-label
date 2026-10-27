package com.lsxuan.udilabel.application.label;

import com.lsxuan.udilabel.domain.label.CodeType;
import com.lsxuan.udilabel.domain.label.LabelTemplate;
import com.lsxuan.udilabel.domain.product.Product;
import com.lsxuan.udilabel.domain.product.ProductRepository;
import com.lsxuan.udilabel.infrastructure.codesymbol.CodeSymbolImageGenerator;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.svgsupport.BatikSVGDrawer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

@Service
public class LabelApplicationService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyMMdd");

    private final TemplateEngine templateEngine;
    private final ProductRepository productRepository;
    private final CodeSymbolImageGenerator codeSymbolImageGenerator;

    public LabelApplicationService(TemplateEngine templateEngine, ProductRepository productRepository,
        @Qualifier("zxingCodeSymbolImageGenerator") CodeSymbolImageGenerator codeSymbolImageGenerator) {

        this.templateEngine = templateEngine;
        this.productRepository = productRepository;
        this.codeSymbolImageGenerator = codeSymbolImageGenerator;
    }

    public byte[] generatePdf(UUID productId, String templateCode) {
        LabelGenerationContext context = prepareLabel(productId, templateCode);

        String barcodeSvg = normalizeSvg(context.svgBytes());
        String html = renderHtml(context.template(), barcodeSvg, context.displayText());

        return renderPdf(html);
    }

    public byte[] generateSvg(UUID productId, String templateCode) {
        LabelGenerationContext context = prepareLabel(productId, templateCode);
        return context.svgBytes();
    }

    private LabelGenerationContext prepareLabel(UUID productId, String templateCode) {

        LabelTemplate template = LabelTemplate.fromCode(templateCode);

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product does not exist: " + productId));

        validateRequiredDates(product, template);

        String codeContent = buildCodeContent(product, template);
        String displayText = buildDisplayText(product, template);

        byte[] svgBytes = codeSymbolImageGenerator.generate(template, codeContent);

        return new LabelGenerationContext(template, svgBytes, displayText);
    }

    private record LabelGenerationContext(LabelTemplate template, byte[] svgBytes, String displayText) {
    }

    private String buildCodeContent(Product product, LabelTemplate template) {

        StringBuilder codeContent = new StringBuilder();

        codeContent.append("(01)");
        codeContent.append(product.getIndicatorDigit());
        codeContent.append(product.getCompanyPrefix());
        codeContent.append(product.getItemReference());
        codeContent.append(product.getCheckDigit());

        if (template.isIncludeProductionDate()) {
            codeContent.append("(11)");
            codeContent.append(formatDate(product.getProductionDate()));
        }

        if (template.isIncludeExpirationDate()) {
            codeContent.append("(17)");
            codeContent.append(formatDate(product.getExpirationDate()));
        }

        if (template.isIncludeLotNumber()) {
            codeContent.append("(10)");
            codeContent.append(product.getLotNumber());
        }

        return codeContent.toString();
    }

    private String buildDisplayText(Product product, LabelTemplate template) {
        StringBuilder text = new StringBuilder();

        if (template.getCodeType().equals(CodeType.GS1_128)) {
            text.append("(01)&#160;");
            text.append(product.getIndicatorDigit());
            text.append(product.getCompanyPrefix());
            text.append(product.getItemReference());
            text.append(product.getCheckDigit());
            text.append("&#160;");

            if (template.isIncludeProductionDate()) {
                text.append("(11)&#160;");
                text.append(formatDate(product.getProductionDate()));
                text.append("&#160;");
            }

            if (template.isIncludeExpirationDate()) {
                text.append("(17)&#160;");
                text.append(formatDate(product.getExpirationDate()));
                text.append("&#160;");
            }

            if (template.isIncludeLotNumber()) {
                text.append("(10)&#160;");
                text.append(product.getLotNumber());
                text.append("&#160;");
            }
        }

        if (template.getCodeType().equals(CodeType.GS1_DATA_MATRIX)) {
            text.append("<p>(01)&#160;");
            text.append(product.getIndicatorDigit());
            text.append(product.getCompanyPrefix());
            text.append(product.getItemReference());
            text.append(product.getCheckDigit());
            text.append("</p>");

            if (template.isIncludeProductionDate()) {
                text.append("<p>(11)&#160;");
                text.append(formatDate(product.getProductionDate()));
                text.append("</p>");
            }

            if (template.isIncludeExpirationDate()) {
                text.append("<p>(17)&#160;");
                text.append(formatDate(product.getExpirationDate()));
                text.append("</p>");
            }

            if (template.isIncludeLotNumber()) {
                text.append("<p>(10)&#160;");
                text.append(product.getLotNumber());
                text.append("</p>");
            }
        }

        return text.toString();
    }

    private void validateRequiredDates(Product product, LabelTemplate template) {

        if (template.isIncludeProductionDate() && product.getProductionDate() == null) {
            throw new IllegalArgumentException("Production date is required by the selected template.");
        }

        if (template.isIncludeExpirationDate() && product.getExpirationDate() == null) {
            throw new IllegalArgumentException("Expiration date is required by the selected template.");
        }
    }

    private String formatDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }

    private String normalizeSvg(byte[] svgBytes) {
        String svg = new String(svgBytes, StandardCharsets.UTF_8);

        // 移除 SVG 开头的 XML 声明，避免嵌入 HTML 时触发 XML 解析错误。
        return svg.replaceFirst("^\\uFEFF?\\s*<\\?xml[^?]*\\?>\\s*", "");
    }

    private String renderHtml(LabelTemplate template, String barcodeSvg, String displayText) {

        Context context = new Context(Locale.SIMPLIFIED_CHINESE);
        context.setVariable("barcodeSvg", barcodeSvg);
        context.setVariable("displayText", displayText);

        return templateEngine.process(template.getHtmlTemplate(), context);
    }

    private byte[] renderPdf(String html) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useSVGDrawer(new BatikSVGDrawer());
            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new IllegalStateException("Failed to render label PDF.", e);
        }
    }
}