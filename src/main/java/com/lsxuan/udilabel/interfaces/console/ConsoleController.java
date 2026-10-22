package com.lsxuan.udilabel.interfaces.console;

import com.lsxuan.udilabel.application.label.LabelApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ConsoleController {

    private final LabelApplicationService labelApplicationService;

    @GetMapping({"/", "/index", "/console"})
    public String index() {

        return "console/index";
    }

    @GetMapping("/product")
    public String productList(Model model) {

        model.addAttribute("pageTitle", "产品管理");
        model.addAttribute("pageSubTitle", "产品列表");
        model.addAttribute("activePage", "product");
        // Add list data for rendering
        return "console/product";
    }

    @GetMapping("/product/new")
    public String newProduct(Model model) {

        model.addAttribute("pageTitle", "产品管理");
        model.addAttribute("pageSubTitle", "添加产品");
        model.addAttribute("activePage", "product");
        // Add an empty form model
        return "console/product-form";
    }

    @GetMapping("/product/{id}/edit")
    public String editProduct(@PathVariable UUID id, Model model) {

        model.addAttribute("pageTitle", "产品管理");
        model.addAttribute("pageSubTitle", "编辑产品");
        model.addAttribute("activePage", "product");
        model.addAttribute("productId", id);
        // Add the selected product as the form model
        return "console/product-form";
    }

    @GetMapping("/product/{id}/labels/generate")
    public String generateLabelPage(@PathVariable UUID id, Model model) {

        model.addAttribute("pageTitle", "产品管理");
        model.addAttribute("pageSubTitle", "生成标签");
        model.addAttribute("activePage", "product");
        model.addAttribute("productId", id);
        return "console/product-label";
    }

    @GetMapping("/udi")
    public String udi() {

        return "console/udi";
    }

    @GetMapping({"/index2"})
    public String index2() {

        return "console/index2";
    }

    @PostMapping("/console/labels/generate")
    public String generateLabel(
        @RequestParam UUID productId,
        @RequestParam String templateCode,
        Model model) {

        try {
            byte[] svgBytes =  labelApplicationService.generate(
                productId, templateCode
            );

            String svg = new String(svgBytes, StandardCharsets.UTF_8);
            model.addAttribute("svg", svg);

        } catch (Exception e) {
            model.addAttribute("error", "标签生成失败，请稍后重试");
        }

        return "console/label/fragments :: result";
    }

}
