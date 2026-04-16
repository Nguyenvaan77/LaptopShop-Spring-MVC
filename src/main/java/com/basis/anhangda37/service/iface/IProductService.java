package com.basis.anhangda37.service.iface;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.basis.anhangda37.domain.Product;
import com.basis.anhangda37.domain.dto.ProductCriteriaDto;

import java.util.List;

/**
 * Service interface for Product-related operations.
 * Defines contracts for product management including CRUD operations and filtering.
 */
public interface IProductService {

    /**
     * Retrieves a product by its ID.
     * @param id The product ID
     * @return The product, or null if not found
     * @throws com.basis.anhangda37.exception.ProductNotFoundException if product doesn't exist
     */
    Product getProductById(Long id);

    /**
     * Retrieves all products with pagination.
     * @param pageable The pagination information
     * @return A page of products
     */
    Page<Product> getAllProducts(Pageable pageable);

    /**
     * Retrieves all products.
     * @return A list of all products
     */
    List<Product> getAllProducts();

    /**
     * Retrieves all filtered products .
     * @param pageable The pagination information
     * @param dto the filtering information
     * @return A list of filtered products
     */
    Page<Product> fetchProductWithSpecification(Pageable pageable, ProductCriteriaDto dto);

    /**
     * Searches for products by name with pagination.
     * @param name The product name to search for
     * @param pageable The pagination information
     * @return A page of matching products
     */
    Page<Product> searchProductsByName(String name, Pageable pageable);

    /**
     * Saves or updates a product.
     * @param product The product to save
     * @return The saved product
     */
    Product saveProduct(Product product);

    /**
     * Deletes a product by ID.
     * @param id The product ID
     */
    void deleteProductById(Long id);

    /**
     * Returns the total count of products.
     * @return The product count
     */
    long countProducts();

    /**
     * Retrieves products filtered by factory with pagination.
     * @param factories The list of factory names
     * @param pageable The pagination information
     * @return A page of matching products
     */
    Page<Product> getProductsByFactories(List<String> factories, Pageable pageable);

    /**
     * Retrieves products filtered by price range with pagination.
     * @param priceRanges The list of price range strings
     * @param pageable The pagination information
     * @return A page of matching products
     */
    Page<Product> getProductsByPriceRanges(List<String> priceRanges, Pageable pageable);

    /**
     * Retrieves products with advanced filtering based on criteria.
     * @param page The pagination information
     * @param criteria The filtering criteria
     * @return A page of matching products
     */
    Page<Product> getProductsByAdvancedCriteria(Pageable page, ProductCriteriaDto criteria);
}
