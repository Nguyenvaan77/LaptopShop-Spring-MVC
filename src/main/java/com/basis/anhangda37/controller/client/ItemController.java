package com.basis.anhangda37.controller.client;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.basis.anhangda37.domain.Cart;
import com.basis.anhangda37.domain.CartDetail;
import com.basis.anhangda37.domain.Product;
import com.basis.anhangda37.domain.User;
import com.basis.anhangda37.domain.dto.CartDetailDto;
import com.basis.anhangda37.service.CartDetailService;
import com.basis.anhangda37.service.CartService;
import com.basis.anhangda37.service.ProductService;
import com.basis.anhangda37.service.UserService;
import com.basis.anhangda37.util.AppConstants;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Controller for handling client-side product and cart operations.
 * Manages user interactions with products and shopping cart without business logic.
 * Follows MVC pattern with proper separation of concerns.
 */
@Controller
public class ItemController {

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    private final ProductService productService;
    private final UserService userService;
    private final CartService cartService;
    private final CartDetailService cartDetailService;

    /**
     * Constructs an ItemController with required dependencies.
     */
    public ItemController(
            ProductService productService,
            UserService userService,
            CartService cartService,
            CartDetailService cartDetailService) {
        this.productService = productService;
        this.userService = userService;
        this.cartService = cartService;
        this.cartDetailService = cartDetailService;
    }

    /**
     * Displays the product detail page.
     * GET endpoint: /product/{id}
     * @param id The product ID
     * @param model The model to add attributes
     * @return The product detail view name
     */
    @GetMapping("/product/{id}")
    public String getProductDetail(@PathVariable("id") Long id, Model model) {
        logger.debug("Loading product detail for id: {}", id);
        
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);

        return "client/product/detail";
    }

    /**
     * Displays the shopping cart page.
     * GET endpoint: /cart
     * @param httpRequest The HTTP servlet request to get session
     * @param model The model to add attributes
     * @return The cart view name or empty cart view
     * @throws IOException if an IO error occurs
     */
    @GetMapping("/cart")
    public String getCartPage(HttpServletRequest httpRequest, Model model) throws IOException {
        logger.debug("Loading shopping cart");
        
        HttpSession session = httpRequest.getSession();
        String userEmail = (String) session.getAttribute(AppConstants.SESSION_USER_EMAIL);

        if (userEmail == null) {
            logger.warn("Attempted to access cart without active session");
            return "redirect:/login";
        }

        User user = userService.getUserByEmail(userEmail);
        if (user == null) {
            logger.error("User not found for email: {}", userEmail);
            return "redirect:/login";
        }

        Cart cart = cartService.findOrCreateCartForUser(user);
        List<CartDetail> cartDetails = cartDetailService.getCartDetailsByCart(cart);

        if (cartDetails.isEmpty()) {
            logger.debug("Cart is empty for user: {}", userEmail);
            return "client/cart/empty-cart";
        }

        // Convert to DTOs and calculate total
        List<CartDetailDto> cartDetailDtos = cartDetailService.convertToCartDetailDtos(cartDetails);
        double totalPayment = cartDetailDtos.stream()
                .mapToDouble(dto -> dto.getPrice() * dto.getQuantity())
                .sum();

        model.addAttribute("cartDetails", cartDetailDtos);
        model.addAttribute("totalPayment", totalPayment);
        model.addAttribute("cart", cart);

        logger.info("Cart displayed for user: {} with {} items", userEmail, cartDetails.size());
        return "client/cart/show";
    }

    /**
     * Handles removing a product from the cart.
     * POST endpoint: /remove-product-from-cart/{productId}
     * @param productId The cart detail ID
     * @param httpRequest The HTTP servlet request to get session
     * @return Redirect to cart page
     */
    @PostMapping("/remove-product-from-cart/{productId}")
    public String removeProductFromCart(@PathVariable Long productId, HttpServletRequest httpRequest) {
        logger.info("Removing product from cart with id: {}", productId);
        
        HttpSession session = httpRequest.getSession();
        Optional<CartDetail> cartDetailOpt = cartDetailService.getCartDetailById(productId);

        if (cartDetailOpt.isPresent()) {
            CartDetail cartDetail = cartDetailOpt.get();
            Cart cart = cartDetail.getCart();

            cart.removeCartDetail(cartDetail);
            cartDetailService.deleteCartDetail(cartDetail);
            
            session.setAttribute(AppConstants.SESSION_CART_SUM, cart.getSum());
            logger.info("Product removed successfully from cart");
        } else {
            logger.warn("Cart detail not found for id: {}", productId);
        }

        return "redirect:/cart";
    }
}
