package com.lsxuan.udilabel.interfaces.console;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Controller
public class ConsoleController {

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

    @GetMapping("/udi")
    public String udi() {

        return "console/udi";
    }

    @GetMapping({"/index2"})
    public String index2() {

        return "console/index2";
    }

}
