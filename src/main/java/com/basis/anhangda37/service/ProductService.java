package com.basis.anhangda37.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.basis.anhangda37.domain.Cart;
import com.basis.anhangda37.domain.CartDetail;
import com.basis.anhangda37.domain.Order;
import com.basis.anhangda37.domain.OrderDetail;
import com.basis.anhangda37.domain.Product;
import com.basis.anhangda37.domain.User;
import com.basis.anhangda37.domain.dto.ProductCriteriaDto;
import com.basis.anhangda37.exception.CartNotFoundException;
import com.basis.anhangda37.exception.ProductNotFoundException;
import com.basis.anhangda37.exception.UserNotFoundException;
import com.basis.anhangda37.repository.CartDetailRepository;
import com.basis.anhangda37.repository.CartRepository;
import com.basis.anhangda37.repository.OrderDetailRepository;
import com.basis.anhangda37.repository.OrderRepository;
import com.basis.anhangda37.repository.ProductRepository;
import com.basis.anhangda37.repository.UserRepository;
import com.basis.anhangda37.service.iface.IProductService;
import com.basis.anhangda37.service.specification.ProductSpec;
import com.basis.anhangda37.util.AppConstants;

import jakarta.servlet.http.HttpSession;

/**
 * Service class for Product-related operations.
 * Handles product management, filtering, and business logic related to products.
 * Implements clean separation of concerns and dependency injection.
 */
