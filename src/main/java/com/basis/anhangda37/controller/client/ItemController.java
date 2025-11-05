package com.basis.anhangda37.controller.client;

import java.net.http.HttpRequest;
import java.util.List;
import java.util.Optional;

import javax.smartcardio.CardException;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Producer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.basis.anhangda37.domain.Cart;
import com.basis.anhangda37.domain.CartDetail;
import com.basis.anhangda37.domain.Product;
import com.basis.anhangda37.domain.User;
import com.basis.anhangda37.domain.dto.CartDetailDto;
import com.basis.anhangda37.repository.CartDetailRepository;
import com.basis.anhangda37.service.CartDetailService;
import com.basis.anhangda37.service.CartService;
import com.basis.anhangda37.service.ProductService;
import com.basis.anhangda37.service.UserService;
import com.mysql.cj.exceptions.DeadlockTimeoutRollbackMarker;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ItemController {

    private final CartDetailRepository cartDetailRepository;
    private final ProductService productService;
    private final UserService userService;
    private final CartService cartService;
    private final CartDetailService cartDetailService;

    public ItemController(ProductService productService, UserService userService, CartService cartService,
            CartDetailService cartDetailService, CartDetailRepository cartDetailRepository) {
        this.productService = productService;
        this.userService = userService;
        this.cartService = cartService;
        this.cartDetailService = cartDetailService;
        this.cartDetailRepository = cartDetailRepository;
    }

    @GetMapping("/product/{id}")
    public String getProductDetail(Model model, @PathVariable("id") Long id) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "client/product/detail";
    }

    @GetMapping("/detail")
    public String getMethodName() {
        return "client/product/detail";
    }

    @GetMapping("/cart")
    public String getCartPage(Model model, HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        User user = userService.getUserByEmail((String) session.getAttribute("email"));
        Cart cart = cartService.findCartByUser(user);
        List<CartDetail> cartDetails = cartDetailService.getCartDetailByCart(cart);
        List<CartDetailDto> cartDetailDtos = cartDetailService.convertCartDetailToDto(cartDetails);
        double totalPayment = 0;
        for (CartDetailDto cartDetailDto : cartDetailDtos) {
            totalPayment += cartDetailDto.getTotal();
        }
        model.addAttribute("cartDetails", cartDetailDtos);
        model.addAttribute("totalPayment", totalPayment);
        model.addAttribute("cart", cart);
        return "client/cart/show";
    }

    @PostMapping("/remove-product-from-cart/{productId}")
    public String removeProductFromCart(@PathVariable Long productId, HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        Optional<CartDetail> optional = cartDetailService.getCartDetailById(productId);
        if (optional.isPresent()) {
            CartDetail currentCartDetail = optional.get();
            Cart cart = currentCartDetail.getCart();

            cart.removeCartDetail(currentCartDetail);
            cartDetailService.deleteCartDetail(currentCartDetail);
            session.setAttribute("sum", cart.getSum());
        }
        return "redirect:/cart";
    }

}
