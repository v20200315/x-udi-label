package com.lsxuan.udilabel.interfaces.console;

import com.lsxuan.udilabel.application.label.LabelApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.charset.StandardCharsets;
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

        try {
            byte[] svgBytes = labelApplicationService.generate(productId, templateCode);
            String svg = new String(svgBytes, StandardCharsets.UTF_8);
            model.addAttribute("svg", svg);
        } catch (Exception e) {
            log.error("Failed to generate label preview. productId={}, templateCode={}", productId, templateCode, e);
            model.addAttribute("error", "标签生成失败，请稍后重试");
        }
        return "console/label/fragments :: result";
    }

    private void setPageMetadata(Model model, String pageSubTitle) {
        model.addAttribute("pageTitle", "产品管理");
        model.addAttribute("pageSubTitle", pageSubTitle);
        model.addAttribute("activePage", "product");
    }
}
