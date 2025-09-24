package com.basis.anhangda37.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ItemController {
    @GetMapping("/product/shop-detail")
    public String getProductDetail() {
        return "client/product/shop-detail";
    }
}
