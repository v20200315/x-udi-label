package com.lsxuan.udilabel.interfaces.console;

import com.lsxuan.udilabel.application.label.LabelApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/product/{id}/edit")
    public String editProductPage(@PathVariable UUID id, Model model) {

        setPageMetadata(model, "编辑产品");
        model.addAttribute("productId", id);
        // TODO 根据 id 查询产品，并放入 Model。
        return "console/product-form";
    }

    @GetMapping("/product/{id}/labels/generate")
    public String generateLabelPage(@PathVariable UUID id, Model model) {

        setPageMetadata(model, "生成标签");
        model.addAttribute("productId", id);
        // TODO 根据 id 查询产品，并放入 Model。
        return "console/product-label";
    }

    @PostMapping("/console/labels/generate")
    public String generateLabel(@RequestParam UUID productId, @RequestParam String templateCode, Model model) {

        // Mock：模拟标签生成成功
        String labelId = "mock-label-001";

        model.addAttribute("labelId", labelId);
        model.addAttribute("fileName", "product-label.pdf");
        model.addAttribute("previewUrl", "/console/labels/" + labelId + "/preview");
        model.addAttribute("downloadUrl", "/console/labels/" + labelId + "/download");

        return "console/label/fragments :: result";
    }

    /**
     * 预览标签 PDF。
     */
    @GetMapping("/console/labels/{labelId}/preview")
    public ResponseEntity<byte[]> previewLabel(@PathVariable String labelId) {

        String productId = "01a1585c-2780-7e76-929a-14edcc0cebc2";
        String templateCode = "BARCODE_10_11_17";
        byte[] pdfBytes = labelApplicationService.generatePdf(productId, templateCode);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"product-label.pdf\"").body(pdfBytes);
    }

    /**
     * 下载标签 PDF。
     */
    @GetMapping("/console/labels/{labelId}/download")
    public ResponseEntity<byte[]> downloadLabel(@PathVariable String labelId) {

        String productId = "01a1585c-2780-7e76-929a-14edcc0cebc2";
        String templateCode = "BARCODE_10_11_17";
        byte[] pdfBytes = labelApplicationService.generatePdf(productId, templateCode);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"product-label.pdf\"").body(pdfBytes);
    }

    private void setPageMetadata(Model model, String pageSubTitle) {

        model.addAttribute("pageTitle", "产品管理");
        model.addAttribute("pageSubTitle", pageSubTitle);
        model.addAttribute("activePage", "product");
    }
}
