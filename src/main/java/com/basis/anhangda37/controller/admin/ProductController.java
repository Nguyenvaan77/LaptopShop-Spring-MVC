package com.basis.anhangda37.controller.admin;

import java.util.List;

import org.aspectj.util.LangUtil.ProcessController;
import org.springframework.data.domain.jaxb.SortAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.basis.anhangda37.domain.Product;
import com.basis.anhangda37.domain.User;
import com.basis.anhangda37.repository.ProductRepository;
import com.basis.anhangda37.service.ProductService;
import com.basis.anhangda37.service.UploadService;

import jakarta.validation.Valid;

@Controller
public class ProductController {
    private final ProductService productService;
    private final UploadService uploadService;

    public ProductController(ProductService productService, UploadService uploadService) {
        this.productService = productService;
        this.uploadService = uploadService;
    }

    @GetMapping("/admin/product")
    public String getDashboard(Model model) {
        List<Product> products = productService.getAllProduct();
        model.addAttribute("products", products);
        return "admin/product/show";
    }

    @GetMapping("/admin/product/{id}")
    public String getProductDetailPage(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("id", id);
        model.addAttribute("product", product);
        return "admin/product/detail";
    }

    @GetMapping("/admin/product/create")
    public String routeProductTable(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }

    @PostMapping("/admin/product/create")
    public String getNewProductModel(Model model,
            @ModelAttribute("newProduct") @Valid Product product,
            BindingResult newProductBindingResult,
            @RequestParam("avatarProduct") MultipartFile avatarFile) {
        List<FieldError> errors = newProductBindingResult.getFieldErrors();
        errors.forEach(e -> {
            System.out.println(e.getField() + " " + e.getDefaultMessage());
        });
        if (newProductBindingResult.hasErrors()) {
            return "/admin/product/create";
        }
        String avatarProductPath = uploadService.handleSaveUploadFile(avatarFile, "product");
        product.setImage(avatarProductPath);
        product.setSold(0L);
        productService.saveProduct(product);
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/update/{id}")
    public String getUpdateProductPage(Model model, @PathVariable Long id) {
        Product product = productService.getProductById(id);
        model.addAttribute("newProduct", product);
        return "admin/product/update";
    }

    @PostMapping("/admin/product/update")
    public String postUpdate(Model model,
            @ModelAttribute("newProduct") @Valid Product product,
            BindingResult newProductBindingResult,
            @RequestParam("avatarProduct") MultipartFile file) {
        List<FieldError> errors = newProductBindingResult.getFieldErrors();
        errors.forEach(e -> {
            System.out.println(e.getField() + " " + e.getDefaultMessage());
        });
        if (newProductBindingResult.hasErrors()) {
            return "/admin/product/update";
        }

        Product officialProduct = productService.getProductById(product.getId());
        if (officialProduct != null) {
            officialProduct.setDetailDesc(product.getDetailDesc());
            officialProduct.setShortDesc(product.getShortDesc());
            officialProduct.setFactory(product.getFactory());
            officialProduct.setName(product.getName());
            officialProduct.setTarget(product.getTarget());
            officialProduct.setPrice(product.getPrice());

            String avatarProduct = uploadService.handleSaveUploadFile(file, "product");
            if (!(avatarProduct == null || avatarProduct.isBlank() || avatarProduct.isEmpty())) {
                officialProduct.setImage(avatarProduct);
            }
            productService.saveProduct(officialProduct);
        }
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String getDeletePage(Model model, @PathVariable Long id) {
        model.addAttribute("id", id);
        Product product = new Product();
        product.setId(id);
        model.addAttribute("product", product);
        return "admin/product/delete";
    }

    @PostMapping("/admin/product/delete")
    public String postDeleteProduct(Model model, @ModelAttribute("product") Product product) {
        productService.deleteProductById(product.getId());
        return "redirect:/admin/product";
    }
}
