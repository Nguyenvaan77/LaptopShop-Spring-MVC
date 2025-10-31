package com.basis.anhangda37.controller.client;

import java.util.List;

import org.springframework.boot.autoconfigure.jms.JmsProperties.Listener.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.basis.anhangda37.domain.Order;
import com.basis.anhangda37.domain.Product;
import com.basis.anhangda37.domain.User;
import com.basis.anhangda37.service.OrderService;
import com.basis.anhangda37.service.ProductService;
import com.basis.anhangda37.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpServerErrorException;

@Controller
public class HomePageController {
    private final ProductService productService;
    private final UserService userService;
    private final OrderService orderService;

    public HomePageController(ProductService productService, UserService userService, OrderService orderService) {
        this.productService = productService;
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/")
    public String getHomePage(Model model) {
        List<Product> products = productService.getAllProduct();
        model.addAttribute("products", products);
        return "client/homepage/show";

    }

    @GetMapping("/access-deny")
    public String getAccessDenyPage() {
        return "client/homepage/deny";
    }

    @PostMapping("/add-product-to-cart/{productId}")
    public String addProductToCart(@PathVariable Long productId, HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        String email = (String) session.getAttribute("email");
        productService.handleProductToCart(session, email, productId, 1L);
        return "redirect:/";
    }

    @PostMapping("/add-product-to-cart")
    public String addProductToCartFromProductDetail(@ModelAttribute("product") Product product,
            HttpServletRequest request) {
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");
        productService.handleProductToCart(session, email, product.getId(), product.getQuantity());
        return "redirect:/";
    }

    @GetMapping("/order-history")
    public String getOrderHistoryPage(HttpServletRequest request, Model model) {
        User user = userService.getUserByEmail((String) request.getSession().getAttribute("email"));
        List<Order> orders = orderService.getAllOrdersByUser(user);
        model.addAttribute("orders", orders);
        return "client/order/order-history";
    }
}
