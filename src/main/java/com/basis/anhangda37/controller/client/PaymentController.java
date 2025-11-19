package com.basis.anhangda37.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PaymentController {
    @GetMapping("/order/payment/success")
    public String getSuccessPageAfterStatusOrderQuery(@RequestParam("orderId") Long orderId, Model model) {
        model.addAttribute("orderCode", orderId);
        return "client/payment/querycheckout/success";
    }

    @GetMapping("/order/payment/failed")
    public String getFailedPageAfterStatusOrderQuery(@RequestParam("orderId") Long orderId, Model model) {
        model.addAttribute("orderCode", orderId);
        return "client/payment/querycheckout/failed";
    }

    @GetMapping("/order/payment/pending")
    public String getPendingPageAfterStatusOrderQuery(@RequestParam("orderId") Long orderId, Model model) {
        model.addAttribute("orderCode", orderId);
        return "client/payment/querycheckout/pending";
    }
}
