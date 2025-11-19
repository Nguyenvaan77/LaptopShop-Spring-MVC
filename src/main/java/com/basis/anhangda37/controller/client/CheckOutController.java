package com.basis.anhangda37.controller.client;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.basis.anhangda37.domain.Cart;
import com.basis.anhangda37.domain.CartDetail;
import com.basis.anhangda37.domain.dto.CartDetailDto;
import com.basis.anhangda37.service.CartDetailService;
@Controller
public class CheckOutController {


    private final CartDetailService cartDetailService;

    public CheckOutController(CartDetailService cartDetailService) {
        
        this.cartDetailService = cartDetailService;
    }

    // @GetMapping("/checkout")
    // public String getCheckOutPage(Model model) {
    //     return "client/payment/checkout";
    // }

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

    // @PostMapping("/confirm-order")
    // public String postConfirmOrder(HttpServletRequest request,
    //         @RequestParam("customerName") String name,
    //         @RequestParam("customerAddress") String address,
    //         @RequestParam("customerPhone") String phone,
    //         @RequestParam("totalPayment") Double totalPayment,
    //         @ModelAttribute("cart") Cart cart,
    //         Model model) {
    //     HttpSession session = request.getSession();
    //     String email = ((String) session.getAttribute("email"));
    //     List<CartDetail> cartDetails = cart.getCartDetails();
    //     String orderCode = productService.handleCheckOut(email, session, name, address, phone, totalPayment,
    //             cartDetails);
    //     model.addAttribute("orderCode", orderCode);
    //     return "client/cart/thanks";
    // }

}