@Service
public class ProductService implements IProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;

    /**
     * Constructs a ProductService with required repositories.
     * Uses constructor injection for dependency management.
     */
    public ProductService(ProductRepository productRepository, CartRepository cartRepository,
            CartDetailRepository cartDetailRepository, OrderRepository orderRepository,
            OrderDetailRepository orderDetailRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves a product by its ID.
     * @param id The product ID
     * @return The product
     * @throws ProductNotFoundException if the product doesn't exist
     */
    @Override
    public Product getProductById(Long id) {
        logger.debug("Fetching product with id: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Product not found with id: {}", id);
                    return new ProductNotFoundException(id);
                });
    }

    /**
     * Retrieves all products.
     * @return A list of all products
     */
    @Override
    public List<Product> getAllProducts() {
        logger.debug("Fetching all products");
        return productRepository.findAll();
    }

    /**
     * Retrieves all products with pagination.
     * @param pageable The pagination information
     * @return A page of products
     */
    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        logger.debug("Fetching products with pagination: page={}, size={}", 
                pageable.getPageNumber(), pageable.getPageSize());
        return productRepository.findAll(pageable);
    }

    /**
     * Retrieves all products with pagination.
     * @param pageable The pagination information
     * @return A page of products
     */
    @Override
    public Page<Product> fetchProductWithSpecification(Pageable pageable, ProductCriteriaDto dto) {
        Specification<Product> spec = Specification.where(null);

    // filter factory
    if (dto.getFactory().isPresent() && !dto.getFactory().get().isEmpty()) {
        spec = spec.and(ProductSpec.belongListFactory(dto.getFactory().get()));
    }

    // filter target
    if (dto.getTarget().isPresent() && !dto.getTarget().get().isEmpty()) {
        spec = spec.and(ProductSpec.belongListTarget(dto.getTarget().get()));
    }

    // filter price range
    if (dto.getPrice().isPresent() && !dto.getPrice().get().isEmpty()) {

        List<Double[]> priceRanges = dto.getPrice().get().stream()
                .map(item -> {
                    String[] arr = item.split("-");
                    Double min = Double.parseDouble(arr[0]);
                    Double max = Double.parseDouble(arr[1]);
                    return new Double[]{min, max};
                })
                .toList();

        spec = spec.and(ProductSpec.listRangePrice(priceRanges));
    }

        return productRepository.findAll(spec, pageable);
    }

    /**
     * Searches for products by name with pagination.
     * @param name The product name to search for
     * @param pageable The pagination information
     * @return A page of matching products
     */
    @Override
    public Page<Product> searchProductsByName(String name, Pageable pageable) {
        logger.debug("Searching products by name: {}", name);
        return productRepository.findAll(ProductSpec.nameLike(name), pageable);
    }

    /**
     * Saves or updates a product.
     * @param product The product to save
     * @return The saved product
     */
    @Override
    public Product saveProduct(Product product) {
        logger.info("Saving product: {}", product.getName());
        Product savedProduct = productRepository.save(product);
        logger.debug("Product saved successfully with id: {}", savedProduct.getId());
        return savedProduct;
    }

    /**
     * Deletes a product by ID.
     * @param id The product ID
     */
    @Override
    public void deleteProductById(Long id) {
        logger.info("Deleting product with id: {}", id);
        if (!productRepository.existsById(id)) {
            logger.warn("Attempted to delete non-existent product with id: {}", id);
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
        logger.debug("Product deleted successfully with id: {}", id);
    }

    /**
     * Returns the total count of products.
     * @return The product count
     */
    @Override
    public long countProducts() {
        logger.debug("Counting total products");
        return productRepository.count();
    }

    /**
     * Retrieves products filtered by factory with pagination.
     * @param factories The list of factory names
     * @param pageable The pagination information
     * @return A page of matching products
     */
    @Override
    public Page<Product> getProductsByFactories(List<String> factories, Pageable pageable) {
        logger.debug("Fetching products by factories: {}", factories);
        return productRepository.findAll(ProductSpec.belongListFactory(factories), pageable);
    }

    /**
     * Retrieves products filtered by price range with pagination.
     * @param priceRanges The list of price range strings
     * @param pageable The pagination information
     * @return A page of matching products
     */
    @Override
    public Page<Product> getProductsByPriceRanges(List<String> priceRanges, Pageable pageable) {
        logger.debug("Fetching products by price ranges: {}", priceRanges);
        List<Double[]> priceThresholds = convertPriceRangesToThresholds(priceRanges);
        return productRepository.findAll(ProductSpec.listRangePrice(priceThresholds), pageable);
    }

    /**
     * Retrieves products with advanced filtering based on criteria.
     * @param page The pagination information
     * @param criteria The filtering criteria
     * @return A page of matching products
     */
    @Override
    public Page<Product> getProductsByAdvancedCriteria(Pageable page, ProductCriteriaDto criteria) {
        logger.debug("Fetching products with advanced criteria: {}", criteria);
        Specification<Product> combinedSpec = Specification.allOf();

        if (criteria.getFactory() != null && criteria.getFactory().isPresent()) {
            Specification<Product> factorySpec = ProductSpec.belongListFactory(criteria.getFactory().get());
            combinedSpec = combinedSpec.and(factorySpec);
        }

        if (criteria.getTarget() != null && criteria.getTarget().isPresent()) {
            Specification<Product> targetSpec = ProductSpec.belongListTarget(criteria.getTarget().get());
            combinedSpec = combinedSpec.and(targetSpec);
        }

        if (criteria.getPrice() != null && criteria.getPrice().isPresent()) {
            List<Double[]> priceThresholds = convertPriceRangesToThresholds(criteria.getPrice().get());
            Specification<Product> priceSpec = ProductSpec.listRangePrice(priceThresholds);
            combinedSpec = combinedSpec.and(priceSpec);
        }

        Pageable sortedPage = page;
        if (criteria.getSort() != null && criteria.getSort().isPresent()) {
            String sortOption = criteria.getSort().get();
            if (!sortOption.equals("khong")) {
                Sort sort = sortOption.equals("tang-dan") 
                    ? Sort.by(Sort.Direction.ASC, "price")
                    : Sort.by(Sort.Direction.DESC, "price");
                sortedPage = PageRequest.of(page.getPageNumber(), page.getPageSize(), sort);
            }
        }

        return productRepository.findAll(combinedSpec, sortedPage);
    }

    /**
     * Handles adding a product to the user's cart.
     * Creates a new cart if the user doesn't have one, or updates existing cart detail.
     * @param session The HTTP session
     * @param email The user's email
     * @param productId The product ID to add
     * @param quantityToAdd The quantity to add
     */
    @Transactional
    public void handleProductToCart(HttpSession session, String email, Long productId, Long quantityToAdd) {
        logger.info("Adding product {} to cart for user {}", productId, email);
        
        User user = userRepository.findByEmail(email);
        if (user == null) {
            logger.error("User not found with email: {}", email);
            throw new UserNotFoundException("email", email);
        }

        Product product = getProductById(productId);
        Cart cart = cartRepository.findByUser(user);

        if (cart == null) {
            cart = createNewCart(user, product, quantityToAdd);
            logger.debug("Created new cart for user: {}", email);
        } else {
            updateCart(cart, product, quantityToAdd);
            logger.debug("Updated existing cart for user: {}", email);
        }

        session.setAttribute(AppConstants.SESSION_CART_SUM, cart.getSum());
        logger.info("Product added to cart successfully");
    }

    /**
     * Handles checkout process: updates product quantities and creates an order.
     * @param email The user's email
     * @param session The HTTP session
     * @param receiverName The receiver's name
     * @param receiverAddress The receiver's address
     * @param receiverPhone The receiver's phone
     * @param totalPayment The total payment amount
     * @param cartDetails The cart details to checkout
     * @return The order code/ID as a string
     */
    @Transactional
    public String handleCheckOut(String email, HttpSession session, String receiverName, String receiverAddress,
            String receiverPhone, Double totalPayment, List<CartDetail> cartDetails) {
        logger.info("Processing checkout for user: {}", email);
        
        updateProductQuantitiesBeforeCheckout(cartDetails);
        String orderCode = createOrder(email, session, receiverName, receiverAddress, receiverPhone, totalPayment, cartDetails);
        
        logger.info("Checkout completed successfully. Order code: {}", orderCode);
        return orderCode;
    }

    /**
     * Updates product quantities based on cart details before checkout.
     * @param cartDetails The cart details containing quantities to deduct
     */
    private void updateProductQuantitiesBeforeCheckout(List<CartDetail> cartDetails) {
        if (cartDetails == null || cartDetails.isEmpty()) {
            logger.debug("No cart details to process");
            return;
        }

        logger.debug("Updating product quantities for {} items", cartDetails.size());
        for (CartDetail cartDetail : cartDetails) {
            CartDetail fetchedCartDetail = cartDetailRepository.findById(cartDetail.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Cart detail not found"));
            
            Product product = fetchedCartDetail.getProduct();
            Long quantityToSold = cartDetail.getQuantity();
            
            product.setQuantity(product.getQuantity() - quantityToSold);
            productRepository.save(product);
            
            logger.debug("Product {} quantity updated. New quantity: {}", product.getId(), product.getQuantity());
        }
    }

    /**
     * Creates a new cart for the user with initial product.
     * @param user The user
     * @param product The product to add
     * @param quantity The quantity
     * @return The created cart
     */
    private Cart createNewCart(User user, Product product, Long quantity) {
        Cart newCart = new Cart();
        newCart.setUser(user);
        newCart.setSum(0);
        newCart = cartRepository.save(newCart);

        CartDetail cartDetail = new CartDetail();
        cartDetail.setCart(newCart);
        cartDetail.setQuantity(quantity);
        cartDetail.setProduct(product);

        newCart.addCartDetail(cartDetail);
        cartRepository.save(newCart);

        return newCart;
    }

    /**
     * Updates an existing cart with a product.
     * @param cart The cart to update
     * @param product The product to add
     * @param quantityToAdd The quantity to add
     */
    private void updateCart(Cart cart, Product product, Long quantityToAdd) {
        CartDetail existingCartDetail = cartDetailRepository.findByCartAndProduct(cart, product);

        if (existingCartDetail == null) {
            CartDetail newCartDetail = new CartDetail();
            newCartDetail.setProduct(product);
            newCartDetail.setQuantity(quantityToAdd);
            newCartDetail.setCart(cart);
            cart.addCartDetail(newCartDetail);
            cartDetailRepository.save(newCartDetail);
        } else {
            existingCartDetail.setQuantity(existingCartDetail.getQuantity() + quantityToAdd);
            cartDetailRepository.save(existingCartDetail);
            logger.debug("Updated existing cart item quantity");
        }
    }

    /**
     * Creates an order from cart details.
     * @param email The user's email
     * @param session The HTTP session
     * @param receiverName The receiver's name
     * @param receiverAddress The receiver's address
     * @param receiverPhone The receiver's phone
     * @param totalPayment The total payment amount
     * @param cartDetails The cart details
     * @return The order ID as a string
     */
    private String createOrder(String email, HttpSession session, String receiverName, String receiverAddress,
            String receiverPhone, Double totalPayment, List<CartDetail> cartDetails) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            logger.error("User not found for email: {}", email);
            throw new UserNotFoundException("email", email);
        }

        Order order = new Order();
        order.setReceiverAddress(receiverAddress);
        order.setReceiverName(receiverName);
        order.setReceiverPhone(receiverPhone);
        order.setUser(user);
        order.setTotalPrice(totalPayment);

        List<OrderDetail> orderDetails = convertCartDetailsToOrderDetails(order, cartDetails);
        order.setOrderDetails(orderDetails);

        Order savedOrder = orderRepository.save(order);
        logger.debug("Order created with id: {}", savedOrder.getId());

        clearUserCart(user);
        session.setAttribute(AppConstants.SESSION_CART_SUM, 0);

        return String.valueOf(savedOrder.getId());
    }

    /**
     * Converts cart details to order details and updates product sold count.
     * @param order The order
     * @param cartDetails The cart details
     * @return List of order details
     */
    private List<OrderDetail> convertCartDetailsToOrderDetails(Order order, List<CartDetail> cartDetails) {
        List<OrderDetail> orderDetails = new ArrayList<>();

        for (CartDetail cartDetail : cartDetails) {
            OrderDetail orderDetail = new OrderDetail();
            Product product = cartDetail.getProduct();
            
            orderDetail.setQuantity(cartDetail.getQuantity());
            orderDetail.setProduct(product);
            orderDetail.setPrice(product.getPrice() * cartDetail.getQuantity());
            orderDetail.setOrder(order);
            orderDetails.add(orderDetail);

            // Update product sold count
            product.setSold(product.getSold() + cartDetail.getQuantity());
            productRepository.save(product);

            logger.debug("Created order detail for product: {}", product.getId());
        }

        return orderDetails;
    }

    /**
     * Clears the user's cart after checkout.
     * @param user The user
     */
    private void clearUserCart(User user) {
        Cart cart = cartRepository.findByUser(user);
        if (cart != null) {
            cart.removeAllCartDetail();
            user.removeCart();
            cart.setUser(null);
            cartRepository.deleteById(cart.getId());
            logger.debug("User cart cleared after checkout");
        }
    }

    /**
     * Converts price range strings to double arrays with min and max values.
     * Example: "10-toi-15-trieu" becomes [[10.0, 15.0]]
     * @param priceRanges The list of price range strings
     * @return List of double arrays with min and max price
     */
    private List<Double[]> convertPriceRangesToThresholds(List<String> priceRanges) {
        List<Double[]> thresholds = new ArrayList<>();

        for (String rangeString : priceRanges) {
            Double[] range = new Double[2];
            range[0] = extractMinPriceFromRange(rangeString);
            range[1] = extractMaxPriceFromRange(rangeString);
            thresholds.add(range);
        }

        return thresholds;
    }

    /**
     * Extracts minimum price from a price range string.
     * Example: "10-toi-15-trieu" returns 10.0
     * @param rangeString The price range string
     * @return The minimum price as a double
     */
    private Double extractMinPriceFromRange(String rangeString) {
        String[] parts = rangeString.split("-");
        return Double.parseDouble(parts[0]);
    }

    /**
     * Extracts maximum price from a price range string.
     * Example: "10-toi-15-trieu" returns 15.0
     * @param rangeString The price range string
     * @return The maximum price as a double
     */
    private Double extractMaxPriceFromRange(String rangeString) {
        String[] parts = rangeString.split("-");
        return Double.parseDouble(parts[2]);
    }
}
