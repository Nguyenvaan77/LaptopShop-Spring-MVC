package com.basis.anhangda37.controller.admin;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.basis.anhangda37.service.ProductService;
import com.basis.anhangda37.service.UploadService;
import com.basis.anhangda37.util.AppConstants;

import jakarta.validation.Valid;

/**
 * Controller for handling admin product management operations.
 * Manages product CRUD operations for the admin dashboard.
 * Handles HTTP requests/responses without any business logic.
 */
@Controller
public class ProductDashboardController {

    private static final Logger logger = LoggerFactory.getLogger(ProductDashboardController.class);

    @Value("${admin.dashboard.size.product}")
    private int pageSizeOfProductDashboard;

    private final ProductService productService;
    private final UploadService uploadService;

    /**
     * Constructs a ProductDashboardController with required dependencies.
     */
    public ProductDashboardController(ProductService productService, UploadService uploadService) {
        this.productService = productService;
        this.uploadService = uploadService;
    }

    /**
     * Displays the product dashboard with paginated list.
     * GET endpoint: /admin/product
     * @param model The model to add attributes
     * @param page The page number (default: 0)
     * @return The product list view name
     */
    @GetMapping("/admin/product")
    public String getDashboard(Model model, @RequestParam(name = "page", defaultValue = "0") int page) {
        logger.debug("Loading product dashboard for page: {}", page);
        
        Pageable pageable = PageRequest.of(page, pageSizeOfProductDashboard);
        Page<Product> productPage = productService.getAllProducts(pageable);
        List<Product> products = productPage.getContent();

        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        logger.info("Displayed product dashboard with {} products on page {}", products.size(), page);
        return "admin/product/show";
    }

    /**
     * Displays detailed information of a specific product.
     * GET endpoint: /admin/product/{id}
     * @param id The product ID
     * @param model The model to add attributes
     * @return The product detail view name
     */
    @GetMapping("/admin/product/{id}")
    public String getProductDetailPage(@PathVariable Long id, Model model) {
        logger.debug("Loading product detail page for id: {}", id);
        
        Product product = productService.getProductById(id);
        model.addAttribute("id", id);
        model.addAttribute("product", product);

        logger.info("Product detail page displayed for id: {}", id);
        return "admin/product/detail";
    }

    /**
     * Displays the product creation form.
     * GET endpoint: /admin/product/create
     * @param model The model to add attributes
     * @return The product creation form view name
     */
    @GetMapping("/admin/product/create")
    public String getProductCreationForm(Model model) {
        logger.debug("Loading product creation form");
        
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }

    /**
     * Handles product creation.
     * POST endpoint: /admin/product/create
     * @param model The model
     * @param product The product entity
     * @param bindingResult The validation binding result
     * @param avatarFile The product image file
     * @return Redirect to product dashboard on success, back to creation form on error
     */
    @PostMapping("/admin/product/create")
    public String createProduct(
            Model model,
            @ModelAttribute("newProduct") @Valid Product product,
            BindingResult bindingResult,
            @RequestParam("avatarProduct") MultipartFile avatarFile) {

        logger.info("Creating new product: {}", product.getName());

        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            logger.warn("Product creation validation failed with {} errors", errors.size());
            errors.forEach(error ->
                logger.debug("Field: {}, Message: {}", error.getField(), error.getDefaultMessage())
            );
            return "/admin/product/create";
        }

        try {
            String imagePath = uploadService.handleSaveUploadFile(avatarFile, AppConstants.UPLOAD_DIR_PRODUCT);
            product.setImage(imagePath);
            product.setSold(0L);

            productService.saveProduct(product);
            logger.info("Product created successfully: {} with id: {}", product.getName(), product.getId());
            
            return "redirect:/admin/product";
        } catch (Exception ex) {
            logger.error("Error creating product: {}", ex.getMessage(), ex);
            bindingResult.reject("product.error", "An error occurred while creating the product");
            return "/admin/product/create";
        }
    }

    /**
     * Displays the product update form.
     * GET endpoint: /admin/product/update/{id}
     * @param id The product ID
     * @param model The model to add attributes
     * @return The product update form view name
     */
    @GetMapping("/admin/product/update/{id}")
    public String getProductUpdateForm(@PathVariable Long id, Model model) {
        logger.debug("Loading product update form for id: {}", id);
        
        Product product = productService.getProductById(id);
        model.addAttribute("newProduct", product);

        return "admin/product/update";
    }

    /**
     * Handles product update.
     * POST endpoint: /admin/product/update
     * @param model The model
     * @param product The updated product entity
     * @param bindingResult The validation binding result
     * @param file The product image file
     * @return Redirect to product dashboard on success, back to update form on error
     */
    @PostMapping("/admin/product/update")
    public String updateProduct(
            Model model,
            @ModelAttribute("newProduct") @Valid Product product,
            BindingResult bindingResult,
            @RequestParam("avatarProduct") MultipartFile file) {

        logger.info("Updating product with id: {}", product.getId());

        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            logger.warn("Product update validation failed with {} errors", errors.size());
            errors.forEach(error ->
                logger.debug("Field: {}, Message: {}", error.getField(), error.getDefaultMessage())
            );
            return "/admin/product/update";
        }

        try {
            Product existingProduct = productService.getProductById(product.getId());
            
            // Update product fields
            existingProduct.setDetailDesc(product.getDetailDesc());
            existingProduct.setShortDesc(product.getShortDesc());
            existingProduct.setFactory(product.getFactory());
            existingProduct.setName(product.getName());
            existingProduct.setTarget(product.getTarget());
            existingProduct.setPrice(product.getPrice());

            // Update image if provided
            String imagePath = uploadService.handleSaveUploadFile(file, AppConstants.UPLOAD_DIR_PRODUCT);
            if (imagePath != null && !imagePath.isBlank()) {
                existingProduct.setImage(imagePath);
                logger.debug("Product image updated for id: {}", product.getId());
            }

            productService.saveProduct(existingProduct);
            logger.info("Product updated successfully with id: {}", product.getId());
            
            return "redirect:/admin/product";
        } catch (Exception ex) {
            logger.error("Error updating product: {}", ex.getMessage(), ex);
            bindingResult.reject("product.error", "An error occurred while updating the product");
            return "/admin/product/update";
        }
    }

    /**
     * Displays the product deletion confirmation page.
     * GET endpoint: /admin/product/delete/{id}
     * @param id The product ID
     * @param model The model to add attributes
     * @return The product deletion confirmation view name
     */
    @GetMapping("/admin/product/delete/{id}")
    public String getProductDeletionConfirmation(@PathVariable Long id, Model model) {
        logger.debug("Loading product deletion confirmation for id: {}", id);
        
        model.addAttribute("id", id);
        Product product = new Product();
        product.setId(id);
        model.addAttribute("product", product);

        return "admin/product/delete";
    }

    /**
     * Handles product deletion.
     * POST endpoint: /admin/product/delete
     * @param model The model
     * @param product The product to delete
     * @return Redirect to product dashboard
     */
    @PostMapping("/admin/product/delete")
    public String deleteProduct(Model model, @ModelAttribute("product") Product product) {
        logger.info("Deleting product with id: {}", product.getId());
        
        try {
            productService.deleteProductById(product.getId());
            logger.info("Product deleted successfully with id: {}", product.getId());
            
            return "redirect:/admin/product";
        } catch (Exception ex) {
            logger.error("Error deleting product: {}", ex.getMessage(), ex);
            return "redirect:/admin/product?error=delete_failed";
        }
    }
}
