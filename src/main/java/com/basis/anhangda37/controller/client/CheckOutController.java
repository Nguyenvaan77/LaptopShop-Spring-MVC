package com.basis.anhangda37.controller.client;

import java.net.http.HttpRequest;
import java.util.List;
import java.util.Optional;

import javax.smartcardio.CardException;

import org.eclipse.tags.shaded.org.apache.bcel.generic.LSTORE;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Producer;
import org.springframework.security.web.authentication.logout.DelegatingLogoutSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;

import com.basis.anhangda37.controller.auth.AuthController;
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
public class CheckOutController {

    private final AuthController authController;

    private final CartDetailRepository cartDetailRepository;
    private final ProductService productService;
    private final UserService userService;
    private final CartService cartService;
    private final CartDetailService cartDetailService;

    public CheckOutController(ProductService productService, UserService userService, CartService cartService,
            CartDetailService cartDetailService, CartDetailRepository cartDetailRepository, AuthController authController) {
        this.productService = productService;
        this.userService = userService;
        this.cartService = cartService;
        this.cartDetailService = cartDetailService;
        this.cartDetailRepository = cartDetailRepository;
        this.authController = authController;
    }

    @GetMapping("/checkout")
    public String getCheckOutPage(Model model) {
        return "client/payment/checkout";
    }

    @GetMapping("/thanks")
    public String getThanksPage() {
        return "client/cart/thanks";
    }

    @PostMapping("/confirm-checkout")
    public String postCheckOut(@ModelAttribute("cart") Cart cart,
                                Model model) {
        List<CartDetail> cartDetails = cart.getCartDetails();
        for (CartDetail cartDetail : cartDetails) {
            cartDetail.setProduct(cartDetailService.getCartDetailById(cartDetail.getId()).get().getProduct());
        }
        List<CartDetailDto> cartDetailDtos = cartDetailService.convertCartDetailToDto(cartDetails);
        Double totalPayment = 0D;
        for (CartDetail cartDetail : cartDetails) {
            totalPayment += cartDetail.getQuantity() * cartDetail.getProduct().getPrice();
        }
        model.addAttribute("cartDetails", cartDetailDtos);
        model.addAttribute("totalPayment", totalPayment);
        return "client/payment/checkout";
    }

    @PostMapping("/confirm-order")
    public String postConfirmOrder(HttpServletRequest request,
                                    @RequestParam("customerName") String name,
                                   @RequestParam("customerAddress") String address,
                                   @RequestParam("customerPhone") String phone,
                                   @RequestParam("totalPayment") Double totalPayment,
                                   @ModelAttribute("cart") Cart cart,
                                   Model model
                                   ) {
    HttpSession session = request.getSession();
    String email = ((String)session.getAttribute("email"));
    List<CartDetail> cartDetails = cart.getCartDetails();
    String orderCode = productService.handleCheckOut(email, session, name, address, phone, totalPayment, cartDetails);
    model.addAttribute("orderCode", orderCode);
    return "client/cart/thanks";
    }
}
