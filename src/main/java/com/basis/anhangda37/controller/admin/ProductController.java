package com.basis.anhangda37.controller.admin;

import java.util.List;

import org.aspectj.util.LangUtil.ProcessController;
import org.springframework.data.domain.jaxb.SortAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.basis.anhangda37.domain.Product;
import com.basis.anhangda37.domain.User;
import com.basis.anhangda37.repository.ProductRepository;
import com.basis.anhangda37.service.ProductService;
import com.basis.anhangda37.service.UploadService;

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

    @GetMapping("/admin/product/create")
    public String routeProductTable(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }

    @PostMapping("/admin/product/create")
    public String getNewProductModel(Model model,
                                    @ModelAttribute("newProduct") Product product,
                                    @RequestParam("avatarProduct") MultipartFile avatarFile
                                    ) {
        String avatarProductPath = uploadService.handleSaveUploadFile(avatarFile, "product");
        product.setImage(avatarProductPath);
        product.setSold(0L);
        productService.saveProduct(product);
        return "redirect:/admin/product";
    }
    
}
