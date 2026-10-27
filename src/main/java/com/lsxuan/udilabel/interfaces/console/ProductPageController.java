package com.lsxuan.udilabel.interfaces.console;

import com.lsxuan.udilabel.application.label.LabelApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProductPageController {

    private final LabelApplicationService labelApplicationService;

    @GetMapping("/product")
    public String productListPage(Model model) {

        setPageMetadata(model, "产品列表");
        // TODO 查询产品列表并放入 Model。
        return "console/product";
    }

    @GetMapping("/product/new")
    public String newProductPage(Model model) {

        setPageMetadata(model, "添加产品");
        // TODO 初始化空的产品表单对象。
        return "console/product-form";
    }

    @GetMapping("/product/{productId}/edit")
    public String editProductPage(@PathVariable UUID productId, Model model) {

        setPageMetadata(model, "编辑产品");
        model.addAttribute("productId", productId);
        // TODO 根据 id 查询产品，并放入 Model。
        return "console/product-form";
    }

    @GetMapping("/product/{productId}/label/generate")
    public String generateLabelPage(@PathVariable UUID productId, Model model) {

        setPageMetadata(model, "生成标签");
        model.addAttribute("productId", productId);
        // TODO 根据 id 查询产品，并放入 Model。
        return "console/product-label";
    }

    @PostMapping("/product/label/generate")
    public String generateLabel(@RequestParam UUID productId, @RequestParam String templateCode, Model model) {

        model.addAttribute("previewUrl", "/product/" + productId + "/labels/preview?templateCode=" + templateCode);
        model.addAttribute("downloadUrl", "/product/" + productId + "/labels/download?templateCode=" + templateCode);
        model.addAttribute("svgDownloadUrl",
            "/product/" + productId + "/labels/download.svg?templateCode=" + templateCode);
        return "console/label/fragments :: result";
    }

    @GetMapping("/product/{productId}/labels/preview")
    public ResponseEntity<byte[]> previewLabel(@PathVariable UUID productId, @RequestParam String templateCode) {

        byte[] pdfBytes = labelApplicationService.generatePdf(productId, templateCode);
        String fileName = "product-label-" + System.currentTimeMillis() + ".pdf";
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
            .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.inline().filename(fileName).build().toString())
            .body(pdfBytes);
    }

    @GetMapping("/product/{productId}/labels/download")
    public ResponseEntity<byte[]> downloadLabel(@PathVariable UUID productId, @RequestParam String templateCode) {

        byte[] pdfBytes = labelApplicationService.generatePdf(productId, templateCode);
        String fileName = "product-label-" + System.currentTimeMillis() + ".pdf";
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
            .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(fileName).build().toString())
            .body(pdfBytes);
    }

    @GetMapping("/product/{productId}/labels/download.svg")
    public ResponseEntity<byte[]> downloadSvg(@PathVariable UUID productId, @RequestParam String templateCode) {

        byte[] svgBytes = labelApplicationService.generateSvg(productId, templateCode);
        String fileName = "product-label-" + System.currentTimeMillis() + ".svg";
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("image/svg+xml"))
            .header(HttpHeaders.CONTENT_DISPOSITION,
                ContentDisposition.attachment().filename(fileName).build().toString()).body(svgBytes);
    }

    private void setPageMetadata(Model model, String pageSubTitle) {

        model.addAttribute("pageTitle", "产品管理");
        model.addAttribute("pageSubTitle", pageSubTitle);
        model.addAttribute("activePage", "product");
    }
}
