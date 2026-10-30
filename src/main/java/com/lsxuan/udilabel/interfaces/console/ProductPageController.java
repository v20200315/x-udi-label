package com.lsxuan.udilabel.interfaces.console;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lsxuan.udilabel.application.label.LabelApplicationService;
import com.lsxuan.udilabel.application.product.ProductApplicationService;
import com.lsxuan.udilabel.interfaces.api.v1.product.dto.CreateProductRequest;
import com.lsxuan.udilabel.interfaces.api.v1.product.dto.ProductResponse;
import com.lsxuan.udilabel.interfaces.api.v1.product.dto.UpdateProductRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProductPageController {

    private final ProductApplicationService productApplicationService;
    private final LabelApplicationService labelApplicationService;

    @GetMapping("/product")
    public String productListPage(Model model) {

        setPageMetadata(model, "产品列表");

        model.addAttribute("products", productApplicationService.findAll());

        return "console/product";
    }

    @GetMapping("/product-table-with-page")
    public String productListPage2(@RequestParam(defaultValue = "1") int pageNum,
        @RequestParam(defaultValue = "10") int pageSize, Model model) {

        pageNum = Math.max(pageNum, 1);
        pageSize = Math.clamp(pageSize, 1, 100);

        IPage<ProductResponse> page = productApplicationService.findPage(pageNum, pageSize);

        setPageMetadata(model, "产品列表");

        model.addAttribute("products", page.getRecords());

        model.addAttribute("pageNum", page.getCurrent());
        model.addAttribute("pageSize", page.getSize());
        model.addAttribute("total", page.getTotal());
        model.addAttribute("totalPages", page.getPages());

        return "console/product-table-with-page";
    }

    @GetMapping("/product/new")
    public String newProductPage(Model model) {

        setPageMetadata(model, "新增产品");
        model.addAttribute("formAction", "/product/new");
        model.addAttribute("submitLabel", "新增产品");
        model.addAttribute("productForm", ProductForm.empty());

        return "console/product-form";
    }

    @PostMapping("/product/new")
    public String createProduct(@Valid @ModelAttribute("productForm") ProductForm form, BindingResult bindingResult,
        Model model, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasFieldErrors("validItemReferenceLength")) {
            bindingResult.rejectValue("itemReference", "itemReference.length.invalid",
                "商品项目代码的位数与厂商识别码的位数之和应为12");
        }

        if (bindingResult.hasFieldErrors("validDateRange")) {
            bindingResult.rejectValue("expirationDate", "expirationDate.invalid", "有效期截止日期不能早于生产日期");
        }

        if (bindingResult.hasErrors()) {
            setPageMetadata(model, "新增产品");
            model.addAttribute("formAction", "/product/new");
            model.addAttribute("submitLabel", "新增产品");
            return "console/product-form";
        }

        CreateProductRequest request = toCreateProductRequest(form);
        productApplicationService.create(request);

        redirectAttributes.addFlashAttribute("successMessage", "产品创建成功");

        return "redirect:/product";
    }

    @GetMapping("/product/{productId}/edit")
    public String editProductPage(@PathVariable UUID productId, Model model) {

        ProductResponse response = productApplicationService.findById(productId);

        setPageMetadata(model, "编辑产品");
        model.addAttribute("formAction", "/product/" + productId + "/edit");
        model.addAttribute("submitLabel", "保存修改");
        model.addAttribute("productForm", toProductForm(response));

        return "console/product-form";
    }

    @PostMapping("/product/{productId}/edit")
    public String updateProduct(@PathVariable UUID productId,
        @Valid @ModelAttribute("productForm") ProductForm productForm, BindingResult bindingResult, Model model,
        RedirectAttributes redirectAttributes) {

        if (bindingResult.hasFieldErrors("validItemReferenceLength")) {
            bindingResult.rejectValue("itemReference", "itemReference.length.invalid",
                "商品项目代码的位数与厂商识别码的位数之和应为12");
        }

        if (bindingResult.hasFieldErrors("validDateRange")) {
            bindingResult.rejectValue("expirationDate", "expirationDate.invalid", "有效期截止日期不能早于生产日期");
        }

        if (bindingResult.hasErrors()) {
            setPageMetadata(model, "编辑产品");
            model.addAttribute("formAction", "/product/" + productId + "/edit");
            model.addAttribute("submitLabel", "保存修改");
            return "console/product-form";
        }

        UpdateProductRequest request = toUpdateProductRequest(productForm);
        productApplicationService.update(productId, request);

        redirectAttributes.addFlashAttribute("successMessage", "产品修改成功");

        return "redirect:/product";
    }

    @GetMapping("/product/{productId}/label/generate")
    public String generateLabelPage(@PathVariable UUID productId, Model model) {

        ProductResponse response = productApplicationService.findById(productId);

        setPageMetadata(model, "生成标签");
        model.addAttribute("productId", productId);
        model.addAttribute("productForm", toProductForm(response));

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
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).header(HttpHeaders.CONTENT_DISPOSITION,
            ContentDisposition.attachment().filename(fileName).build().toString()).body(pdfBytes);
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

    private CreateProductRequest toCreateProductRequest(ProductForm form) {

        return new CreateProductRequest(form.getIndicatorDigit(), form.getCompanyPrefix(), form.getItemReference(),
            form.getLotNumber(), form.getName(), form.getSpecification(), form.getModel(), form.getProductionDate(),
            form.getExpirationDate());
    }

    private UpdateProductRequest toUpdateProductRequest(ProductForm form) {

        return new UpdateProductRequest(form.getIndicatorDigit(), form.getCompanyPrefix(), form.getItemReference(),
            form.getLotNumber(), form.getName(), form.getSpecification(), form.getModel(), form.getProductionDate(),
            form.getExpirationDate());
    }

    private ProductForm toProductForm(ProductResponse response) {

        ProductForm form = new ProductForm();
        form.setIndicatorDigit(response.indicatorDigit());
        form.setCompanyPrefix(response.companyPrefix());
        form.setItemReference(response.itemReference());
        form.setCheckDigit(response.checkDigit());
        form.setLotNumber(response.lotNumber());
        form.setName(response.name());
        form.setSpecification(response.specification());
        form.setModel(response.model());
        form.setProductionDate(response.productionDate());
        form.setExpirationDate(response.expirationDate());
        return form;
    }
}
