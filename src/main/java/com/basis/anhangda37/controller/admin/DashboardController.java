package com.basis.anhangda37.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;

import com.basis.anhangda37.service.OrderService;
import com.basis.anhangda37.service.ProductService;
import com.basis.anhangda37.service.UserService;

@Controller
public class DashboardController {
    private final ProductService productService;
    private final UserService userService;
    private final OrderService orderService;

    public DashboardController(ProductService productService, UserService userService, OrderService orderService) {
        this.productService = productService;
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/admin")
    public String getDashboard(Model model) {
        model.addAttribute("userCount", userService.countUser());
        model.addAttribute("productCount", productService.countProduct());
        model.addAttribute("orderCount", orderService.countOrder());
        return "admin/dashboard/show";
    }

}
